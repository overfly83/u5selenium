package test.sap.fpa.connection;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarItem;
import com.sap.ui5.selenium.sap.fpa.ui.control.tabContainer.TabContainer;
import com.sap.ui5.selenium.sap.m.Dialog;
import com.sap.ui5.selenium.sap.ui.table.Table;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.SapUi5Factory;

public class ConnectionHelper {

	
	public static void gotoTab(BrowserWindow bw,String tabName){
		TabContainer tabbar = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "connections-tab-container");
		tabbar.clickTab(tabName);
	}
	
	public static boolean selectConnections(BrowserWindow bw, String...connections){
		Table connectionList = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-di-connection-list");
		return connectionList.multiSelectByColumn(Resource.getMessage("DI_CONNLIST_CONNECTION_NAME"), connections);
	}
	
	public static void deleteConnections(BrowserWindow bw, String...connections){
		NewToolbarItem delete = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-delete-connection");
		delete.click();
		Dialog deleteConfirm = SapUi5Factory.findUI5ObjectByClass(bw, "sapEpmUiDialog ");
		deleteConfirm.ok();
	}
	
}
