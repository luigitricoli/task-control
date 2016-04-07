package br.com.egs.task.control.web.model.task;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import br.com.egs.task.control.web.model.ForeseenType;
import br.com.egs.task.control.web.model.Task;

public class TaskBuilderTest {
	
	TaskBuilder tasker = new TaskBuilder();

	@Before
	public void init() {
		String type = "interna";
		String system = "task_control";
		String idType = "INT";
		String idValue = "1459964094437";
		String description = "test";
		ForeseenType foreseenType = ForeseenType.days;
		List<String> owners = new ArrayList<String>();
		owners.add("ltricoli");
		tasker.setSource(type).setForeseenType(foreseenType);
        tasker.setApplication(system).setDescription(description).setTaskType(idType.concat(idValue));
        tasker.addOwnersAsString(owners);
	}
	
	@Test
	public void oneDayMonday() throws InvalidTask {
		String start = "04/04/16";
		Integer foreseenQtd = 1;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
		
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("04/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("04/04/2016"));
	}
	
	@Test
	public void oneDayTuesday() throws InvalidTask {
		String start = "05/04/16";
		Integer foreseenQtd = 1;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
		
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("05/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("05/04/2016"));
	}
	
	@Test
	public void oneDayWednesday() throws InvalidTask {
		String start = "06/04/16";
		Integer foreseenQtd = 1;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
		
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("06/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("06/04/2016"));
	}
	
	@Test
	public void oneDayThursday() throws InvalidTask {
		String start = "07/04/16";
		Integer foreseenQtd = 1;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
		
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("07/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("07/04/2016"));
	}
	
	@Test
	public void oneDayFriday() throws InvalidTask {
		String start = "08/04/16";
		Integer foreseenQtd = 1;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
		
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("08/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("08/04/2016"));
	}
	
	@Test
	public void twoDaysMonday() throws InvalidTask {
		String start = "04/04/16";
		Integer foreseenQtd = 2;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
		
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("04/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("05/04/2016"));
	}
	
	@Test
	public void twoDaysTuesday() throws InvalidTask {
		String start = "05/04/16";
		Integer foreseenQtd = 2;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
		
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("05/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("06/04/2016"));
	}
	
	@Test
	public void twoDaysWednesday() throws InvalidTask {
		String start = "06/04/16";
		Integer foreseenQtd = 2;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
		
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("06/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("07/04/2016"));
	}
	
	@Test
	public void twoDaysThursday() throws InvalidTask {
		String start = "07/04/16";
		Integer foreseenQtd = 2;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("07/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("08/04/2016"));
	}
	
	@Test
	public void twoDaysFriday() throws InvalidTask {
		String start = "08/04/16";
		Integer foreseenQtd = 2;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("08/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("11/04/2016"));
	}
	
	@Test
	public void threeDaysMonday() throws InvalidTask {
		String start = "04/04/16";
		Integer foreseenQtd = 3;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("04/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("06/04/2016"));
	}
	
	@Test
	public void threeDaysTuesday() throws InvalidTask {
		String start = "05/04/16";
		Integer foreseenQtd = 3;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("05/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("07/04/2016"));
	}

	@Test
	public void threeDaysWednesday() throws InvalidTask {
		String start = "06/04/16";
		Integer foreseenQtd = 3;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("06/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("08/04/2016"));
	}
	
	@Test
	public void threeDaysThursday() throws InvalidTask {
		String start = "07/04/16";
		Integer foreseenQtd = 3;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("07/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("11/04/2016"));
	}
	
	@Test
	public void threeDaysFriday() throws InvalidTask {
		String start = "08/04/16";
		Integer foreseenQtd = 3;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("08/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("12/04/2016"));
	}
	
	@Test
	public void fourDaysMonday() throws InvalidTask {
		String start = "04/04/16";
		Integer foreseenQtd = 4;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("04/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("07/04/2016"));
	}
	
	@Test
	public void fourDaysTuesday() throws InvalidTask {
		String start = "05/04/16";
		Integer foreseenQtd = 4;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("05/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("08/04/2016"));
	}
	
	@Test
	public void fourDaysWednesday() throws InvalidTask {
		String start = "06/04/16";
		Integer foreseenQtd = 4;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("06/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("11/04/2016"));
	}
	
	@Test
	public void fourDaysThursday() throws InvalidTask {
		String start = "07/04/16";
		Integer foreseenQtd = 4;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("07/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("12/04/2016"));
	}
	
	@Test
	public void fourDaysFriday() throws InvalidTask {
		String start = "08/04/16";
		Integer foreseenQtd = 4;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("08/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("13/04/2016"));
	}
	
	@Test
	public void fiveDaysMonday() throws InvalidTask {
		String start = "04/04/16";
		Integer foreseenQtd = 5;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("04/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("08/04/2016"));
	}
	
	@Test
	public void threeWeeksMonday() throws InvalidTask {
		String start = "04/04/16";
		Integer foreseenQtd = 15;
		
		tasker.setStartDate(start).setForeseenQtd(foreseenQtd);
        Task task = tasker.build();
        assertThat(task.getStartDateAsString(), equalTo("04/04/2016"));
        assertThat(task.getForeseenEndDateAsString(), equalTo("22/04/2016"));
	}
	
}
