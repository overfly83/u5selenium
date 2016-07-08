package com.sap.ui5.selenium.sap.fpa.ui.control.modeller.datagrid;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.core.Control;
import com.sap.ui5.selenium.core.Frame;

public class DataGrid  extends Control{

	protected DataGrid(String id, Frame frame) {
		super(id, frame);
	}
	
	
	public void selectFirstCell() {
		By firstcell = By.xpath("//td[@data-modellergrid-row='0' and @data-modellergrid-column='0']");
		this.findRootWebElement().findElements(firstcell).get(0).click();
	}
	
	/**
	 * Be careful to wait the paste finished
	 * @param data
	 */
	public void pasteDataIntoGrid(String data) {
		StringSelection clipData = new StringSelection(data);
		Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipBoard.setContents(clipData, null);
	    Actions actions = new Actions(driver);
	    actions.sendKeys(Keys.CONTROL + "v");
		actions.keyUp(Keys.CONTROL);
		actions.perform();
		actions.release();
		
	}
	
	@SuppressWarnings("unused")
	private void selectCell(String colName, int rowIndex) {
		//TODO
	}
	@SuppressWarnings("unused")
	private static void waitUntilGridLoaded(BrowserWindow bw) {
		final By colClassName = By.className("sapEPMUIModellerGridGridColumn");
//		bws.findWebElement(colClassName).getText();
		Wait<WebDriver> wait = new WebDriverWait(bw.driver(), 30).pollingEvery(1, TimeUnit.SECONDS)
				.ignoring(StaleElementReferenceException.class);
		wait.until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					WebElement col = webdriver.findElement(colClassName);
					String colText = col.getText();
					return (colText.equals("ID"));
				
				}catch(Exception e){
					try{
						Thread.sleep(1000);
						WebElement col = webdriver.findElement(colClassName);
						String colText = col.getText();
						return (colText.equals("ID"));
						
					}catch(Exception ex){
						
					}
					return false;
				}
			}
				
		});
	}
	

}
