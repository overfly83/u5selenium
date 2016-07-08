package com.sap.ui5.selenium.sap.m;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;
public class Dialog extends CompoundControl {


	protected Dialog(String id, Frame frame) {
		super(id, frame);
	}

	
	public Dialog getThisDialog(){
		return this;
	}
	
	
	public void ok(){
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		// changed timeout period to 20 seconds -> Change was required for CreateModel Action's dialog prompt
		new WebDriverWait(driver, 20).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					if(getThisDialog().isDisplay()){
						getWebElement(By.xpath(".//footer//button[1]"),1).click();
						return false;
					}else{
						return true;
					}
				}catch(Exception e){
					return true;
				}
			}
		});
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	public void cancel(){
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		new WebDriverWait(driver, 3).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					if(getThisDialog().isDisplay()){
						getWebElement(By.xpath(".//footer//button[2]"),1).click();
						return false;
					}else{
						return true;
					}
				}catch(Exception e){
					return true;
				}
			}
		});
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	public String getTitle(){
		//check the title in sapMDialogTitle, in m.Dialog case
		List<WebElement> title = this.findRootWebElement().findElements(By.xpath(".//*[contains(@class,'sapMDialogTitle')]"));
		if(title.size() != 0 && !title.get(0).getText().isEmpty()){
			return title.get(0).getText();
		}else{   //in common dialog case
			title = this.findRootWebElement().findElements(By.xpath(".//*[@class ='sapEpmUiFormTitle']"));
			if(title.size() != 0 && !title.get(0).getText().isEmpty()){
				return title.get(0).getText();
			}else{
				return "";
			}
		}
	}
	
	public boolean isDisplay(){
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		boolean isDisplayed = this.getThisDialog().findRootWebElement().isDisplayed();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return isDisplayed;
	}
	
	/** 
	 * check if dialog display status is as expected
	 * @param expected dialog display status, true indicates visible, false indicates invisible
	 */
	public boolean checkDisplay(boolean expected) {
		return this.isDisplay() == expected;
	}
	
	/** 
	 * assert if dialog display status is as expected
	 * @param expected dialog display status, true indicates visible, false indicates invisible
	 */
	public void assertDisplay(boolean expected) {
		Assert.assertEquals("assert display status of dialog '" +this.getId()+ "'", expected, this.isDisplay());
	}
	
	/** 
	 * check if dialog OK button enable status is as expected
	 * @param expected OK button enable status, true indicates enable, false indicates disable
	 */
	public boolean checkOkButtonEnable(boolean expected) {
		Button btnOk = getUI5Object(By.xpath(".//footer//button[1]"));
		return btnOk.checkEnableStatus(expected);
	}
	
	/** 
	 * assert if dialog OK button enable status is as expected
	 * @param expected OK button enable status, true indicates enable, false indicates disable
	 */
	public void assertOkButtonEnable(boolean expected) {
		Button btnOk = getUI5Object(By.xpath(".//footer//button[1]"));
		btnOk.assertEnableStatus(expected);
	}
	
	/** 
	 * check if dialog Cancel button enable status is as expected
	 * @param expected Cancel button enable status, true indicates enable, false indicates disable
	 */
	public boolean checkCancelButtonEnable(boolean expected) {
		Button btnCancel = getUI5Object(By.xpath(".//footer//button[2]"));
		return btnCancel.checkEnableStatus(expected);
	}
	
	/** 
	 * Assert if dialog Cancel button enable status is as expected
	 * @param expected Cancel button enable status, true indicates enable, false indicates disable
	 */
	public void assertCancelButtonEnable(boolean expected) {
		Button btnCancel = getUI5Object(By.xpath(".//footer//button[2]"));
		btnCancel.assertEnableStatus(expected);
	}
	
}
