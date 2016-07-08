package com.sap.ui5.selenium.sap.fpa.ui.control.gridlayouter;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;

public class GridLayouter extends CompoundControl {

	public GridLayouter(String id, Frame frame) {
		super(id, frame);
	}
	
	
	/**
	 * 
	 * @param perspective
	 * @param to the value could be Perspective, Column, Row
	 */
	public void dragDropPerspective(String perspective, String to){
		WebElement persp = this.findRootWebElement().findElements(By.xpath(".//div[text()='"+perspective+"']")).get(0);
		WebElement target = this.findRootWebElement().findElements(By.xpath(".//div[text()='"+to+"']")).get(0);
		
		Actions builder = new Actions(driver);
		Action dragAndDrop = builder.clickAndHold(persp).moveToElement(target,10,70).release().build();
		dragAndDrop.perform();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {}
	}
	
	/**
	 * 
	 * @param perspective should fully match the display value in the layouter
	 */
	public void clickFilterByPerspectiveName(final String perspective){
		final WebElement gl = this.findRootWebElement();
		new WebDriverWait(driver,30).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					List<WebElement> dimcontainers = gl.findElements(By.className("sapEPMUIGridLayouterDimension"));
					for(WebElement dim:dimcontainers){
						if(dim.getText().equalsIgnoreCase(perspective)){
							String dimId = dim.getAttribute("id");
							dim.findElement(By.xpath(".//div[@class='sapEPMUIGridLayouterExpandButtons']/span[1]")).click();
							gl.findElements(By.id(dimId)).get(0).findElements(By.xpath(".//span[contains(@class,'filterBttn')]")).get(0).click();
							break;
						}
					}
					new WebDriverWait(driver,5).until(ExpectedConditions.visibilityOfElementLocated(SapUi5Utilities.getEndsWithLocator("Member-Filter")));
					return true;
				}catch(Exception e){
					return false;
				}
			}
		});
		
		
	}
	/**
	 * 
	 * @param perspective should fully match the display value in the layouter
	 */
	public void clickAttributesByPerspectiveName(String perspective){
		List<WebElement> dimcontainers = this.findRootWebElement().findElements(By.className("sapEPMUIGridLayouterDimensionContainer"));
		for(WebElement dim:dimcontainers){
			if(dim.getText().equalsIgnoreCase(perspective)){
				dim.findElement(By.xpath(".//div[@class='sapEPMUIGridLayouterExpandButtons']/span[2]")).click();
				return;
			}
		}
		
	}
	
	/**
	 * check whether ClickFilterByPerspectiveName is enabled
	 * @param perspective
	 * @return
	 */
	public boolean checkClickFilterByPerspectiveNameEnabled(String perspective){
		List<WebElement> dimcontainers = this.findRootWebElement().findElements(By.className("sapEPMUIGridLayouterDimensionContainer"));
		for(WebElement dim:dimcontainers){
			if(dim.getText().equalsIgnoreCase(perspective)){
				return dim.findElement(By.xpath(".//div[@class='sapEPMUIGridLayouterExpandButtons']/span[1]")).isEnabled();
			}
		}
		return false;
	}
	
	/**
	 * Assert whether ClickFilterByPerspectiveName is enabled
	 * @param perspective
	 * @param expected
	 */	
	public void assertClickFilterByPerspectiveNameEnabled(String perspective, Boolean expected){
		List<WebElement> dimcontainers = this.findRootWebElement().findElements(By.className("sapEPMUIGridLayouterDimensionContainer"));
		Boolean actual = false;
		for(WebElement dim:dimcontainers){
			if(dim.getText().equalsIgnoreCase(perspective)){
				actual = dim.findElement(By.xpath(".//div[@class='sapEPMUIGridLayouterExpandButtons']/span[1]")).isEnabled();
				break;
			}
		}
		Assert.assertEquals("Assert ClickFilterByPerspectiveName:" + perspective + " is enabled", expected, actual);
	}
	
	/**
	 * check whether ClickAttributesByPerspectiveName is enabled
	 * @param perspective
	 * @return
	 */
	public boolean checkClickAttributesByPerspectiveNameEnabled(String perspective){
		List<WebElement> dimcontainers = this.findRootWebElement().findElements(By.className("sapEPMUIGridLayouterDimensionContainer"));
		for(WebElement dim:dimcontainers){
			if(dim.getText().equalsIgnoreCase(perspective)){
				return dim.findElement(By.xpath(".//div[@class='sapEPMUIGridLayouterExpandButtons']/span[2]")).isEnabled();
			}
		}
		return false;
	}
	

	/**
	 * Assert whether ClickAttributesByPerspectiveName is enabled
	 * @param perspective
	 * @param expected
	 */
	public void assertClickAttributesByPerspectiveNameEnabled(String perspective, Boolean expected){
		List<WebElement> dimcontainers = this.findRootWebElement().findElements(By.className("sapEPMUIGridLayouterDimensionContainer"));
		Boolean Actual = false;
		for(WebElement dim:dimcontainers){
			if(dim.getText().equalsIgnoreCase(perspective)){
				Actual = dim.findElement(By.xpath(".//div[@class='sapEPMUIGridLayouterExpandButtons']/span[2]")).isEnabled();
				break;
			}
		}
		Assert.assertEquals("Assert ClickAttributesByPerspectiveName:" + perspective + " is enabled", expected, Actual);
	}


}
