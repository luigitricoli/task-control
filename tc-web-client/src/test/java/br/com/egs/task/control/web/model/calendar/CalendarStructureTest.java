package br.com.egs.task.control.web.model.calendar;

import org.junit.Test;

import java.util.Calendar;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class CalendarStructureTest {

    @Test
    public void februaryJson() {
        ReferenceMonth month = new ReferenceMonth(2, 2013);

        String expected = "{\"label\":\"Fevereiro/2013\", \"days\":[\"27\",\"28\",\"29\",\"30\",\"31\",\"Fev 1\","
                + "\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\","
                + "\"9\",\"10\",\"11\",\"12\",\"13\",\"14\",\"15\","
                + "\"16\",\"17\",\"18\",\"19\",\"20\",\"21\",\"22\","
                + "\"23\",\"24\",\"25\",\"26\",\"27\",\"28\",\"Mar 1\","
                + "\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"]}";

        assertEquals(expected, month.getDaysAsJson());
    }

    @Test
    public void dezemberJson() {
        ReferenceMonth month = new ReferenceMonth(12, 2014);

        String expected = "{\"label\":\"Atual\", \"days\":[\"30\",\"Dez 1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\",\"10\",\"11\",\"12\",\"Hoje 13\",\"14\",\"15\",\"16\",\"17\",\"18\",\"19\",\"20\",\"21\",\"22\",\"23\",\"24\",\"25\",\"26\",\"27\",\"28\",\"29\",\"30\",\"31\",\"Jan 1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\",\"10\"]}";

        assertEquals(expected, month.getDaysAsJson());
    }

    @Test
    public void currentMonthJson() {
        Calendar cal = Calendar.getInstance();
        int monthId = cal.get(Calendar.MONTH)+1;
        ReferenceMonth month = new ReferenceMonth(monthId, cal.get(Calendar.YEAR));

        String expected = String.format(",\"Hoje %s\",", cal.get(Calendar.DAY_OF_MONTH));
        assertThat(month.getDaysAsJson(), containsString(expected));
    }

    @Test
    public void monthIds(){
        assertEquals(ReferenceMonth.Label.Jan.getId(), 0);
        assertEquals(ReferenceMonth.Label.Fev.getId(), 1);
        assertEquals(ReferenceMonth.Label.Mar.getId(), 2);
        assertEquals(ReferenceMonth.Label.Abr.getId(), 3);
        assertEquals(ReferenceMonth.Label.Mai.getId(), 4);
        assertEquals(ReferenceMonth.Label.Jun.getId(), 5);
        assertEquals(ReferenceMonth.Label.Jul.getId(), 6);
        assertEquals(ReferenceMonth.Label.Ago.getId(), 7);
        assertEquals(ReferenceMonth.Label.Set.getId(), 8);
        assertEquals(ReferenceMonth.Label.Out.getId(), 9);
        assertEquals(ReferenceMonth.Label.Nov.getId(), 10);
        assertEquals(ReferenceMonth.Label.Dez.getId(), 11);
    }

}
