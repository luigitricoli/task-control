package br.com.egs.task.control.web.model.calendar;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.util.Calendar;

import org.junit.Test;

public class CalendarStructureTest {

    @Test
    public void februaryJson() {
        MonthStructure month = new MonthStructure(Month.Fev, 2013);

        String expected = "{\"days\":[\"27\",\"28\",\"29\",\"30\",\"31\",\"Fev 1\","
                + "\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\","
                + "\"9\",\"10\",\"11\",\"12\",\"13\",\"14\",\"15\","
                + "\"16\",\"17\",\"18\",\"19\",\"20\",\"21\",\"22\","
                + "\"23\",\"24\",\"25\",\"26\",\"27\",\"28\",\"Mar 1\","
                + "\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"]}";

        assertEquals(expected, month.getDaysAsJson());
    }

    //@Test
    public void marchJson() {
        MonthStructure month = new MonthStructure(Month.Mar, 2014);

        String expected = "{\"days\":[\"23\",\"24\",\"25\",\"26\",\"27\",\"28\",\"Mar 1\","
                + "\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\","
                + "\"9\",\"10\",\"11\",\"12\",\"13\",\"14\",\"15\","
                + "\"16\",\"17\",\"18\",\"19\",\"20\",\"21\",\"22\"," +
                "\"23\",\"24\",\"25\",\"26\",\"27\",\"28\",\"29\","
                + "\"30\",\"31\",\"Abr 1\",\"2\",\"3\",\"4\",\"5\"]}";

        assertEquals(expected, month.getDaysAsJson());
    }

    @Test
    public void currentMonthJson() {
        Calendar cal = Calendar.getInstance();
        int monthId = cal.get(Calendar.MONTH);
        MonthStructure month = new MonthStructure(Month.getById(monthId), cal.get(Calendar.YEAR));

        String expected = String.format(",\"Hoje %s\",", cal.get(Calendar.DAY_OF_MONTH));
        assertThat(month.getDaysAsJson(), containsString(expected));
    }

    @Test
    public void monthIds(){
        assertEquals(Month.Jan.getId(), 0);
        assertEquals(Month.Fev.getId(), 1);
        assertEquals(Month.Mar.getId(), 2);
        assertEquals(Month.Abr.getId(), 3);
        assertEquals(Month.Mai.getId(), 4);
        assertEquals(Month.Jun.getId(), 5);
        assertEquals(Month.Jul.getId(), 6);
        assertEquals(Month.Ago.getId(), 7);
        assertEquals(Month.Set.getId(), 8);
        assertEquals(Month.Out.getId(), 9);
        assertEquals(Month.Nov.getId(), 10);
        assertEquals(Month.Dez.getId(), 11);
    }

}
