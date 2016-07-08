package test.sap.fpa.workflow;

import org.junit.Test;

import test.sap.fpa.library.LibraryHelper;
import test.sap.fpa.security.RolesHelper;
import test.sap.fpa.security.TeamsHelper;
import test.sap.fpa.security.UsersHelper;

import com.sap.ui5.selenium.sap.fpa.ui.control.shell.ShellInfoMessage.INFO_TYPE;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;
import com.sap.ui5.selenium.utilities.ScenarioTest;

public class TeamSupportST extends ScenarioTest{
	static final String tenant1Admin = "ADMIN";
	static final String tenantPassword = "Abcd1234";
	static final String tenantIniPwd = "Initial1";
	static final String tenant1 = "TA1";
	static final String loginDavin = "DAVIN";
	static final String loginAlex = "ALEX";
	static final String team = "SalesTeam";
	static final String folder = loginDavin;
	@Test
	public void TeamScenarioTest(){
		login(tenant1Admin,tenantPassword,tenant1);
		navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_SET_UP_USERS"));
		if(UsersHelper.deleteUsers(browserWindow, loginDavin,loginAlex)){
			getFPAMessageArea().waitForMessage(Resource.getMessage("YOUR_CHANGES_SAVED"),INFO_TYPE.SUCCESS);
		}
		UsersHelper.addUsers(browserWindow, tenantIniPwd, 
				new String[]{loginDavin, "DAVIN", "Brody", loginDavin},
				new String[]{loginAlex, "ALEX", "Overman", loginAlex});
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("YOUR_CHANGES_SAVED"),INFO_TYPE.SUCCESS);
		
		navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_SET_UP_TEAMS"));
		if(TeamsHelper.deleteTeams(browserWindow, team)){
			getFPAMessageArea().waitForMessage(Resource.getMessage("DELETE_TEAM_SUCCESSFULLY"),INFO_TYPE.SUCCESS);
			SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		}
		TeamsHelper.addTeam(browserWindow, team, team,loginDavin);
		getFPAMessageArea().waitForMessage(Resource.getMessage("CREATE_TEAM_SUCCESSFULL_MSG"),INFO_TYPE.SUCCESS);
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		TeamsHelper.refreshTeams(browserWindow);
		TeamsHelper.assertTeamMembers(browserWindow, team, tenant1Admin, loginDavin);
		navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_DEFINE_ROLES"));
		RolesHelper.assignRole(browserWindow, "sap.epm:Admin", "TEAM:"+team);
		logout();
		
		login(loginDavin,tenantIniPwd,tenantPassword,tenant1);
		navigateTo(Resource.getMessage("MENU_LIBRARY"));
		LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_TEAMS"));
		
		LibraryHelper.assertObjectExistInList(browserWindow, team, true);
		LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_TEAMS")+"/"+team);
		LibraryHelper.createFolder(browserWindow, folder);
		getFPAMessageArea().waitForMessage(Resource.getMessage("CONTENTLIB_FOLDER_CREATED"), INFO_TYPE.SUCCESS);
		LibraryHelper.deleteObjects(browserWindow, folder);
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("CONTENTLIB_SELECTED_DELETED"), INFO_TYPE.SUCCESS);
		logout();
		
		login(loginAlex,tenantIniPwd,tenantPassword,tenant1);
		navigateTo(Resource.getMessage("MENU_LIBRARY"));
		LibraryHelper.gotoFolder(browserWindow, Resource.getMessage("CONTENTLIB_TEAMS"));
		
		LibraryHelper.assertObjectExistInList(browserWindow, team, false);
		logout();
		
	}
}
