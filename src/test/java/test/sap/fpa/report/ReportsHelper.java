package test.sap.fpa.report;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.sap.fpa.ui.control.list.ContentlibFilterTabs;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarItem;
import com.sap.ui5.selenium.sap.fpa.ui.control.table.Table;
import com.sap.ui5.selenium.sap.m.Dialog;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.SapUi5Factory;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;


public class ReportsHelper {
	
	public static void gotoTab(BrowserWindow bw, String tab){
		ContentlibFilterTabs tb = SapUi5Factory.findUI5ObjectByClass(bw, "sapEpmUiTabContainer");
		tb.clickTab(tab);
	}
	
	public static void selectReport(BrowserWindow bws,String colname, String report) {
		Table table = SapUi5Factory.findUI5ObjectByClass(bws,"sapUiTable");
		table.selectRowByColumn(colname, report);
	}
	
	
	public static void selectReports(BrowserWindow bws,String colname, String... reports) {
		Table table = SapUi5Factory.findUI5ObjectByClass(bws,"sapUiTable");
		table.multiSelectByColumn(colname, reports);
		
	}
	
	/*public static void refreshReportList(BrowserWindow bws) {
		NewToolbar tb = NewToolbar.findToolbarByClass(bws, "sapEpmModelerToolbar");
		tb.select(4);
	}*/
	
	public static void openReport(BrowserWindow bws,String reportname) {
		Table table = SapUi5Factory.findUI5ObjectByClass(bws,"sapUiTable");
		table.clickCell(Resource.getMessage("CONTENTLIB_NAME"),reportname);
		SapUi5Utilities.waitWhenPageBusy(bws.driver());
	}
	
	public static void searchReport(BrowserWindow bws,String substring) {
		com.sap.ui5.selenium.sap.m.SearchField searchfield = SapUi5Factory.findUI5ObjectByClass(bws, "-searchField");
		searchfield.clearValue();
		searchfield.setValue(substring);
		searchfield.triggerSearch();
	}
	
	public static boolean checkReportExist(BrowserWindow bws,String reportname){
		Table table = SapUi5Factory.findUI5ObjectByClass(bws,"sapUiTable");
		return table.checkItemExist(Resource.getMessage("CONTENTLIB_NAME"), reportname);
	}
	
	public static void delete(BrowserWindow bws){
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "toolbar-deleteButton")).click();
		Dialog deleteDlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "delete-dialog");
		deleteDlg.ok();
	}
	

	public static void assertReportExist(BrowserWindow bws,String reportname,Boolean expected){
		Table table = SapUi5Factory.findUI5ObjectByClass(bws,"sapUiTable");
		table.assertItemExist(Resource.getMessage("CONTENTLIB_NAME"),reportname, expected);
	}
	




	
	
}
