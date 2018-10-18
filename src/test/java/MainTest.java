import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;


public class MainTest {
    @Test
    public void testBuildObject() throws ParseException {

        String line = "16,9/6/2015,1.11,99172.96,879.45,90062.62,240.79,7990.1,7762.87,227.23,0,conventional,2015,Albany";
        DateFormat format = new SimpleDateFormat("m/d/yyyy");
        Date date = format.parse("9/6/2015");
        Avocado expected = new Avocado(date.getTime(), 1.11, 99172.96, "conventional", "Albany");
        Avocado actual = Main.createAvocado(line.split(","));

        assertNotNull(actual);
        assertEquals(actual, expected);
        assertEquals(actual.getRegion(), "Albany");
        assertEquals(Long.valueOf(actual.getDate()), Long.valueOf(date.getTime()));
        assertEquals(actual.getAvgPrice(), 1.11);
        assertEquals(actual.getVolume(), 99172.96);

    }

    @Test
    public void testWrongDateFormat() {
        String line = "43,fbfd,0.99,55595.74,629.46,45633.34,181.49,9151.45,8986.06,165.39,0,conventional,2015,Albany";
        Avocado actual = Main.createAvocado(line.split(","));

        assertNull(actual);
    }
    @Test
    public void testNotEnoughData() {
        String line = "43,9/6/2015,0.99,55595.74,629.46,45633.34,181.49,9151.45,165.39,0,conventional,2015";
        Avocado actual = Main.createAvocado(line.split(","));

        assertNull(actual);
    }

    @Test
    public void testCommaSeparators() throws ParseException {
        String line = "16,9/6/2015,1,11,99172,96,879,45,90062,62,240,79,7990,1,7762,87,227,23,0,conventional,2015,Albany";
        DateFormat format = new SimpleDateFormat("m/d/yyyy");
        Date date = format.parse("9/6/2015");
        Avocado expected = new Avocado(date.getTime(), 1.11, 99172.96, "conventional", "Albany");
        Avocado actual = Main.createAvocado(line.split(","));

        assertNotNull(actual);
        assertFalse(actual.equals(expected));
      
    }
}