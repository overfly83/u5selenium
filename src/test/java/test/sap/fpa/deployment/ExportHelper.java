package test.sap.fpa.deployment;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.commons.RadioButton;
import com.sap.ui5.selenium.commons.TriStateCheckBox;
import com.sap.ui5.selenium.commons.TriStateCheckBoxState;
import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.sap.fpa.ui.control.commons.Input;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarItem;
import com.sap.ui5.selenium.sap.m.Dialog;
import com.sap.ui5.selenium.sap.ui.table.Table;
import com.sap.ui5.selenium.sap.ui.table.TreeTable;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.SapUi5Factory;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;

public class ExportHelper {
	
	private final static String EXPORTNAME = Resource.getMessage("NAME");
	/** 
	 * @param ExportNameList
	 * 				names of the exports wanted to be selected
	 */
	public static void selectExports(BrowserWindow bw, String... ExportNameList) {
		Table table = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-history-table");
		for(int i=0;i<ExportNameList.length;i++){
			table.selectRowByColumn(EXPORTNAME, ExportNameList[i]);
		}
	}
	
	/** 
	 * @param ExportNameList
	 * 				names of the exports wanted to be deleted
	 */
	public static void deleteExports(BrowserWindow bw, String... ExportNameList) {
		selectExports(bw, ExportNameList);
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "erase-btn")).click();
		((Dialog)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "confirm-delete-history-dialog")).ok();		
	}
	
	/** 
	 * @param ExportName
	 * 				name of the export wanted to be opened
	 */
	public static void openExports(BrowserWindow bw, String ExportName) {
		Table table = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-history-table");
		table.clickCell(EXPORTNAME, ExportName);
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
	}
	
	/** 
	 * @param ExportName
	 *				name of the the exported history
	 * @param LocalOrTransport
	 * 				export content to local or transport,the value should be "local" or "transport"
	 * @param isOverWrite
	 * 				if the ExportName exits,isOverWrite should be set true, default is false
	 */
	public static void exportCurrentContent(BrowserWindow bw, String ExportName, String LocalOrTransport, Boolean... isOverWrite) {
		String radioBtnId = "";
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "export-btn")).click();
		Dialog exportDlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "export-dialog");
		if(LocalOrTransport.equalsIgnoreCase("local")) {
			radioBtnId = "export-download-radio";
		}
		if(LocalOrTransport.equalsIgnoreCase("transport")) {
			radioBtnId = "export-transport-radio";
		}
		((RadioButton)exportDlg.getUI5ObjectByIdEndsWith(radioBtnId)).select();
		Input nameIpt = exportDlg.getUI5ObjectByIdEndsWith("export-name-input");
		nameIpt.clearValue(true);
		nameIpt.setValue(ExportName, false);
		exportDlg.ok();
		if(isOverWrite.length==1 && isOverWrite[0]) {
			((Dialog)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "confirm-overwrite-dialog")).ok();
		}
		waitUntilExportDone(bw);
	}
	
	/** 
	 * select content in the current export
	 * @param contents
	 * 				the content you want select
	 * 				eg:{"Roles/Viewer,Modeler","Files/public/file1,file2,file3"}
	 */
	public static void selectExportContent(BrowserWindow bw, String[] contents) {
		TreeTable tree = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-table");
		tree.selectRows(EXPORTNAME, contents);
		waitUntilSelectedContentLoaded(bw);
		
	}
	
	/** 
	 * @param name
	 * 				name of the the exported history
	 * @param contents
	 * 				the content you want select
	 * 				eg:{"Roles/Viewer,Modeler","Files/public/file1,file2,file3"}
	 * @param LocalOrTransport
	 * 				export content to local or transport,the value should be "local" or "transport"
	 * @param isOverWrite
	 * 				if the ExportName exits,isOverWrite should be set true, default is false
	 */
	public static void createExport(BrowserWindow bw, String name, String[] contents, String LocalOrTransport, Boolean... isOverWrite) {
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "new-btn")).click();
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
		selectExportContent(bw, contents);
		exportCurrentContent(bw, name, LocalOrTransport, isOverWrite);
	}
	
	
	public static void selectAllContent(BrowserWindow bw) {
		WebElement we = SapUi5Factory.findWebElementByIdEndsWith(bw, "select-all-checkbox");
		TriStateCheckBox chb = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "select-all-checkbox");
		if(chb.getSelectionState().equals(TriStateCheckBoxState.Mixed) || chb.getSelectionState().equals(TriStateCheckBoxState.Unchecked)) {
			we.click();
			waitUntilSelectedContentLoaded(bw);
		}
	}
	
	public static void waitUntilExportDone(BrowserWindow bw) {
		final NewToolbarItem cancelBtn = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "cancel-btn");
		Wait<WebDriver> wait = new WebDriverWait(bw.driver(), 180);
		wait.until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				return !cancelBtn.isEnabled();
			}
		});
	}
	
	public static void waitUntilSelectedContentLoaded(BrowserWindow bw) {
		Table tabelContent = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "selection-table");
		tabelContent.waitUntilTableInitialized();
	}
	
	public static void refresh(BrowserWindow bw) {
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "refresh-btn")).click();
		((Table) SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-history-table")).waitUntilTableInitialized();
	}
}