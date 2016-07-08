package test.sap.fpa.deployment;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarItem;
import com.sap.ui5.selenium.sap.m.Dialog;
import com.sap.ui5.selenium.sap.ui.fileuploader.FileUploader;
import com.sap.ui5.selenium.sap.ui.table.Table;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.SapUi5Factory;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;

public class ImportHelper{
	private final static String EXPORTNAME = Resource.getMessage("NAME");
	
	/**
	 * example:C:\fpatest\src\test\resources\Library\IMPORTSCENARIOTEST.tgz
	 * @param bw
	 * @param localPath the path where you store the file
	 */
	public static void uploadFile(BrowserWindow bw, String localPath){
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "upload-btn")).click();
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "upload-dialog");
		FileUploader fu = FileUploader.findFileUploaderByIdEndsWith(bw,"upload-du-uploader");
		fu.assertEnabledStatus(true);
		fu.upload(localPath);
		dlg.ok();
		Dialog dialog = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "upload-confirm-dialog");
		dialog.ok();
		Dialog importdlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "import-dialog");
		SapUi5Utilities.waitUntilMessageDisappear(bw.driver());
		importdlg.ok();
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "import-btn")).click();
		Dialog DG = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "option-dialog");
		DG.ok();
		waitUntilImportDone(bw);
	}
	
	/**
	 * import a new object
	 * @param bw
	 * @param index
	 * @param name
	 */
	public static void newImport(BrowserWindow bw, String index, String name){
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "new-btn")).click();
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "new-import-dialog");
		dlg.ok();
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "import-btn")).click();
		waitUntilImportDone(bw);
	}
	
	
	public static void refresh(BrowserWindow bw){
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "refresh-btn")).click();
		((Table) SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-history-table")).waitUntilTableInitialized();
	}
	
	/**
	 * open a object in the current list
	 * @param name object name
	 */
	public static void openObject(BrowserWindow bw, String name){
		Table tb = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-history-table");
		tb.clickCell(EXPORTNAME, name);
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
	}
	
	public static void selectObjects(BrowserWindow bw, String... objectNameList){
		
		Table tb = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-history-table");
		for(int i=0;i<objectNameList.length;i++){
			tb.selectRowByColumn(EXPORTNAME, objectNameList[i]);
		}
	
	}
	
	public static void waitUntilImportDone(BrowserWindow bw) {
		final NewToolbarItem cancelBtn = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "cancel-btn");
		Wait<WebDriver> wait = new WebDriverWait(bw.driver(), 360);
		wait.until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				return !cancelBtn.isEnabled();
			}
		});
	}

	public static void deleteObjects(BrowserWindow bw, String... objectsList){
		selectObjects(bw,objectsList);
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "erase-btn")).click();	
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw,"delete-history-dialog");		
		dlg.ok();
		SapUi5Utilities.waitUntilMessageDisappear(bw.driver());
	}
	
	public static boolean checkImportButtonEnabled(BrowserWindow bw){
		NewToolbarItem bt = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "import-btn");
		return bt.isEnabled();
	}
	
	public static boolean checkItemExists(BrowserWindow bw, String name){
		Table tb = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-history-table");
		return tb.checkItemExist(EXPORTNAME, name);
	}
	
	/**
	 * assert whether item exists in the table
	 * @param bw
	 * @param name
	 * @param expected
	 */
	public static void assertItemExists(BrowserWindow bw, String name, boolean expected){
		Assert.assertEquals("Assert whether: " + name + " exists", expected, checkItemExists(bw,name));
	}
	
	public static boolean checksubItemExists(BrowserWindow bw, String name){
		Table tb = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-table");
		return tb.checkItemExist(EXPORTNAME, name);
	}
	
	/**
	 * assert whether subitem exists in the item
	 * @param bw
	 * @param name
	 * @param expected
	 */
	public static void assertsubItemExists(BrowserWindow bw, String name, boolean expected){
		Assert.assertEquals("Assert whether: " + name + " exists", expected, checksubItemExists(bw,name));
	}	
}