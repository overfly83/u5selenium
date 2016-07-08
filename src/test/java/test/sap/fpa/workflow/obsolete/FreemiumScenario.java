package test.sap.fpa.workflow.obsolete;

import java.sql.SQLException;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import com.sap.ui5.selenium.sap.fpa.ui.control.shell.ShellInfoMessage.INFO_TYPE;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;
import com.sap.ui5.selenium.utilities.ScenarioTest;

import test.sap.fpa.collaboration.CollaborationHelper;
import test.sap.fpa.console.ConsoleHelper;
import test.sap.fpa.library.LibraryHelper;
import test.sap.fpa.modeling.ModelingHelper;
import test.sap.fpa.security.UsersHelper;


public class FreemiumScenario extends ScenarioTest {
	static final String loginAdmin = "SYSTEM";
	static final String loginMike = "MIKE";
	static final String loginAdminPwd = "manager";
	static final String loginMikePwd = "Abcd1234";
	static final String folderName ="MikeFolder";
	static final String modelName ="MIKE" + new Date().getTime();
	
	@SuppressWarnings("deprecation")
	@Test @Ignore
	public void FreemiumScenarioMain(){
		try{
			runSql("insert into  \"SAP_FPA_SERVICES\".\"sap.fpa.services.core.system::Config\" values('DEVELOPMENT_MODE','true')");
		} catch (SQLException e) {
			try {
				runSql("upsert  \"SAP_FPA_SERVICES\".\"sap.fpa.services.core.system::Config\" values('DEVELOPMENT_MODE','true') with primary key");
			} catch (SQLException ex) {}
		}
		this.loginConsole(loginAdmin, loginAdminPwd);
		
		ConsoleHelper.gotoTab(browserWindow, "Feature List");
		ConsoleHelper.turnOnToggle(browserWindow,"FREEMIUM");
		this.login(loginAdmin, loginAdminPwd,null);
		navigateTo(Resource.getMessage("MENU_USERS"), Resource.getMessage("MENU_USERS_SET_UP_USERS"));
		if(UsersHelper.deleteUsers(browserWindow, loginMike)){
			getFPAMessageArea().waitForMessage(Resource.getMessage("YOUR_CHANGES_SAVED"), INFO_TYPE.SUCCESS);
		}
		this.logout();
		this.logoutConsole();
		
		this.registerFreemiumUser(loginMike, loginMikePwd);
		//this.logout();
		//this.login(loginMike, loginMikePwd);
		//Check "Share" button's existence status
		navigateTo(Resource.getMessage("MENU_LIBRARY"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_PRIVATE"));
		if(LibraryHelper.checkItemExist(browserWindow, folderName)){
			LibraryHelper.deleteObjects(browserWindow, folderName);

			this.getFPAMessageArea().waitForMessage(Resource.getMessage("CONTENTLIB_SELECTED_DELETED"),INFO_TYPE.SUCCESS);

		}
		LibraryHelper.createFolder(browserWindow, folderName);
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("CONTENTLIB_FOLDER_CREATED"),INFO_TYPE.SUCCESS);
		
		LibraryHelper.selectObjects(browserWindow, folderName);
		LibraryHelper.assertSharePermission(browserWindow, false);
		
		//Check user can only create one model
		navigateTo(Resource.getMessage("MENU_MODEL_MODELER"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		ModelingHelper.createModel(browserWindow, modelName,"",true);
		ModelingHelper.createNewAccount(browserWindow, modelName+"act", "accountFree", "400000\tREVENUE\t\t\n");
		ModelingHelper.saveModel(browserWindow);
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_SAVE_MODEL_SUCCESS", modelName),INFO_TYPE.SUCCESS);
		ModelingHelper.assertCreatePermission(browserWindow, false);
		
		//Check user can not open colloboration panel
		getShell().toggleDiscussions();
		CollaborationHelper.assertCollaborationEnabled(browserWindow, false);
		logout();
		
		//Check other user can not see Mike's folder
		login(loginAdmin, loginAdminPwd,null);
		navigateTo(Resource.getMessage("MENU_LIBRARY"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		LibraryHelper.search(browserWindow, folderName);
		LibraryHelper.assertObjectExistInList(browserWindow, folderName, false);
		logout();
	}
		
}
