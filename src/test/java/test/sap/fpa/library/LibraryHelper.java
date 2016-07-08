package test.sap.fpa.library;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.sap.fpa.ui.control.commons.Input;
import com.sap.ui5.selenium.sap.fpa.ui.control.memberselector.MemberSelector;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarItem;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarMenu;
import com.sap.ui5.selenium.sap.fpa.ui.control.tabContainer.TabContainer;
import com.sap.ui5.selenium.sap.m.CheckBox;
import com.sap.ui5.selenium.sap.m.Dialog;
import com.sap.ui5.selenium.sap.m.MultiInput;
import com.sap.ui5.selenium.sap.m.SearchField;
import com.sap.ui5.selenium.sap.ui.fileuploader.FileUploader;
import com.sap.ui5.selenium.sap.ui.table.Table;
import com.sap.ui5.selenium.sap.ui.tree.Tree;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.SapUi5Factory;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;


public class LibraryHelper {
	
	private final static String LIBRARYNAME = Resource.getMessage("CONTENTLIB_NAME");
	
	private static void waitInitialized(BrowserWindow bw){
		final String contentlibId = SapUi5Factory.findWebElementByIdEndsWith(bw,"content-lib").getAttribute("id");
		new WebDriverWait(bw.driver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='"+contentlibId+"']//div[@class='sapUiLocalBusyIndicator']")));
		
	}
	/**
	 * @param path Public/aaa/bbb
	 */
	public static void gotoFolder(BrowserWindow bw,String path){
		String[] folders = path.split("/");
		if(folders.length<1) return;
		waitInitialized(bw);
		TabContainer container = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "content-lib-content-tab");
    	container.clickTab(folders[0]);
    	SapUi5Utilities.waitWhenPageBusy(bw.driver());

    	Table table = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "content-lib-content-grid");
    	
    	for(int i=1;i<folders.length;i++ ){
    		table.clickCell(LIBRARYNAME, folders[i]);
    		SapUi5Utilities.waitWhenPageBusy(bw.driver());
    	}
	}
	
	public static void assertDetail(BrowserWindow bw, String objname,String desc, String type){
		selectObjects(bw,objname);
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "content-lib-detail-btn")).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String actualobjname = SapUi5Factory.findWebElementByLocator(bw, By.className("contentlib-detail-summary")).getText();
		String actualdesc =  SapUi5Factory.findWebElementByLocator(bw, By.className("contentlib-detail-description")).getAttribute("value");
		String actualtype = SapUi5Factory.findWebElementByLocator(bw, By.className("contentlib-detail-text")).getText();
		Assert.assertEquals("Verifying the expected type:" +objname +" && Autial type is: "+actualobjname , objname, actualobjname);
		Assert.assertEquals("Verifying the expected description:" +desc +" && Autial description is: "+actualdesc , objname, actualobjname);
		Assert.assertTrue("Verifying the expected type:" +type +" && Autial type is: "+actualtype , type.matches(actualtype.trim()));
	}
	/**
	 * This function will create folder base on current path, use with gotoFolder(String path) normally
	 * @param folderName "abc", "my folder"
	 */
	public static void createFolder(BrowserWindow bw,String folderName){
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "create-folder-btn")).click();
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw,"create-folder-dialog");
		com.sap.ui5.selenium.sap.fpa.ui.control.commons.Input foldername = dlg.getUI5Object(By.xpath(".//div[contains(@id,'content-lib-create-folder-input')]"));
		foldername.setValue(folderName);
		dlg.ok();
		//SapUi5Utilities.waitWhenPageBusy(bw.driver());
	}
	/**
	 * This function will delete folder/file base on current path, use with gotoFolder(String path) normally,
	 * but you have to make sure they are in the same page
	 * @param folderName
	 */
	public static boolean deleteObjects(BrowserWindow bw,String... objectsList){
		
		if(!selectObjects(bw,objectsList)){
			return false;
		}
		
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "delete-btn")).click();	
		Dialog dlg = SapUi5Factory.findUI5ObjectByClass(bw, "modeller-deleteModeldialog", 30);
		SapUi5Utilities.waitUntilMessageDisappear(bw.driver());
		dlg.ok();
		return true;
	}
	/**
	 * 
	 * @param bw
	 * @param objectname
	 * @return
	 */
	public static boolean checkItemExist(BrowserWindow bw,String objectname){
		Table table = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "content-lib-content-grid");
		return table.checkItemExist(LIBRARYNAME, objectname);
	}
	
	/**
	 * This function will check whether file exist in current path, use with gotoFolder(String path) normally
	 * @param fileName
	 */
	public static void assertObjectExistInList(BrowserWindow bw,String objectname, Boolean expected){

		Table table = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "content-lib-content-grid");
		Boolean actual = table.checkItemExist(LIBRARYNAME, objectname);
		Assert.assertEquals("Checking whether file or folder:"+objectname +" exist in list", expected, actual);

	}
	
	/**
	 * This function will select folder base on current path, use with gotoFolder(String path) normally
	 * @param folderName
	 */
	public static boolean selectObjects(BrowserWindow bw,String... objectNameList){
		
		Table table = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "content-lib-content-grid");
		table.waitUntilTableInitialized();
		if(table.multiSelectByColumn(LIBRARYNAME, objectNameList)){
			return true;
		}
		return false;
	
	}
	/**
	 * 
	 * @param bw
	 * @param oldName
	 * @param newName
	 * @throws Exception 
	 */
	public static void renameObject(BrowserWindow bw,String oldName , String newName) throws Exception{
		selectObjects(bw,oldName);
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "manage-btn")).click();
		NewToolbarMenu toolbarMenu = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "manage-toolbar-menu");
		if(toolbarMenu.checkItemEnabled(Resource.getMessage("CONTENTLIB_RENAME"))) {
			toolbarMenu.select(Resource.getMessage("CONTENTLIB_RENAME"));
			Dialog renameDlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw,"rename-dialog");
			((Input)renameDlg.getUI5ObjectByIdEndsWith("rename-input")).setValue(newName, false);
			renameDlg.ok();
			SapUi5Utilities.waitWhenPageBusy(bw.driver());
		}else {
			throw new Exception("Can not rename Object");
		}
	}
	
	///////////////////////////////////////////////for report  ///////////////////////////////////////////////////////
	/**
	 * open a object in the current list
	 * @param reportName
	 */
	public static void openObject(BrowserWindow bw,String reportName)
	{
		Table table = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "content-lib-content-grid");
		table.waitUntilTableInitialized();
		table.clickCell(LIBRARYNAME, reportName);
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * example : uploadFile("C:/Users/i058399/Desktop/manual_test/RoleAboutUser.docx");
	 * @param localPath: the path where you store the file
	 */
	public static void uploadFile(BrowserWindow bw,String localPath){
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "upload-file-btn")).click();
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "content-lib-upload-file-dialog");

		FileUploader fu = FileUploader.findFileUploaderByIdEndsWith(bw,"upload-file-uploader");
		fu.assertEnabledStatus(true);
		fu.upload(localPath);
		SapUi5Utilities.waitUntilMessageDisappear(bw.driver());
		dlg.ok();
		
	}
	
	/**
	 * Add target object to favorite
	 * @param objectName
	 */
	public static void addToFavorite(BrowserWindow bw,String objname){
		selectObjects(bw,objname);
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "manage-btn")).click();
		NewToolbarMenu menu = SapUi5Factory.findUI5ObjectByIdEndsWith(bw,"manage-toolbar-menu");
		if(menu.checkItemVisible(Resource.getMessage("CONTENTLIB_ADD_TO_FAVORITES"))){
			menu.select(Resource.getMessage("CONTENTLIB_ADD_TO_FAVORITES"));
			SapUi5Utilities.waitWhenPageBusy(bw.driver());
		}else{
			return;
		}
	}
	
	/**
	 * Search the content from the library
	 * @param searchinfo
	 */
	public static void search(BrowserWindow bw,String searchinfo){
		
		SearchField searchField = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "search-field");
		searchField.clearValue();
		searchField.setValue(searchinfo);
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
		searchField.triggerSearch();
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
	}
	public static void clearSearch(BrowserWindow bw){
		
		SearchField searchField = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "search-field");
		searchField.clearValue();
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
		searchField.triggerSearch();
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
	}
	/**
	 * 
	 * @param bw
	 * @param objname
	 * @param path
	 */
	public static void copyCurrentObjectTo(BrowserWindow bw,String objname, String path){
		selectObjects(bw,objname);
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "content-lib-copy-btn")).click();
		Dialog movedlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw,"copy-move-dialog");
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
		Tree tree = Tree.findTreeByIdEndsWith(bw, "content-lib-tree");
		tree.gotoNode(path);
		movedlg.ok();
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
	}
	/**
	 * 
	 * @param bw
	 * @param objname
	 * @param path
	 * @throws Exception 
	 */
	public static void moveCurrentObjectTo(BrowserWindow bw,String objname, String path) throws Exception{
		selectObjects(bw,objname);
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "manage-btn")).click();
		NewToolbarMenu toolbarMenu = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "manage-toolbar-menu");
		if(toolbarMenu.checkItemEnabled(Resource.getMessage("CONTENTLIB_MOVE_TO"))) {
			toolbarMenu.select(Resource.getMessage("CONTENTLIB_MOVE_TO"));
			Dialog movedlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw,"copy-move-dialog");
			SapUi5Utilities.waitWhenPageBusy(bw.driver());
			Tree tree = Tree.findTreeByIdEndsWith(bw, "content-lib-tree");
			tree.gotoNode(path);

			movedlg.ok();
			SapUi5Utilities.waitWhenPageBusy(bw.driver());
		}else {
			throw new Exception("Can not move Object item 'Move To' is not enabled");
		}
	}
	/**
	 * 
	 * @param bw
	 * @param searchItem
	 * @param expected
	 */
	public static void assertSearchResult(BrowserWindow bw,String searchItem,Boolean expected){
		Table table = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "content-lib-content-grid");
		table.assertItemExist(LIBRARYNAME, searchItem, expected);
		
	}
	
	/**
	 * For parameters users and permission, they should match the size so as to align the permission with each user, 
	 * for the permission, there are 6 in all, or less or more.
	 * @param bw
	 * @param objname
	 * @param users:  structure: ["usera","userb","userc"]
	 * @param permissions:  structure: 4 or 6 permissions
	 * e.g: ["true/false/true/false/true/false","true/false/true/false/true/false","true/false/true/false/true/false"]
	 */
	public static void assignPermissionsToUser(BrowserWindow bw, String objname, String[] users,String[] permissions){
		selectObjects(bw,objname);
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "manage-btn")).click();
		((NewToolbarMenu)SapUi5Factory.findUI5ObjectByIdEndsWith( bw,"manage-toolbar-menu")).select(Resource.getMessage("CONTENTLIB_ASSIGN_PERMISSIONS"));
		
		Dialog assignDlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "assign-dialog");
		((com.sap.ui5.selenium.sap.m.Button)assignDlg.getUI5ObjectByIdEndsWith("assign-user-select-btn")).click();
		MemberSelector mbrSelector = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "assign-user-selector");
		mbrSelector.multiSearch(Resource.getMessage("DISPLAY_NAME"), users);
		mbrSelector.ok();
		
		Table assigntable = assignDlg.getUI5ObjectByIdEndsWith("assign-user-table");
		
		for(int i=0;i<users.length; i++){
			String[] userPermission = permissions[i].split("/");
			assignPermissionsToUser(assigntable, users[i], userPermission);
		}
		SapUi5Utilities.waitUntilMessageDisappear(bw.driver());
		assignDlg.ok();
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
	}
	/**
	 * 
	 * @param bw
	 * @param user
	 * @param fullaccess
	 * @param read
	 * @param update
	 * @param delete
	 * @param newdoc
	 * @param newfolder
	 */
	private static void assignPermissionsToUser(Table assigntable,String user,String... userPermission){
		final String READ = Resource.getMessage("CONTENTLIB_READ_ACCESS");
		final String FULL = Resource.getMessage("CONTENTLIB_FULL_ACCESS");
		final String UPDATE = Resource.getMessage("CONTENTLIB_UPDATE_ACCESS");
		final String DELETE = Resource.getMessage("CONTENTLIB_DELETE_ACCESS");
		final String COLUMNNAME = Resource.getMessage("CONTENTLIB_USER");
		final String NEWDOCS = Resource.getMessage("CONTENTLIB_NEW_DOCS");
		final String NEWFOLDERS = Resource.getMessage("CONTENTLIB_NEW_FOLDERS");
		if(Boolean.parseBoolean(userPermission[0])){
			assigntable.tickCell(FULL, COLUMNNAME, user);
		}else{
			if(Boolean.parseBoolean(userPermission[1])){
				assigntable.tickCell(READ, COLUMNNAME, user);
			}else{
				assigntable.unTickCell(READ, COLUMNNAME, user);
			}
			if(Boolean.parseBoolean(userPermission[2])){
				assigntable.tickCell(UPDATE, COLUMNNAME, user);
			}else{
				assigntable.unTickCell(UPDATE, COLUMNNAME, user);
			}
			if(Boolean.parseBoolean(userPermission[3])){
				assigntable.tickCell(DELETE, COLUMNNAME, user);
			}else{
				assigntable.unTickCell(DELETE, COLUMNNAME, user);
			}
			
			if(userPermission.length>4){
				if(Boolean.parseBoolean(userPermission[4])){
					assigntable.tickCell(NEWDOCS, COLUMNNAME, user);
				}else{
					assigntable.unTickCell(NEWDOCS, COLUMNNAME, user);
				}
				if(Boolean.parseBoolean(userPermission[5])){
					assigntable.tickCell(NEWFOLDERS, COLUMNNAME, user);
				}else{
					assigntable.unTickCell(NEWFOLDERS, COLUMNNAME, user);
				}
			}
			
			
		}
		
		
	}
	/**
	 * 
	 * @param bw
	 * @param objname
	 * @param all
	 * @param toWhom
	 */
	public static void shareObjectToUsers(BrowserWindow bw,String objname,boolean all,String... toWhom){
		selectObjects(bw,objname);
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "manage-btn")).click();
		((NewToolbarMenu)SapUi5Factory.findUI5ObjectByIdEndsWith( bw,"manage-toolbar-menu")).select(Resource.getMessage("CONTENTLIB_SHARE"));
		

		if(all){
			((CheckBox)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "share-all-checkbox")).check();
		}
		if(toWhom.length>0){
			
			((MultiInput)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-multi-input")).clearValue();
			SapUi5Factory.findWebElementByIdEndsWith(bw, "share-multi-input__vhi").click();
			
			MemberSelector selector = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "share-user-selector");
			selector.multiSearch(Resource.getMessage("MEMBER_SELECTOR_ID"), toWhom);
			selector.ok();
		}
		((Dialog)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "share-dialog")).ok();
		
	}
	
	
	public static void unShareObject(BrowserWindow bw,String objname){
		selectObjects(bw,objname);
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "manage-btn")).click();
		((NewToolbarMenu)SapUi5Factory.findUI5ObjectByIdEndsWith( bw,"manage-toolbar-menu")).select(Resource.getMessage("CONTENTLIB_SHARE"));

		((CheckBox)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "share-all-checkbox")).uncheck();
		
		((MultiInput)SapUi5Factory.findUI5ObjectByClass(bw, "-multi-input")).clearValue();
		((Dialog)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "share-dialog")).ok();
	}
	
	public static void refresh(BrowserWindow bw){
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "refresh-btn")).click();
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
	}
	
	public static void assertReadPermission(BrowserWindow bw, String objName, boolean expected){
		assertObjectExistInList(bw, objName, expected);
	}
	
	public static void assertCopyPermission(BrowserWindow bw, boolean expected){
		NewToolbarItem toolBar = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "content-lib-copy-btn");
		toolBar.assertButtonEnabledStatus(expected);
	}
	
	public static void assertUpdatePermission(BrowserWindow bw, boolean expected){
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "manage-btn")).click();
		NewToolbarMenu toolbarMenu = SapUi5Factory.findUI5ObjectByIdEndsWith( bw,"manage-toolbar-menu");
		toolbarMenu.assertItemEnabled(Resource.getMessage("CONTENTLIB_RENAME"), expected);
	}
	
	public static void assertDeletePermission(BrowserWindow bw, boolean expected){
		NewToolbarItem toolBar = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "delete-btn");
		toolBar.assertButtonEnabledStatus(expected);
	}
	
	public static void assertSharePermission(BrowserWindow bw, boolean expected){
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "manage-btn")).click();
		NewToolbarMenu toolbarMenu = SapUi5Factory.findUI5ObjectByIdEndsWith( bw,"manage-toolbar-menu");
		toolbarMenu.assertItemVisible(Resource.getMessage("CONTENTLIB_SHARE"), expected);
	}

	
}
