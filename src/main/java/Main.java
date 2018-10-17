import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.LongAccumulator;

public class Main  {

    static Avocado createAvocado(String[] metadata, LongAccumulator accum) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date ;

        try {
            date = df.parse(metadata[1]);

            Double avgPrice = Double.parseDouble(metadata[2]);
            Double volume = Double.parseDouble(metadata[3]);
            String type = metadata[11];
            DateFormat df1 = new SimpleDateFormat("yyyy");
            Date year;

            year = df1.parse(metadata[12]);

            String region = metadata[13];
            Avocado avocado = new Avocado(date, avgPrice, volume, type, year, region);

            return avocado;
        } catch (Exception e) {
            accum.add(1L);
            System.out.println(accum.value());
            return null;
        }

    }



    public static void main(String[] args)
            throws IOException, ParseException, InterruptedException, ExecutionException {
        String csvFile = "src/main/resources/avocado.csv";

        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("Spark2Example");
        sparkConf.setMaster("local");
        JavaSparkContext context = new JavaSparkContext(sparkConf);

        JavaRDD<String> rdd_records = context.textFile(csvFile);

        JavaRDD<String[]> splitted_csv_recordsRDD = rdd_records.map(line -> line.split(","));
        try {
            LongAccumulator accum = context.sc().longAccumulator();
            JavaRDD<Avocado> avocadosRDD = splitted_csv_recordsRDD.map(line -> createAvocado(line, accum));

            JavaRDD<Avocado> avocadosFromBoiseRDD = avocadosRDD.filter(Objects::nonNull)
                    .filter(x -> x.getRegion().equals("Boise"));
            Long avocadosFromBoiseCount = avocadosFromBoiseRDD.count();
            System.out.println("number of avocados from Boise: " + avocadosFromBoiseCount);
            Double maxAvgPriceFromBoise = avocadosFromBoiseRDD.map(x -> x.getAvgPrice())
                    .reduce((x, y) -> Math.max(x, y));
            System.out.println("max average price of avocados from Boise: " + maxAvgPriceFromBoise);
            System.out.println("count= " + accum.count());

            JavaPairRDD<Object, Iterable<Avocado>> grouppedRDD = avocadosRDD.filter(Objects::nonNull)
                    .groupBy(avocado -> avocado.getDate());
            //System.out.println(grouppedRDD.collect());
            while (true) {
                Thread.sleep(200);
            }

        } catch (NullPointerException npe) {

            System.out.println("Null pointer exception");
        }

        context.close();
    }
}
