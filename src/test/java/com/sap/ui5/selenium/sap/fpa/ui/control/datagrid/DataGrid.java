package com.sap.ui5.selenium.sap.fpa.ui.control.datagrid;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;
import com.sap.ui5.selenium.commons.TextField;
import com.sap.ui5.selenium.utilities.SapUi5Factory;

public class DataGrid extends CompoundControl {

	private int selectedColumnIndex = -1;
	protected DataGrid(String id, Frame frame) {
		super(id, frame);
		// TODO Auto-generated constructor stub
	}
	
	private List<WebElement> getColumnHeaderList(){
		return this.findRootWebElement().findElements(By.xpath(".//div[@class='sapEPMUIModellerGridGridHeader']/div"));
	}
	

	public WebElement SelectColumnByColumnIndex(int index) {
		this.selectedColumnIndex = index;
		List<WebElement> headerList = this.getColumnHeaderList();
		WebElement element = headerList.get(index);
		element.click();
		return element;
	}
	
	public void replaceSelectedColumnName(String newValue) {
		List<WebElement> headerList = this.getColumnHeaderList();
		WebElement element = headerList.get(selectedColumnIndex);
		String curId = element.findElements(By.xpath(".//input[@role='textbox']")).get(0).getAttribute("id");
		((TextField)SapUi5Factory.findUI5ObjectByIdEndsWith(this.windowManager.currentWindow(), curId)).setValue(newValue, true);
	}
	
	public String getSelectedColumnName(){
		List<WebElement> headerList = this.getColumnHeaderList();
		WebElement element = headerList.get(selectedColumnIndex);
		String curId = element.findElements(By.xpath(".//input[@role='textbox']")).get(0).getAttribute("id");
		return ((TextField)SapUi5Factory.findUI5ObjectByIdEndsWith(this.windowManager.currentWindow(), curId)).getValue();
	}
	
	
	/**
	 * 
	 * @param rowIndex, starting from 0, coulmn header is not the row
	 * @param columnIndex, starting from 0, line number is not the column
	 */
	public void selectCell(int rowIndex, int columnIndex){
		this.findRootWebElement().findElements(By.xpath("//td[@data-modellergrid-row='" + String.valueOf(rowIndex) 
				+"' and @data-modellergrid-column='" + String.valueOf(columnIndex) +"']")).get(0).click();
	}
	
}
