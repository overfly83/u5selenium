package com.sap.ui5.selenium.sap.fpa.ui.control.list;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;


public class List extends CompoundControl{
	

	protected List(String id, Frame frame) {
		super(id, frame);
	}
	
	
	private List getThisList(){
		return this;
	}
	
	public void multiSelectRowByKey(String colname, String... values) {
		waitUntilListInitialized();
		int colindex = this.getColumnIndex(colname);
		java.util.List<WebElement> rows = getThisList().findRootWebElement().findElements(By.className("sapEPMUIListBodyRow"));
		
		for(int i=0;i<rows.size();i++){
			String cellvalue = rows.get(i).findElements(By.className("sapEPMUIListBodyCell")).get(colindex).findElement(By.className("sapEPMUIListBodyCellText")).getText().trim();
			for(int rowindex=0;rowindex<values.length;rowindex++) {
				if(cellvalue.equalsIgnoreCase(values[rowindex])){
					this.selectRow(i);
					break;
				}
			}
		}
	}
	
	public void selectRowByKey(String colname, String value) {
		waitUntilListInitialized();
		int colindex = this.getColumnIndex(colname);
		java.util.List<WebElement> rows = getThisList().findRootWebElement().findElements(By.className("sapEPMUIListBodyRow"));
		int rowindex = -1;

		for(int i=0;i<rows.size();i++){	
			String cellvalue = rows.get(i).findElements(By.className("sapEPMUIListBodyCell")).get(colindex).findElement(By.className("sapEPMUIListBodyCellText")).getText().trim();
			if(cellvalue.equalsIgnoreCase(value)){
				rowindex = i;
				break;
			}
		}
		if(rowindex>-1){
			this.selectRow(rowindex);	
		}else{
			Assert.fail("The expected value"+value + " is not found in the list");
		}
	}
	
	public void clickRowByKey(String column, String value) {
		waitUntilListInitialized();
		int colindex = this.getColumnIndex(column);
		java.util.List<WebElement> rows = getThisList().findRootWebElement().findElements(By.className("sapEPMUIListBodyRow"));
		int rowindex = -1;

		for(int i=0;i<rows.size();i++){
			String cellvalue = rows.get(i).findElements(By.className("sapEPMUIListBodyCell")).get(colindex).findElement(By.className("sapEPMUIListBodyCellText")).getText().trim();
			if(cellvalue.equalsIgnoreCase(value)){
				rowindex = i;
				break;
			}
		}
		if(rowindex>-1){
			this.clickRow(rowindex);
		}else{
			Assert.fail("The expected value"+value + " is not found in the list");
		}
	}


	/**
	 * check whether Item exists
	 */
	public boolean checkItemExists(String column,String value){
		waitUntilListInitialized();
		int colindex = this.getColumnIndex(column);
		java.util.List<WebElement> rows = getThisList().findRootWebElement().findElements(By.className("sapEPMUIListBodyRow"));

		for(int i=0;i<rows.size();i++){
			String cellvalue = rows.get(i).findElements(By.className("sapEPMUIListBodyCell")).get(colindex).findElement(By.className("sapEPMUIListBodyCellText")).getText().trim();
			if(cellvalue.equalsIgnoreCase(value)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * check only these items exist in the list
	 * @param values
	 * @return
	 */
	public boolean checkItemsExistOnly(String... values){
		waitUntilListInitialized();
		int length = values.length;
		int colindex = this.getColumnIndex("Name");
		java.util.List<WebElement> rows = getThisList().findRootWebElement().findElements(By.className("sapEPMUIListBodyRow"));
		if (values.length!=rows.size()) return false;
		for(int i=0;i<values.length;i++){
			for(int j=0;j<rows.size();j++){
				String cellvalue = rows.get(j).findElements(By.className("sapEPMUIListBodyCell")).get(colindex).findElement(By.className("sapEPMUIListBodyCellText")).getText().trim();
				if(cellvalue.equalsIgnoreCase(values[i])){
					length--;
					break;
				}
			}
		}
		if (length==0) return true; 
			else return false;
	}
	
	/**
	 * assert whether Item exists
	 */
	public void assertItemExists(String column,String value,Boolean expected){
		Boolean actual = checkItemExists(column,value);
		Assert.assertEquals("Assert whether Item: " + value + " Exists", expected, actual);
	}
			
	private void selectRow(int index) {
		java.util.List<WebElement> rows = getThisList().findRootWebElement().findElements(By.className("sapEPMUIListBodyRow"));
		String checkboxId = rows.get(index).findElement(By.className("sapEPMUIListBodyCellCheckbox")).findElement(By.className("sapEPMUIListCheckbox")).getAttribute("id");
		((com.sap.ui5.selenium.sap.m.CheckBox)this.getUI5Object(By.id(checkboxId))).check();
		
	}
	
	private void clickRow(int index) {
		waitUntilListInitialized();
		java.util.List<WebElement> rows = this.findRootWebElement().findElements(By.className("sapEPMUIListBodyRow"));
		rows.get(index).findElement(By.className("sapEPMUIListBodyCellMain")).click();
	}
	
	
	private int getColumnIndex(String colName) {
		try{
			java.util.List<WebElement> cols = this.findRootWebElement().findElements(By.className("sapEPMUIListColumn"));
			for(WebElement col:cols){
				if(col.getText().equalsIgnoreCase(colName)){
					return cols.indexOf(col);
				}
			}
		}catch(Exception e){	
		}
		return -1;
	}
	
	/**
	 * 
	 * Wait until the list initialization is finished.
	 */
	private void waitUntilListInitialized(){
		Wait<WebDriver> wait = new WebDriverWait(driver, 30).pollingEvery(300, TimeUnit.MILLISECONDS);
		wait.until(ExpectedConditions.refreshed(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					WebElement isBusy = getThisList().findRootWebElement().findElements(By.className("sapEPMUIListBusyIndicator")).get(0);
					return isBusy.getAttribute("style").contains("display: none");
				}catch(Exception e){	
					return false;
				}	
			}	
		}));
	}

	
	
}
