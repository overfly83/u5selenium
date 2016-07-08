package test.sap.fpa.dataintegration;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.commons.Button;
import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.sap.fpa.ui.control.commons.CheckBox;
import com.sap.ui5.selenium.sap.fpa.ui.control.commons.DropDown;
import com.sap.ui5.selenium.sap.fpa.ui.control.commons.Input;
import com.sap.ui5.selenium.sap.fpa.ui.control.datagrid.DataGrid;
import com.sap.ui5.selenium.sap.fpa.ui.control.memberselector.MemberSelector;
import com.sap.ui5.selenium.sap.fpa.ui.dm.controls.Histogram;
import com.sap.ui5.selenium.sap.fpa.ui.dm.controls.HistogramItem;
import com.sap.ui5.selenium.sap.m.Dialog;
import com.sap.ui5.selenium.utilities.SapUi5Factory;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;

public class DataIntegrationNewHelper {


	public static void mapColumnForNewModel(BrowserWindow bw, int index, String perspectiveName, String attributeType, String perspectiveType, String... timeFormat){
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
		DataGrid dataGrid = getWranglingDataGrid(bw);
		dataGrid.SelectColumnByColumnIndex(index);
		String columnName = dataGrid.getSelectedColumnName();
		getColumnView(bw);
		if(perspectiveType.equalsIgnoreCase("Time")){
			setColumnSettings(bw, columnName, perspectiveName, attributeType, perspectiveType, timeFormat);
		}else{
			setColumnSettings(bw, columnName, perspectiveName, attributeType, perspectiveType);
		}
		
		if(attributeType.equalsIgnoreCase("ID") || attributeType.equalsIgnoreCase("KeyFigure"))
			dataGrid = getWranglingDataGrid(bw);
			dataGrid.SelectColumnByColumnIndex(index);
			dataGrid.replaceSelectedColumnName(perspectiveName);			
	}
	

	
	public static DataGrid getWranglingDataGrid(BrowserWindow bw){
		return SapUi5Factory.findUI5ObjectByClass(bw, "sapEPMUIModellerGridModeller");
	}
	
	public static void getColumnView(BrowserWindow bw){
		WebElement button = SapUi5Factory.findWebElementByClass(bw, "em-previewTask-main-pane-tabMenu-right-btn");
		if(!button.isSelected()){
			button.click();
		}
	}
	
	
	public static void finishMapping(BrowserWindow bw){
		SapUi5Factory.findWebElementByClass(bw, "em-previewTask-main-pane-tabMenu-left-btn").click();
		Button finishMappingBtn = SapUi5Factory.findUI5ObjectByClass(bw, "finishMappingBtn");
		finishMappingBtn.click();
		
		Dialog confirmDlg = SapUi5Factory.findUI5ObjectByClass(bw, "sapMDialog");
		confirmDlg.ok();
	}
	
	
	/**
	 * Set the option 'Set First Row As Column Header' in column view. 
	 * @param bw, current browser instance
	 * @param index, the index of selected column
	 * @param isFirstRowHeader, toggle this option?
	 */
	public static void setFirstRowAsColumnHeadersBySelectColumn(BrowserWindow bw, int index, boolean isFirstRowHeader){
		DataGrid dataGrid = getWranglingDataGrid(bw);
		dataGrid.SelectColumnByColumnIndex(index);
		SapUi5Factory.findWebElementByClass(bw, "em-previewTask-main-pane-tabMenu-left-btn").click();
		CheckBox firstRowCB = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "columnHeaderCheckbox");
		if(firstRowCB.isChecked() != isFirstRowHeader){
			firstRowCB.check();
		}
	}
	
	/**
	 * Select one column, open the filter popup, select the Items
	 * @param bw, current browser instance
	 * @param index, the index of selected column
	 * @param IdDescription, filter type options, supported value: ID, DESCRIPTION AND ID and Description.
	 * @param member, filter items
	 * @param isUnselect, select or unselect?
	 */
	public static void filterItemBySelectColumn(BrowserWindow bw, int index, String IdDescription, String member, boolean isUnselect){
		DataGrid dataGrid = getWranglingDataGrid(bw);
		List<String> memberList = Arrays.asList(member.split(","));
		WebElement currentHeader = dataGrid.SelectColumnByColumnIndex(index);
		currentHeader.findElements(By.className("complexHeaderSourceDimToolBarBtn")).get(0).click();
		SapUi5Factory.findWebElementByClass(bw, "complexHeaderSourceToolPopup").findElement(By.className("complexHeaderSourceDimFilterToolBtn")).click();
		MemberSelector ms = (MemberSelector)SapUi5Factory.findUI5ObjectByClass(bw, "member-selector");
		
		if(isUnselect){
			for(String memberItem: memberList){
				ms.unSelect(IdDescription, memberItem, memberItem);
			}
		}else{
			ms.clearSelection();
			for(String memberItem: memberList){
				ms.singleSearch(IdDescription, memberItem, memberItem);
			}
		}
		ms.ok();
	}
	
	/**
	 * set column settings, it will be used for setting attribute and perspective.
	 * @param bw, current browser instance
	 * @param type, attribute type or perspective type.
	 */
	private static void selectValueInTypeList(BrowserWindow bw,String type){
		WebElement typeControl = SapUi5Factory.findWebElements(bw, By.id("csvTypeMappingDialogContents-type-main-page")).get(0);
		
		List<WebElement> typeList = typeControl.findElements(By.tagName("button"));
		Button typeBtn;
		for(WebElement element: typeList){
			//can't get text via webElement for those items out of displaying list so convert the webElmenet to button to get the text.
			typeBtn = (com.sap.ui5.selenium.commons.Button)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, element.getAttribute("id"));
			if(typeBtn.getText()!=null && typeBtn.getText().equalsIgnoreCase(type)){
				typeBtn.click();
				break;
			}
		}
	}
	
	/**
	 * set column settings in column view.
	 * @param bw, current browser instance
	 * @param columnName, selected column.
	 * @param perspectiveName, column perspectiveName.
	 * @param attributeType, column attributeType. Supported value:ID, Description, Property, Hierarchy, KeyFigure, Currency.
	 * @param perspectiveType, column perspectiveType. Supported value:Account, Generic, Time, Organization, SignedData.
	 * @param timeFormat, when perspective type is time, need set this value.
	 */
	public static void setColumnSettings(BrowserWindow bw, String columnName, String perspectiveName, String attributeType, String perspectiveType, String... timeFormat) {
		//wait until the panel is switched for columnName.
		SapUi5Factory.findWebElementByLocator(bw, By.xpath(".//div[contains(@id,'columnLabelTitleText')]/div[@title='"+columnName+"']"));
		//set attribute type
		SapUi5Factory.findWebElementByLocator(bw,By.xpath(".//div[contains(@id,'attributeTypeBtn')]/div[@class='textButtonValue']")).click();
		selectValueInTypeList(bw, attributeType);
		if(attributeType.equalsIgnoreCase("Hierarchy") || 
				attributeType.equalsIgnoreCase("Description")|| 
				attributeType.equalsIgnoreCase("Property") || 
				attributeType.equalsIgnoreCase("Currency")){
			//TODO: double check if following item works and refine following codes to see if we could improve them to handle with correct context.
			SapUi5Factory.findWebElements(bw, By.id("csvTypeMappingDialogContents-type-replace-page")).get(0);		
			DropDown cbb = SapUi5Factory.findUI5ObjectByLocator(bw, By.xpath(".//div[@role='combobox']"));
			cbb.setValue(perspectiveName);
			Button btn = SapUi5Factory.findUI5ObjectByLocator(bw, By.className("mappingPopupReplaceApplyBtn"));
			btn.click();
		}		
		//set perspective type
		if(attributeType.equalsIgnoreCase("ID")){
			SapUi5Factory.findWebElementByLocator(bw,By.xpath(".//div[contains(@id,'perspectiveTypeBtn')]/div[@class='textButtonValue']")).click();
			selectValueInTypeList(bw, perspectiveType);
		}

		//set time format
		if(timeFormat.length > 0 && timeFormat[0] !=null){
			SapUi5Factory.findWebElementByLocator(bw,By.xpath(".//div[contains(@id,'timeFormatBtn')]/div[@class='textButtonValue']")).click();
			selectValueInTypeList(bw, timeFormat[0]);
		}
	}
	
	/**
	 * It is used for finding mapping popup, and replacing the selected item.
	 * @param bw, current browser instance
	 * @param newValue, the name of selected column
	 * @param timeout, settings for finding popup and replacing the item.
	 */
	private static void replaceValueInPopup(BrowserWindow bw, String newValue, int...timeout) {
		final String replaceValue = newValue;
		int defaulttimeout = (timeout!=null && timeout.length==1)?timeout[0]:60;
		SapUi5Factory.findWebElementByClass(bw, "sapDataIntegrationMappingToolpopup");
		final BrowserWindow curruntWin = bw;
		Wait<WebDriver> wait = new WebDriverWait(bw.syncAjax().driver(), defaulttimeout);
		wait.until(new ExpectedCondition<Boolean>(){ 
		    public Boolean apply(WebDriver webDriver) {
		    	try{
		    		((Button)SapUi5Factory.findUI5ObjectByClass(curruntWin,"mappingPopupMainReplaceBtn")).click();
		    		((Input)SapUi5Factory.findUI5ObjectByClass(curruntWin,"sapEpmUiInput")).setValue(replaceValue);		    		
		    		((Button)SapUi5Factory.findUI5ObjectByClass(curruntWin, "mappingPopupReplaceApplyBtn")).click();
					return true;
				}catch(Exception e){
					return false;
				}
		    }
		});
		
	}
	
	/**
	 * In data grid, find and replace item according to specified columnIndex and rowIndex. Known issue: this function cannot support find the invisible element. Such as: 
	 * this is element will be displayed until you drag scroll bar.
	 * @param bw, current browser instance
	 * @param columnIndex, start from 0.
	 * @param rowIndex, start from 0.
	 * @param newValue, the replace content.
	 * @param timeout, set timeout for replace action.
	 */
	public static void replaceValueInDataGridBySelectColumn(BrowserWindow bw, int columnIndex, int rowIndex, String newValue, int...timeout) {
		DataGrid dataGrid = getWranglingDataGrid(bw);
		dataGrid.selectCell(rowIndex, columnIndex);
		replaceValueInPopup(bw, newValue, timeout);
	}
	
	
	/**
	 * In column view, find and replace the content.
	 * @param bw, current browser instance
	 * @param index, the index of selected column
	 * @param oldValue, the content of replaced item
	 * @param newValue, the replace content
	 * @param isSorted, sort the content in column view?
	 * @param timeout, set timeout for replace action
	 */
	public static void replaceElementInColumnView(BrowserWindow bw, int index,String oldValue,String newValue,Boolean isSorted, int...timeout) {
		DataGrid dataGrid = getWranglingDataGrid(bw);
		dataGrid.SelectColumnByColumnIndex(index);
		getColumnView(bw);
		if(isSorted){
			SapUi5Factory.findWebElementByClass(bw, "indicatorLeftTextLabel").click();
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Histogram histogram = (Histogram)SapUi5Factory.findUI5ObjectByClass(bw, "QualityIndicator");
		HistogramItem item = histogram.getHistogramItemByValue(oldValue);
		if(item!=null){
			item.click();
			replaceValueInPopup(bw, newValue,timeout);
		} else {
			throw new RuntimeException("Cannot find item: " + oldValue + " in column view");
		}
	}
	
}
;
