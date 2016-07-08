package com.sap.ui5.selenium.sap.fpa.ui.control.tabContainer;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;

public class TabContainer extends CompoundControl {

	protected TabContainer(String id, Frame frame) {
		super(id, frame);
	}


	public void clickLeftToolbarButton() {
		waitTabsToInitialized();
		List<WebElement> tabs = this.findRootWebElement().findElements(By.xpath(".//*[@class='sapEpmUiTabContainerLeftToolbar']//div[contains(@class,'sapEpmModelerToolbarItem')]"));
		clickTab(tabs.get(0));
	}

	public void clickRightToolbarButton() {
		waitTabsToInitialized();
		List<WebElement> tabs = this.findRootWebElement().findElements(By.xpath(".//*[@class='sapEpmUiTabContainerRightToolbar']//div[contains(@class,'sapEpmModelerToolbarItem')]"));
		clickTab(tabs.get(0));
	}

	public void clickTab(String tabName) {
		waitTabsToInitialized();
		List<WebElement> tabs = this.findRootWebElement().findElements(By.className("sapEpmUiTabContainerTab"));
		for(final WebElement tb:tabs){
			if(tb.getText().trim().equals(tabName.trim())){
				clickTab(tb);
				break;
			}
		}
	}

	public boolean checkTabExist(String tabName) {
		waitTabsToInitialized();
		List<WebElement> tabs = this.findRootWebElement().findElements(By.className("sapEpmUiTabContainerTab"));
		for (WebElement tb : tabs) {
			if (tb.getText().trim().equals(tabName.trim())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Assert whether Tab exists
	 * @param tabName
	 * @param expected
	 */
	public void assertTabExist(String tabName, boolean expected){
		boolean actual = checkTabExist(tabName);
		Assert.assertEquals("Assert whether Tab:" + tabName + " exists", expected, actual);
	}


	private void waitTabsToInitialized() {
		final String id = this.getId();
		Wait<WebDriver> wait = new WebDriverWait(driver, 30).pollingEvery(100, TimeUnit.MILLISECONDS);
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver webdriver) {
				try {
					List<WebElement> tabs = webdriver.findElement(By.id(id)).findElements(By.className("sapEpmUiTabContainerTab"));
					for (WebElement it : tabs) {
						if (!it.getText().equalsIgnoreCase("")) {
							return true;
						}
					}
					
				} catch (Exception e) {
					return false;
				}
				return false;
			}
		});
	}
	
	private void clickTab(final WebElement tab){
		new WebDriverWait(driver, 30).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver input) {
				try{
					tab.click();
					return true;
				}catch(Exception e){
					return false;
				}
			}	
		});
	}

}
