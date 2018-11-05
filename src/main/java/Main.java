import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.util.LongAccumulator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;


public class Main {

    static Avocado createAvocado(String[] metadata) {

        try {
            DateFormat df = new SimpleDateFormat("m/d/yyyy");
            Long date = df.parse(metadata[1]).getTime();

            Double avgPrice = Double.parseDouble(metadata[2]);
            Double volume = Double.parseDouble(metadata[3]);
            String type = metadata[11];

            String region = metadata[13];
            Avocado avocado = new Avocado(date, avgPrice, volume, type, region);

            return avocado;
        } catch (Exception e) {

            return null;
        }

    }


    public static void main(String[] args)  {
        //String csvFile = "src/main/resources/avocado.csv";
        //  String csvFile = "gs://jar_storage/avocado.csv";
        String csvFile = args[0];
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("Spark2Example");
        sparkConf.setMaster("yarn");
        JavaSparkContext context = new JavaSparkContext(sparkConf);
        LongAccumulator accum = context.sc().longAccumulator();

        JavaRDD<String> rdd_records = context.textFile(csvFile);
        JavaRDD<String[]> splitted_csv_recordsRDD = rdd_records.map(line -> line.split(","));


        JavaRDD<Avocado> avocadosRDD = splitted_csv_recordsRDD.map(line -> createAvocado(line));
        avocadosRDD.filter(Objects::isNull).foreach(x -> accum.add(1));
        System.out.println("count of errors while parsing= " + accum.value());

        SparkSession spark = SparkSession.builder().getOrCreate();

        JavaRDD<Avocado> notNullAvocadosRDD = avocadosRDD.filter(Objects::nonNull);
        Dataset<Row> notNullAvocadosDF = spark.createDataFrame(notNullAvocadosRDD, Avocado.class);
        notNullAvocadosDF.write().json("gs://jar_storage/result/notNullAvocados");

        JavaRDD<Avocado> avocadosFromBoiseRDD = notNullAvocadosRDD
                .filter(x -> x.getRegion().equals("Boise"));
        Dataset<Row> avocadosFromBoiseDF = spark.createDataFrame(avocadosFromBoiseRDD, Avocado.class);
        avocadosFromBoiseDF.write().json("gs://jar_storage/result/avocadosFromBoise");

        Long avocadosFromBoiseCount = avocadosFromBoiseRDD.count();
        System.out.println("number of avocados from Boise: " + avocadosFromBoiseCount);
        Double maxAvgPriceFromBoise = avocadosFromBoiseRDD.map(x -> x.getAvgPrice())
                .reduce((x, y) -> Math.max(x, y));
        System.out.println("max average price of avocados from Boise: " + maxAvgPriceFromBoise);


        Dataset<Row> avocadosDF = spark.createDataFrame(notNullAvocadosRDD, Avocado.class);
        avocadosDF.createOrReplaceTempView("avocados");
        Dataset<Row> maxPriceDF = spark.sql("SELECT from_unixtime(date /1000,\"m/d/yyyy\" )as `date`, region , avgPrice FROM avocados where avgPrice=(" +
                "select max(avgPrice) from avocados)");
        maxPriceDF.write().json("gs://jar_storage/result/maxPriceInAllRegions");


        Dataset<Row> avgPricesByRegionDF = spark.sql("SELECT region, avg(avgPrice) FROM avocados group by region");
        avgPricesByRegionDF.write().json("gs://jar_storage/result/avgPricesByRegion");

        Dataset<Row> maxPricesByRegionDF = spark.sql("SELECT region, max(avgPrice) FROM avocados group by region");
        maxPricesByRegionDF.write().json("gs://jar_storage/result/maxPricesByRegion");


        context.close();


//        while (true) {
//                Thread.sleep(200);
//            }


    }
}
