package test.sap.fpa.workflow;

import java.io.File;

import org.junit.Test;

import test.sap.fpa.library.LibraryHelper;
import test.sap.fpa.modeling.ModelingHelper;
import test.sap.fpa.security.UsersHelper;

import com.sap.ui5.selenium.sap.fpa.ui.control.shell.ShellInfoMessage.INFO_TYPE;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;
import com.sap.ui5.selenium.utilities.ScenarioTest;

public class LibrarySecurityCheckST extends ScenarioTest {

	static final String tenant1Admin = "ADMIN";
	static final String tenantPassword = "Abcd1234";
	static final String tenantIniPwd = "Initial1";
	static final String tenant1 = "TA1";
	static final String loginJoyce = "JOYCE";
	static final String loginMaria = "MARIA";
	static final String loginJerry = "JERRY";

	static final String fileName ="financialreport.pdf";
	static final String fileNameNew ="financialreport1.pdf";
	static final String folderName ="TomFolder";
	static final String reportName = "aReportTest";

	private String resourcesPath = "";
	
	@Test
	public void libararySecurityScenarioTest() throws Exception {
		

		login(tenant1Admin, tenantPassword,tenant1);
		navigateTo(Resource.getMessage("MENU_USERS"), Resource.getMessage("MENU_USERS_SET_UP_USERS"));
		if(UsersHelper.deleteUsers(browserWindow, loginJoyce,loginMaria,loginJerry)){
			getFPAMessageArea().waitForMessage(Resource.getMessage("YOUR_CHANGES_SAVED"), INFO_TYPE.SUCCESS);
		}
		UsersHelper.addUsers(browserWindow, tenantIniPwd, 
				new String[]{loginJoyce, "JOYCE", "Brody", "JOYCE Brody", "", "ADMIN", "sap.epm:Modeler"},
				new String[]{loginMaria, "MARIA", "Overman", "MARIA Overman", "", "ADMIN", "sap.epm:Modeler"},
				new String[]{loginJerry, "JERRY", "Hooper", "JERRY Hooper", "", "ADMIN", "sap.epm:Modeler"});

		getFPAMessageArea().waitForMessage(Resource.getMessage("YOUR_CHANGES_SAVED"), INFO_TYPE.SUCCESS);

		logout();
		
		login(loginJoyce, tenantIniPwd,tenantPassword,tenant1);
		navigateTo(Resource.getMessage("MENU_LIBRARY"));
		
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		
		//clear file
		LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_PRIVATE"));
		if(LibraryHelper.deleteObjects(browserWindow, fileName,folderName)){
			this.getFPAMessageArea().waitForMessage(Resource.getMessage("CONTENTLIB_SELECTED_DELETED"), INFO_TYPE.SUCCESS);
		}

		LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_PUBLIC"));
		if(LibraryHelper.deleteObjects(browserWindow, fileName)){
			this.getFPAMessageArea().waitForMessage(Resource.getMessage("CONTENTLIB_SELECTED_DELETED"), INFO_TYPE.SUCCESS);
		}
		
		resourcesPath = getResourceFolder() +"Library"+File.separator+fileName;
		//upload file to public and check file detail
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		LibraryHelper.uploadFile(browserWindow, resourcesPath);

		this.getFPAMessageArea().waitForMessage(Resource.getMessage("CONTENTLIB_FILE_UPLOADED"), INFO_TYPE.SUCCESS);
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		LibraryHelper.assertDetail(browserWindow, fileName, "Description", Resource.getMessage("CONTENTLIB_FILE"));
		//search file
		LibraryHelper.search(browserWindow, fileName);
		LibraryHelper.assertObjectExistInList(browserWindow, fileName, true);
		LibraryHelper.clearSearch(browserWindow);
		LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_PUBLIC"));
		//LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_SEARCH"));
		LibraryHelper.assertObjectExistInList(browserWindow, fileName, true);
		//add file to favorite
		LibraryHelper.addToFavorite(browserWindow, fileName);
		LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_FAVORITES"));
		LibraryHelper.assertObjectExistInList(browserWindow, fileName, true);
		//copy file to private
		LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_PUBLIC"));
		LibraryHelper.copyCurrentObjectTo(browserWindow, fileName, loginJoyce+" "+Resource.getMessage("CONTENTLIB_PRIVATE"));
		LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_PRIVATE"));
		LibraryHelper.assertObjectExistInList(browserWindow, fileName, true);
		//create a folder and move file to this folder
		LibraryHelper.createFolder(browserWindow, folderName);

		this.getFPAMessageArea().waitForMessage(Resource.getMessage("CONTENTLIB_FOLDER_CREATED"), INFO_TYPE.SUCCESS);
		LibraryHelper.moveCurrentObjectTo(browserWindow, fileName, loginJoyce+" "+Resource.getMessage("CONTENTLIB_PRIVATE") + "/" + folderName);
		LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_PRIVATE") + "/" + folderName);

		LibraryHelper.assertObjectExistInList(browserWindow, fileName, true);
		//share private file to others
		LibraryHelper.assignPermissionsToUser(browserWindow, fileName, new String[] {"MARIA Overman","JERRY Hooper"}, new String[] {"false/true/true/true","false/true/false/false"});
		LibraryHelper.shareObjectToUsers(browserWindow, fileName, false, new String[] {loginMaria,loginJerry});

		this.getFPAMessageArea().waitForMessage(Resource.getMessage("CONTENTLIB_SELECTED_SHARED"), INFO_TYPE.SUCCESS);

		logout();
		//check users' permission on shared file
		login(loginJerry, tenantIniPwd,tenantPassword,tenant1);
		navigateTo(Resource.getMessage("MENU_LIBRARY"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_SHARED"));
		LibraryHelper.assertReadPermission(browserWindow, fileName, true);
		LibraryHelper.selectObjects(browserWindow, fileName);
		LibraryHelper.assertUpdatePermission(browserWindow, false);
		LibraryHelper.assertCopyPermission(browserWindow, true);
		LibraryHelper.assertDeletePermission(browserWindow, false);
		logout();
		
		
		login(loginMaria,tenantIniPwd, tenantPassword,tenant1);
		navigateTo(Resource.getMessage("MENU_LIBRARY"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_SHARED"));
		
		LibraryHelper.assertReadPermission(browserWindow, fileName, true);
		LibraryHelper.selectObjects(browserWindow, fileName);
		LibraryHelper.renameObject(browserWindow, fileName, fileNameNew);

		this.getFPAMessageArea().waitForMessage(Resource.getMessage("CONTENTLIB_SELECTED_RENAMED"), INFO_TYPE.SUCCESS);

		LibraryHelper.selectObjects(browserWindow, fileNameNew);
		LibraryHelper.assertCopyPermission(browserWindow, true);
		LibraryHelper.assertDeletePermission(browserWindow, true);
		logout();
		
		/**
		 * Below logic validate the authentication even the objects is updated, the owner has full access right
		 */
		login(loginJoyce, tenantPassword,tenant1);
		navigateTo(Resource.getMessage("MENU_LIBRARY"));
		
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_SHARED"));
		LibraryHelper.assertReadPermission(browserWindow, fileNameNew, true);
		LibraryHelper.selectObjects(browserWindow, fileNameNew);
		LibraryHelper.assertUpdatePermission(browserWindow, true);
		LibraryHelper.assertCopyPermission(browserWindow, true);
		LibraryHelper.assertDeletePermission(browserWindow, true);
		LibraryHelper.deleteObjects(browserWindow, fileNameNew);
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("CONTENTLIB_SELECTED_DELETED"), INFO_TYPE.SUCCESS);

		logout();
			
		//create a report and check whether it exists in the Initial Reports 
		login(tenant1Admin, tenantPassword,tenant1);
		navigateTo(Resource.getMessage("MENU_MODEL_MODELER"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		if (ModelingHelper.deleteModels(browserWindow, reportName)){
			SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
			this.getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_DELETE_MODEL_SUCCESS", reportName), INFO_TYPE.SUCCESS);

		}
		ModelingHelper.createModel(browserWindow, reportName,"",true);
		ModelingHelper.createWithExistingAccount(browserWindow, "Account");
		ModelingHelper.saveModel(browserWindow);

		this.getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_SAVE_MODEL_SUCCESS", reportName), INFO_TYPE.SUCCESS);

		
		navigateTo(Resource.getMessage("MENU_LIBRARY"));
		
		LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_INITIAL_REPORT"));
		LibraryHelper.assertObjectExistInList(browserWindow, reportName, true);
		
		//copy the initial report to public
		LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_PUBLIC"));
		if (LibraryHelper.deleteObjects(browserWindow, reportName)){
			this.getFPAMessageArea().waitForMessage(Resource.getMessage("CONTENTLIB_SELECTED_DELETED"), INFO_TYPE.SUCCESS);
		}
		LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_INITIAL_REPORT"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		LibraryHelper.copyCurrentObjectTo(browserWindow, reportName, Resource.getMessage("CONTENTLIB_PUBLIC"));
		LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_PUBLIC"));
		LibraryHelper.assertObjectExistInList(browserWindow, reportName, true);
		logout();
		
	}
	
}
		
