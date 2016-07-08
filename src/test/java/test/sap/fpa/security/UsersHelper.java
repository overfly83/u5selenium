package test.sap.fpa.security;

import java.security.InvalidParameterException;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.commons.Button;
import com.sap.ui5.selenium.commons.ComboBox;
import com.sap.ui5.selenium.commons.Label;
import com.sap.ui5.selenium.commons.Link;
import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.sap.fpa.ui.control.commons.Input;
import com.sap.ui5.selenium.sap.fpa.ui.control.memberselector.MemberSelector;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarItem;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarMenu;
import com.sap.ui5.selenium.sap.m.Dialog;
import com.sap.ui5.selenium.sap.m.SearchField;
import com.sap.ui5.selenium.sap.ui.fileuploader.FileUploader;
import com.sap.ui5.selenium.sap.ui.table.Table;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.SapUi5Factory;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;


public class UsersHelper {

	private final static String tableid = "content-grid-content-table";
	private final static String USERID = Resource.getMessage("USER_ID");
	
	/**
	 * 
	 * @param userid
	 * @param firstname
	 * @param lastname
	 * @param displayname
	 * @param email
	 * @param manager
	 * @param roles
	 * @param defaultPassword
	 */
	public static void addUser(BrowserWindow bw, String userid, String firstname, String lastname, String displayname,
			String email, String manager, String roles, String defaultPassword) {

		Table tb = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, tableid);
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "content-grid-add-btn")).click();
		tb.inputDataLine(USERID, "", userid, firstname, lastname, displayname, email, manager, roles);
		// save
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "save-btn")).click();
		fillPassword(bw, defaultPassword);
		
	}

	/**
	 * 
	 * @param defaultPassword
	 * @param users
	 */
	public static void addUsers(BrowserWindow bw, String defaultPassword, String[]... users) {
		Table tb = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, tableid);
		
		for (int i = 0; i < users.length; i++) {
			tb.inputDataLine(USERID, "", users[i]);
		}
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-save-btn")).click();
		fillPassword(bw, defaultPassword);
	}

	/**
	 * delete a user or users by user id
	 * 
	 * @param bw
	 * @param tb
	 * @param users
	 * @return
	 */
	public static boolean deleteUsers(BrowserWindow bw, String... users) {
		try {
			Table tb = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, tableid);
			if (tb.multiSelectByColumn(USERID, users)) {
				((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-remove-btn")).click();
				((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-save-btn")).click();
				SapUi5Utilities.waitWhenPageBusy(bw.driver());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	public static boolean checkIfUserExists(BrowserWindow bw, String user) {
		Table tb = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, tableid);
		return tb.checkItemExist(USERID, user);

	}

	/**
	 * refresh table
	 * 
	 * @param bw
	 * @param tb
	 */
	public static void refresh(BrowserWindow bw) {
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-refresh-btn")).click();
		((Table) SapUi5Factory.findUI5ObjectByIdEndsWith(bw, tableid)).waitUntilTableInitialized();
	}

	/**
	 * set user-defined password
	 * 
	 * @param definedPassword
	 * @param users
	 */
	public static void setPassword(BrowserWindow bw, String definedPassword, String... users) {
		try {
			Table tb = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, tableid);
			tb.multiSelectByColumn(USERID, users);
			((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-set-password-btn")).click();
			fillPassword(bw, definedPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * search user by userid
	 * 
	 * @param user
	 */
	public static void searchUser(BrowserWindow bw, String user) {
		SearchField searchfield = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-search-field");
		searchfield.setValue(user);
		searchfield.triggerSearch();
	}

	/**
	 * update user's whole line info
	 * 
	 * @param userid
	 * @param firstname
	 * @param lastname
	 * @param displayname
	 * @param email
	 * @param manager
	 * @param roles
	 */
	public static void updateUser(BrowserWindow bw, String userid, String firstname, String lastname,
			String displayname, String email, String manager, String roles) {
		try {
			Table tb = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, tableid);
			tb.inputDataLine(USERID, userid, firstname, lastname, displayname, email, manager, roles);
			((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-save-btn")).click();
		} catch (Exception e) {
		}
	}

	/**
	 * update one info of user
	 * 
	 * @param user
	 *            : userid
	 * @param colname
	 *            : the name of column which need to update
	 * @param value
	 *            : the new value want to enter
	 */
	public static void updateUserProperty(BrowserWindow bw, String userid, String colname, String value) {
		try {
			Table tb = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, tableid);
			tb.inputCell(USERID, userid, colname, value);
			((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-add-btn")).click();
			((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-save-btn")).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void closeProfile(BrowserWindow bw) {
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-userPreferenceDlg");
		dlg.cancel();
	}
	
	public static void editProfileImage(BrowserWindow bw, String imagePath) {
		String initialImage="", afterImage="";
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-userPreferenceDlg");
		com.sap.ui5.selenium.sap.m.Button btnEdit = dlg.getUI5ObjectByIdEndsWith("-userPreferenceDlgActionBtn");
		if(btnEdit.checkText(Resource.getMessage("EDIT"))) {
			btnEdit.clickUntilTextChange(Resource.getMessage("SAVE"));
		}
		initialImage = dlg.getWebElementByIdEndsWith("-edit-avatar").getAttribute("src");
		FileUploader fu = FileUploader.findFileUploaderByIdEndsWith(bw, "-avatar-uploader");
		fu.upload(imagePath);
		btnEdit.clickUntilTextChange(Resource.getMessage("EDIT"));  
		afterImage = dlg.getWebElementByIdEndsWith("-read-avatar").getAttribute("src");
		Assert.assertNotSame("image url of profile" , initialImage, afterImage);
	}
	
	/**
	 * edit user profile
	 * 
	 * @param items
	 *            : list of profile content, [name, jobTitle, mobile, phone, email, functionArea, officeLocation]
	 */
	public static void editProfile(BrowserWindow bw, String[] items) {
		
		String[] inputSufix = new String[]{"editName", "editJobTitle", "editMobile", "editWorkPhone", "editEmail", "editFuncArea", "editOfficeLocation"};
		if(items.length != inputSufix.length) {
			throw new InvalidParameterException("items length does not match");
		}
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-userPreferenceDlg");
		com.sap.ui5.selenium.sap.m.Button btnEdit = dlg.getUI5ObjectByIdEndsWith("-userPreferenceDlgActionBtn");
		if(btnEdit.checkText(Resource.getMessage("EDIT"))) {
			btnEdit.clickUntilTextChange(Resource.getMessage("SAVE"));
		}
		for (int i=0; i<items.length; i++) {
			if( items[i]!=null && items[i]!="") {
				((Input)dlg.getUI5ObjectByIdEndsWith(inputSufix[i])).setValue(items[i]);
			}
		}
		btnEdit.clickUntilTextChange(Resource.getMessage("EDIT"));
	}
	
	/** 
	 * @param items
	 *            : list of profile content, [name, jobTitle, mobile, phone, email, functionArea, officeLocation]
	 */
	public static void checkProfile(BrowserWindow bw, String[] items) {
		String[] inputSufix = new String[]{"readName", "readJobTitle", "readMobile", "readWorkPhone", "readEmail", "readFuncArea", "readOfficeLocation"};
		if(items.length != inputSufix.length) {
			throw new InvalidParameterException("items length does not match");
		}
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-userPreferenceDlg");
		com.sap.ui5.selenium.sap.m.Button btnEdit = dlg.getUI5ObjectByIdEndsWith("-userPreferenceDlgActionBtn");
		if(btnEdit.checkText(Resource.getMessage("SAVE"))) {	
			btnEdit.clickUntilTextChange(Resource.getMessage("EDIT"));
		}
		for (int i=0; i<items.length; i++) {
			if(items[i]!=null && items[i]!="") {
				String actualValue="";
				if (!inputSufix[i].equalsIgnoreCase("readEmail")) {
					actualValue = ((Label)dlg.getUI5ObjectByIdEndsWith(inputSufix[i])).getText();
				}
				else {
					actualValue = ((Link)dlg.getUI5ObjectByIdEndsWith(inputSufix[i])).getText();
				}
				Assert.assertEquals(inputSufix[i].substring(4)+" in profile ",items[i], actualValue);
			}
		}
	}
	/**
	 * import .csv file into table example : importUsers(defaultWindow,
	 * "C:\\Users\\xxx\\Downloads\\user.csv")
	 * 
	 * @param localPath
	 */
	public static void importUsers(BrowserWindow bw, String localPath, String defaultPassword) {
		
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "import-btn")).click();
		((NewToolbarMenu)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "import-toolbar-menu")).select("Import Users from File");

		FileUploader fu = FileUploader.findFileUploaderByIdEndsWith(bw, "-uploader");
		fu.upload(localPath);
		Dialog importDlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "user-import-dialog");
		
		Button createmap = importDlg.getUI5ObjectByIdEndsWith("-map-btn");
		new WebDriverWait(bw.driver(), 30).until(ExpectedConditions.elementToBeClickable(By.id(createmap.getId())));
		createmap.click();
		Dialog mapDlg = SapUi5Factory.findUI5ObjectByClass(bw, "sapEpmUiFormDialog");
		((ComboBox)mapDlg.getUI5ObjectByIdEndsWith("-select-header")).setValue("Yes");
		((ComboBox)mapDlg.getUI5ObjectByIdEndsWith("-select-lineSeparator")).setValue("\\n");
		((ComboBox)mapDlg.getUI5ObjectByIdEndsWith("-select-delimiter")).setValue(" , ");
		((ComboBox)mapDlg.getUI5ObjectByIdEndsWith("-select-textQualifier")).setValue(" \" ");
		mapDlg.ok();
		
		importDlg.ok();
		
		fillPassword(bw,defaultPassword);
	
	}
	
	/**
	 * import active directory users
	 * @param defaultpassword   "Initial1"
	 * @param userid  "FPATEST"
	 */
	public static void importADUsers(BrowserWindow bw, String defaultPassword, String[] userid) {
		
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "import-btn")).click();
		((NewToolbarMenu)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "import-toolbar-menu")).select(Resource.getMessage("IMPORT_USERS_FROM_AD")); 
		
		MemberSelector ms = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-ad-selector");
		ms.multiSearch("ID", userid);
		ms.ok();
		
		fillPassword(bw, defaultPassword);
	}
	
	private static void fillPassword(BrowserWindow bw, String definedPassword) {
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "user-set-password-dialog");
		((com.sap.ui5.selenium.sap.fpa.ui.control.commons.Input)dlg.getUI5ObjectByIdEndsWith("-new-password-field")).setValue(definedPassword);
		((com.sap.ui5.selenium.sap.fpa.ui.control.commons.Input)dlg.getUI5ObjectByIdEndsWith("-confirm-password-field")).setValue(definedPassword);
		SapUi5Utilities.waitUntilMessageDisappear(bw.driver());
		dlg.ok();
	}
	
}
