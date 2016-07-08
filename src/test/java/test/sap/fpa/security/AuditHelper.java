package test.sap.fpa.security;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.commons.DatePicker;
import com.sap.ui5.selenium.commons.DropdownBox;
import com.sap.ui5.selenium.commons.Paginator;
import com.sap.ui5.selenium.commons.TextField;
import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarItem;
import com.sap.ui5.selenium.sap.fpa.ui.security.audit.FilterTree;
import com.sap.ui5.selenium.sap.fpa.ui.security.audit.FilterTreeNode;
import com.sap.ui5.selenium.sap.m.Dialog;
import com.sap.ui5.selenium.sap.m.Popover;
import com.sap.ui5.selenium.sap.m.SearchField;
import com.sap.ui5.selenium.sap.ui.table.Table;
import com.sap.ui5.selenium.utilities.SapUi5Factory;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;
public class AuditHelper {
	
	/**
	 * 
	 * @param defaultWindow
	 * @param activity e.g. Read
	 * @param packageName e.g. com.sap.fpa.ui
	 * @param objectType e.g. Perspective
	 * @param objectName e.g. Account_01
	 * @param username e.g. Martin Brody
	 * @param timeStamp e.g. 2015-05-05/2015-06-06
	 */
	public static void setActivityFilter(BrowserWindow defaultWindow,
			String activity,String packageName, String objectType, String objectName, String username, String transType, String timeStamp){
		
		String[] filters = new String[]{activity,packageName,objectType,objectName,transType,username,timeStamp};
		NewToolbarItem filter = SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "filter-btn");
		filter.click();
		
		Dialog filterDlg = SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "-setFilterDlg");
		FilterTree fTree = filterDlg.getUI5Object(By.className("sapUiTree"));
		List<FilterTreeNode> tnodes = fTree.getNodes();
		for(int i=1;i<tnodes.size();i++ ){
			if(filters[i-1]!=null){
				tnodes.get(i).select();
			}
			
		}
		if(activity!=null){
			DropdownBox actDropDown = filterDlg.getUI5ObjectByIdEndsWith("activeFilterActivityDropdown");
			actDropDown.setValue(activity, false);
		}
		
		if(packageName!=null){
			TextField packageNametf = filterDlg.getUI5ObjectByIdEndsWith("activeFilterPackageTextField");
			packageNametf.setValue(packageName, false);
		}	
		
		if(objectName!=null){
			TextField packageNametf = filterDlg.getUI5ObjectByIdEndsWith("activeFilterObjNameTextField");
			packageNametf.setValue(objectName, false);
		}
		
		if(objectType!=null){
			DropdownBox actTypeDropDown = filterDlg.getUI5ObjectByIdEndsWith("activeFilterObjectTypeDropdown");
			actTypeDropDown.setValue(objectType, false);
		}
		
		if(username!=null){
			TextField usernametf = filterDlg.getUI5ObjectByIdEndsWith("activeFilterUserNameTextField");
			usernametf.setValue(username, false);
		}
		
		if(transType!=null){
			DropdownBox transTypeDropDown = filterDlg.getUI5ObjectByIdEndsWith("activeFilterTransTypeDropdown");
			transTypeDropDown.setValue(objectType, false);
		}
		
		if(timeStamp!=null){
			String[] tStamp = timeStamp.split("/");
			DatePicker dateFrom = filterDlg.getUI5ObjectByIdEndsWith("picker0");
			dateFrom.setValue(tStamp[0], false);
			if(tStamp.length>1){
				DatePicker dateTo = filterDlg.getUI5ObjectByIdEndsWith("picker1");
				dateTo.setValue(tStamp[1], false);
			}
		}
		
		filterDlg.ok();
	}
	
	/**
	 * check several items in columns at one time
	 * @param bw
	 * @param column 
	 * @param item
	 * @return
	 */
	public static boolean checkItemsExist(BrowserWindow bw, String[] column, String[] item){
		Table tb = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-table");
		return tb.checkItemsExist_MultiMatch(column, item);

	}
	
	public static void assertItemsExist(BrowserWindow bw, String[] column, String item[], Boolean expected){
		Table tb = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-table");
		tb.assertMultiItemsExist(column, item, expected);
	}  
	
	public static void setPage(BrowserWindow bw, int page) {
		Paginator pageinator = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "auditReport-table-paginator");
		pageinator.navigateTo(page);
		((Table)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "auditReport-table")).waitUntilTableInitialized();
	}
	
	public static void refresh(BrowserWindow bw) {
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "refresh-btn")).click();
		((Table)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "auditReport-table")).waitUntilTableInitialized();
	}
	
	public static void selectModel(final BrowserWindow bw, final String model) {
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
		new WebDriverWait(bw.driver(), 30).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver input) {
				try{
					SapUi5Factory.findWebElementByIdEndsWith(bw, "tab-container").findElement(By.className("sapEpmUiTabContainerTabIcon")).click();
					Popover pop = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-selectModel-popover");
					SearchField sf = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-selectModel-searchField");
					sf.clearValue();
					sf.setValue(model);
					sf.triggerSearch();
					List<WebElement> items = bw.findWebElement(pop.getId()).findElements(By.tagName("label"));
					for (WebElement it : items) {
						if (it.getText().equalsIgnoreCase(model)) {
							it.click();
							break;
						}
					}
					return true;
				}catch(Exception e){
					return false;
				}
			}
			 
		 });
		
	}
	
}
