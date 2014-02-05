package br.com.egs.task.control.web.model.calendar;

import org.junit.Test;

import java.util.Calendar;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class CalendarStructureTest {

    @Test
    public void februaryJson() {
        MonthStructure month = new MonthStructure(1, 2013);

        String expected = "{\"days\":[\"27\",\"28\",\"29\",\"30\",\"31\",\"Fev 1\","
                + "\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\","
                + "\"9\",\"10\",\"11\",\"12\",\"13\",\"14\",\"15\","
                + "\"16\",\"17\",\"18\",\"19\",\"20\",\"21\",\"22\","
                + "\"23\",\"24\",\"25\",\"26\",\"27\",\"28\",\"Mar 1\","
                + "\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"]}";

        assertEquals(expected, month.getDaysAsJson());
    }

    @Test
    public void marchJson() {
        MonthStructure month = new MonthStructure(2, 2014);

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
        MonthStructure month = new MonthStructure(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));

        String expected = String.format(",\"Hoje %s\",", cal.get(Calendar.DAY_OF_MONTH));
        assertThat(month.getDaysAsJson(), containsString(expected));
    }

    @Test
    public void monthIds(){
        assertEquals(MonthStructure.Month.Jan.getId(), 0);
        assertEquals(MonthStructure.Month.Fev.getId(), 1);
        assertEquals(MonthStructure.Month.Mar.getId(), 2);
        assertEquals(MonthStructure.Month.Abr.getId(), 3);
        assertEquals(MonthStructure.Month.Mai.getId(), 4);
        assertEquals(MonthStructure.Month.Jun.getId(), 5);
        assertEquals(MonthStructure.Month.Jul.getId(), 6);
        assertEquals(MonthStructure.Month.Ago.getId(), 7);
        assertEquals(MonthStructure.Month.Set.getId(), 8);
        assertEquals(MonthStructure.Month.Out.getId(), 9);
        assertEquals(MonthStructure.Month.Nov.getId(), 10);
        assertEquals(MonthStructure.Month.Dez.getId(), 11);
    }

}
