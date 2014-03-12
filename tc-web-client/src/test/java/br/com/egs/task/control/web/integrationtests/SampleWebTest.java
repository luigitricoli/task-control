package br.com.egs.task.control.web.integrationtests;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.junit.Assert.assertEquals;

public class SampleWebTest {

	@Test
	public void testSamplePageWithHtmlUnit() {
		HtmlUnitDriver htmlUnit = new HtmlUnitDriver(true);
		performSampleTests(htmlUnit);
	}
	
	@Test
	public void testSamplePageWithFirefox() {
		FirefoxDriver firefox = new FirefoxDriver();
		performSampleTests(firefox);
		firefox.quit();
	}
	
//	@Test
//	public void testSamplePageWithIe() {
//		System.setProperty("webdriver.ie.driver", "C:\\Program Files\\Internet Explorer\\iexplore.exe");
//		InternetExplorerDriver ie = new InternetExplorerDriver();
//		performSampleTests(ie);
//		ie.quit();
//	}
	
	private void performSampleTests(WebDriver driver) {
		driver.get("http://localhost:8091/web-client/sample-page.html");
		
		WebElement button = driver.findElement(By.id("mybutton"));
		WebElement outputLabel = driver.findElement(By.id("outputtext"));
		
		button.click();
		assertEquals("Clicked!", outputLabel.getText());
	}
}
