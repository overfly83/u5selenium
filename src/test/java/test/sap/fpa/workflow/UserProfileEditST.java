package test.sap.fpa.workflow;

import java.io.File;

import org.junit.Test;

import test.sap.fpa.security.UsersHelper;

import com.sap.ui5.selenium.sap.fpa.ui.control.shell.ShellInfoMessage.INFO_TYPE;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.ScenarioTest;

public class UserProfileEditST extends ScenarioTest{
	static final String tenant1Admin = "ADMIN";
	static final String tenantPassword = "Abcd1234";
	static final String tenantIniPwd = "Initial1";
	static final String tenant1 = "TA1";
	static final String loginBright = "BRIGHT";
	private String imagePath1 =  getResourceFolder()+"image"+File.separator+"img001.png";
	private String imagePath2 =  getResourceFolder()+"image"+File.separator+"img002.png";
	
	@Test
	public void userProfileScenarioTest() throws Exception {
		login(tenant1Admin, tenantPassword,tenant1);
		
		navigateTo(Resource.getMessage("MENU_USERS"), Resource.getMessage("MENU_USERS_SET_UP_USERS"));		
		
		if(UsersHelper.deleteUsers(browserWindow, loginBright)){
			getFPAMessageArea().waitForMessage(Resource.getMessage("YOUR_CHANGES_SAVED"), INFO_TYPE.SUCCESS);
		}
		UsersHelper.addUser(browserWindow, loginBright, "BRIGHT", "James", "BRIGHT James", "", tenant1Admin, " ", tenantIniPwd);
		getFPAMessageArea().waitForMessage(Resource.getMessage("YOUR_CHANGES_SAVED"), INFO_TYPE.SUCCESS);
		logout();
		
		//check Bright's security control
		login(loginBright, tenantIniPwd, tenantPassword,tenant1); 
		this.getShell().selectShellOption(Resource.getMessage("MAIN_PROFILE"));
		UsersHelper.editProfile(browserWindow, new String[]{loginBright, "Dev", "13910733521", "8888110", "bright@sap.com", "dev", "pvg03B5"});
		UsersHelper.editProfileImage(browserWindow, imagePath1);
		UsersHelper.editProfileImage(browserWindow, imagePath2);
		UsersHelper.checkProfile(browserWindow, new String[]{loginBright, "Dev", "13910733521", "8888110", "bright@sap.com", "dev", "pvg03B5"});
		UsersHelper.closeProfile(browserWindow);
		
		logout();
	}
}
