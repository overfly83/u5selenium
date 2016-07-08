package com.sap.ui5.selenium.sap.fpa.ui.control.shell;



import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;
import com.sap.ui5.selenium.sap.m.List;
import com.sap.ui5.selenium.sap.m.Popover;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.SapUi5Factory;


public class Shell extends CompoundControl {

	public Shell(String id, Frame frame) {
		super(id, frame);
	}
	public WebElement getShell(){
		return this.findRootWebElement();
	}
	public void selectShellOption(final String option) {
		
		new WebDriverWait(driver, 60).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					WebElement userClickable = getShell().findElements(By.className("sapEpmUiShellUser")).get(0);
					userClickable.click();
					Thread.sleep(2000);
					if(webdriver.findElement(By.className("user_preferrence_popover")).isDisplayed()){
						Popover menu = SapUi5Factory.findUI5ObjectByClass(windowManager.currentWindow(), "user_preferrence_popover");
						menu.select(option);
						return true;
					}else{
						return false;
					}
				}catch(Exception e){
					return false;
				}
			}
		});
		
		
	}
	
	
	public void toggleNavigationPane() {
		new WebDriverWait(driver, 60).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					WebElement navigationClickable = getShell().findElements(By.className("sapEpmUiShellNavigationButton")).get(0);
					navigationClickable.click();
					Thread.sleep(1000);
					if(webdriver.findElement(By.id("HomeNavigation")).isDisplayed()){
						return true;
					}else{
						return false;
					}
				}catch(Exception e){
					return false;
				}
			}
		});
	}

	public void toggleDiscussions(int...timeout) {
		int defaulttimeout = (timeout!=null && timeout.length==1)?timeout[0]:30;
		
		long start = System.currentTimeMillis();
		while (defaulttimeout * 1000 > System.currentTimeMillis() - start) {
			try{
				final WebElement discussionClickable = getShell().findElements(By.className("sapEpmUiShellCollaborationPaneToggle")).get(0);
				discussionClickable.click();
				new WebDriverWait(driver, 20).until(new ExpectedCondition<Boolean>(){
					public Boolean apply(WebDriver webdriver) {
						try{
							if(webdriver.findElement(By.className("sapEpmUiShellCollaborationPane")).getCssValue("margin-right").equals("0px")){
								return true;
							}else{
								return false;
							}
						}catch(Exception e){
							return false;
						}
					}
				});
				return;
			}catch(Exception e){
					
			}
		}
	}
	public void toggleTimePane() {
		long start = System.currentTimeMillis();
		while (30 * 1000 > System.currentTimeMillis() - start) {
			try{
				WebElement discussionClickable = getShell().findElements(By.className("sapEpmUiShellTimePaneToggle")).get(0);
				discussionClickable.click();
				new WebDriverWait(driver, 20).until(new ExpectedCondition<Boolean>(){
					public Boolean apply(WebDriver webdriver) {
						try{
							if(webdriver.findElement(By.className("sapEpmUiShellTimePane")).getCssValue("margin-left").equals("0px")){
								return true;
							}else{
								return false;
							}
						}catch(Exception e){
							return false;
						}
					}
				});
				return;
			}catch(Exception e){
					
			}
		}
	}
	
	public String getUserId() {
		return this.findRootWebElement().getAttribute("data-username");
	}
	public String getUserName() {
		return this.findRootWebElement().findElements(By.className("sapEpmUiShellUser")).get(0).getAttribute("title");
	}
	
	private void showNotification(){
		WebElement notifications = getShell().findElements(By.xpath(".//div[@title='"+Resource.getMessage("TITLE_NOTIFICATION")+"' and @class='sapEpmUiShellHeaderItem']")).get(0);
		notifications.click();
	}
	
	public void clickItemInNotification(final String text) {
		final BrowserWindow bw = this.windowManager.currentWindow();
		new WebDriverWait(driver, 20).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					showNotification();
					if(webdriver.findElement(By.className("sapEpmUiControlNotification")).isDisplayed()){
						Popover menu = SapUi5Factory.findUI5ObjectByClass(bw, "sapEpmUiControlNotification");
						menu.clickWebElement(By.xpath(".//div[text()='"+text+"']"));
						return true;
					}else{
						return false;
					}
				}catch(Exception e){
					return false;
				}
			}
		});
		
	}
	
	/**
	 * check whether ItemInNotification is displayed
	 * @param text
	 * @return
	 */
	public boolean checkItemInNotificationDisplayed(final String text) {
	
		long start = System.currentTimeMillis();
		while (30 * 1000 > System.currentTimeMillis() - start){
			try{
				showNotification();
				if(driver.findElement(By.className("sapEpmUiControlNotification")).isDisplayed()){
					Popover menu = SapUi5Factory.findUI5ObjectByClass(this.windowManager.currentWindow(), "sapEpmUiControlNotification");
					List list = menu.getUI5Object(By.id("Notifications--notifications_others_list"));
					return list.checkItemExists(text);
				}else{
					continue;
				}
			}catch(Exception e){
				continue;
			}
		}
		return false;
	}

	/**
	 * Assert whether ItemInNofication is displayed
	 * @param text
	 * @param expected
	 */
	public void assertItemInNotificationDisplayed(String text, boolean expected){
		
		long start = System.currentTimeMillis();
		while (30 * 1000 > System.currentTimeMillis() - start){
			try{
				showNotification();
				if(driver.findElement(By.className("sapEpmUiControlNotification")).isDisplayed()){
					Popover menu = SapUi5Factory.findUI5ObjectByClass(this.windowManager.currentWindow(), "sapEpmUiControlNotification");
					List list = menu.getUI5Object(By.id("Notifications--notifications_others_list"));
					list.assertItemExists(text,expected);
					return;
				}else{
					continue;
				}
			}catch(Exception e){
				continue;
			}
		}
	}
}
