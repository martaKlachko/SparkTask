
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.util.LongAccumulator;
import scala.Tuple2;
import org.apache.spark.sql.SparkSession;


import java.text.DateFormat;
import java.text.ParseException;
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


    public static void main(String[] args) throws ParseException {
        String csvFile = "src/main/resources/avocado.csv";

        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("Spark2Example");
        sparkConf.setMaster("local");
        JavaSparkContext context = new JavaSparkContext(sparkConf);

        JavaRDD<String> rdd_records = context.textFile(csvFile);

        JavaRDD<String[]> splitted_csv_recordsRDD = rdd_records.map(line -> line.split(","));

        LongAccumulator accum = context.sc().longAccumulator();
        JavaRDD<Avocado> avocadosRDD = splitted_csv_recordsRDD.map(line -> createAvocado(line));
        avocadosRDD.filter(Objects::isNull).foreach(x -> accum.add(1));
        JavaRDD<Avocado> notNullAvocadosRDD = avocadosRDD.filter(Objects::nonNull);

        JavaRDD<Avocado> avocadosFromBoiseRDD = notNullAvocadosRDD
                .filter(x -> x.getRegion().equals("Boise"));
        Long avocadosFromBoiseCount = avocadosFromBoiseRDD.count();
        System.out.println("number of avocados from Boise: " + avocadosFromBoiseCount);
        Double maxAvgPriceFromBoise = avocadosFromBoiseRDD.map(x -> x.getAvgPrice())
                .reduce((x, y) -> Math.max(x, y));
        System.out.println("max average price of avocados from Boise: " + maxAvgPriceFromBoise);
        System.out.println("count of errors while parsing= " + accum.value());


        JavaPairRDD<String, Double> maxPricesByRegionRDD = notNullAvocadosRDD
                .mapToPair(m -> new Tuple2<>(m.getRegion(), m.getAvgPrice()))
                .reduceByKey((a, b) -> Math.max(a, b));
        System.out.println("max prices per region");
        System.out.println(maxPricesByRegionRDD.collect());

        SparkSession spark = SparkSession.builder().getOrCreate();

        Dataset<Row> matchDF = spark.createDataFrame(notNullAvocadosRDD, Avocado.class);

        matchDF.createOrReplaceTempView("avocados");

        Dataset<Row> sqlDF = spark.sql("SELECT from_unixtime(date /1000,\"m/d/yyyy\" )as `date`, region , avgPrice FROM avocados where avgPrice=(" +
                "select max(avgPrice) from avocados)");

        sqlDF.show();

        //while (true) {
//                Thread.sleep(200);
//            }

        context.close();


    }
}
