import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;


public class MainTest {
    @Test
    public void testBuild() throws Exception {


        String line = "16,9/6/2015,1.11,99172.96,879.45,90062.62,240.79,7990.1,7762.87,227.23,0,conventional,2015,Albany";
        DateFormat format = new SimpleDateFormat("m/d/yyyy");
        Date date = format.parse("9/6/2015");
        Avocado expected = new Avocado(date.getTime(), 1.11, 99172.96, "conventional", "Albany");

        Avocado actual = Main.createAvocado(line.split(","));


        assertEquals(actual, expected);

        assertEquals(actual.getRegion(), "Albany");
        assertEquals(Long.valueOf(actual.getDate()), Long.valueOf(date.getTime()));
        assertEquals(actual.getAvgPrice(), 1.11);
        assertEquals(actual.getVolume(), 99172.96);

    }

}