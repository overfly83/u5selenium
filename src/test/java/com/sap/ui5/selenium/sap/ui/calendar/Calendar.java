package com.sap.ui5.selenium.sap.ui.calendar;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;

public class Calendar extends CompoundControl{
	static WebElement cal;
	
	protected Calendar(String id, Frame frame) {
		super(id, frame);
	}
	
	
	public static Calendar findCalendarByIdEndsWith(BrowserWindow bw, String id,int...timeout) {
		int defaulttimeout = (timeout!=null && timeout.length==1)?timeout[0]:30;
		long start = System.currentTimeMillis();
		while (defaulttimeout * 1000 > System.currentTimeMillis() - start) {	
	    	try{
	    		List<WebElement> elementList = bw.syncAjax().driver().findElements(SapUi5Utilities.getEndsWithLocator(id));
	    		for(WebElement el:elementList){
	    			if(el.isDisplayed()){
	    				return new Calendar(el.getAttribute("id"),bw.getMainFrame());
	    			}
	    		}
	    	}catch(Exception e){
	    	}
		} 		
		throw new TimeoutException("Could not find element within timeout " + defaulttimeout + " seconds: " + SapUi5Utilities.getEndsWithLocator(id).toString());
	}
	/**
	 * 
	 * @param date: format: yyyy/mm/dd
	 */
	public void setDate(String date){
		String[] dates = date.split("/");
		String year = String.valueOf(Integer.parseInt(dates[0]));
		int month = Integer.parseInt(dates[1]);
		String day = String.valueOf(Integer.parseInt(dates[2]));
		//click the picker to popup the calendar
		cal.findElement(By.className("sapUiTfDateIcon")).click();
		By calocter = By.className("sapUiCal");
		Wait<WebDriver> wait = new WebDriverWait(driver, 10).pollingEvery(1, TimeUnit.SECONDS)
				.ignoring(StaleElementReferenceException.class);
		wait.until(ExpectedConditions.visibilityOfElementLocated(calocter));
		
		WebElement calend = driver.findElement(calocter);
		//start to select the year
		calend.findElement(By.className("sapUiCalYearPick")).click();
		wait = new WebDriverWait(driver, 10).pollingEvery(1, TimeUnit.SECONDS).ignoring(StaleElementReferenceException.class);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("sapUiCalYears")));
		List<WebElement> years = calend.findElements(By.className("sapUiCalYear"));
		for(int i=0;i<years.size();i++){
			if(years.get(i).getText().equalsIgnoreCase(year)){
				years.get(i).click();
				break;
			}
		}
		
		calend.findElement(By.className("sapUiCalMonthPick")).click();
		wait = new WebDriverWait(driver, 10).pollingEvery(1, TimeUnit.SECONDS).ignoring(StaleElementReferenceException.class);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("sapUiCalMonths")));
		List<WebElement> months = calend.findElements(By.className("sapUiCalMonth"));
		months.get(month-1).click();
		
		List<WebElement> days = calend.findElements(By.className("sapUiCalDay"));
		
		for(int i=0;i<days.size();i++){
			if(days.get(i).getAttribute("class").contains("sapUiCalDayOtherMonth")){
				continue;
			}else{
				if(days.get(i).getText().equalsIgnoreCase(day)){
					days.get(i).click();
					break;
				}
			}
				
		}
	}
	
	
	public boolean checkCalendarYear(String year) {
		return this.js.exec(".getYear()").get(long.class).longValue() == Long.valueOf(year)? true : false;
	}
	
	public void assertCalendarYear(String year) {
		Assert.assertTrue("assert year of calendar '"+ this.getId() +"' to be " + year, checkCalendarYear(year));
	}
	
	public boolean checkCalendarMonth(String month) {
		return this.js.exec(".getMonth()").get(long.class).longValue() == Long.valueOf(month)? true : false;
	}
	
	public void assertCalendarMonth(String month) {
		Assert.assertTrue("assert month of calendar '"+ this.getId() +"' to be " + month, checkCalendarMonth(month));
	}
	
	public boolean checkCalendarDay(String day) {
		return this.js.exec(".getDay()").get(long.class).longValue() == Long.valueOf(day)? true : false;
	}
	
	public void assertCalendarDay(String day) {
		Assert.assertTrue("assert day of calendar '"+ this.getId() +"' to be " + day, checkCalendarDay(day));
	}
	
	public boolean checkCalendarDate(String date) {
		String[] dates = date.split("/");
		
		if(checkCalendarYear(dates[0]) && checkCalendarMonth(dates[1]) && checkCalendarDay(dates[2])) {
			return true;
		}else {
			return false;
		}
	}
	
	public void assertCalendarDate(String date) {
		Assert.assertTrue("assert day of calendar '"+ this.getId() +"' to be " + date, checkCalendarDate(date));
	}
	
	
	
	
}



