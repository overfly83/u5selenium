package test.sap.fpa.workflow.obsolete;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import test.sap.fpa.security.UsersHelper;
import test.sap.fpa.system.AdministrationHelper;

import com.sap.ui5.selenium.sap.fpa.ui.control.shell.ShellInfoMessage.INFO_TYPE;
import com.sap.ui5.selenium.utilities.HttpRequest;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.ScenarioTest;

public class ImportADUsersST extends ScenarioTest {
	static final String loginAdmin = "FPA_ADMIN";
	static final String loginAdminPwd = "Abcd1234";
	static final String defaultPassword = "Initial1";
	static final String hostName = "vantgvmwin159.dhcp.pgdev.sap.corp";
	static final String port = "8080";
	static final String DBUser="SYSTEM";
	static final String DBUserPwd="manager";
	private String metaDataPath = getResourceFolder() + "Library"+File.separator + "V_FederationMetadata.xml";
	
	@Test @Ignore
	public void importADUsersScenairoTest() throws Exception{
		try{
			
			String postData = "{\"package\":\"sap.fpa.ui\",\"configuration\":{\"authentication\":[{\"method\":\"Form\"},{\"method\":\"Basic\"}]}}";
			new HttpRequest().sendPost("/sap/hana/xs/admin/server/runtimeConfig/config.xscfunc", postData, null, null);
			
			login(loginAdmin,loginAdminPwd,null);	
			navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_SET_UP_USERS"));
			if(UsersHelper.deleteUsers(browserWindow, "ADMINISTRATOR","FPATEST")){
				getFPAMessageArea().waitForMessage(Resource.getMessage("YOUR_CHANGES_SAVED"), INFO_TYPE.SUCCESS);
			}
			navigateTo(Resource.getMessage("MENU_SYSTEM"),Resource.getMessage("MENU_SYSTEM_ADMIN"));
			AdministrationHelper.gotoTab(browserWindow, Resource.getMessage("CONNECTION"));
			AdministrationHelper.setCloudConnection(browserWindow, hostName, port, DBUser, DBUserPwd);
			getFPAMessageArea().waitForMessage(Resource.getMessage("SAML_SAVE_CONNECTION_SUCCESS_TEXT"),INFO_TYPE.SUCCESS);
			AdministrationHelper.gotoTab(browserWindow, Resource.getMessage("SAML_IDENTITY_PROVIDER"));
			AdministrationHelper.setSMALIdentityProvider(browserWindow, metaDataPath);
			getFPAMessageArea().waitForMessage(Resource.getMessage("SAMLIDP_SAVE_SUCCESS_TEXT"),INFO_TYPE.SUCCESS);
			AdministrationHelper.gotoTab(browserWindow, Resource.getMessage("SAML_SERVICE_PROVIDER"));
			AdministrationHelper.setServiceProviderInformation(browserWindow, "SAP", "SAP", "SAP");
			getFPAMessageArea().waitForMessage(Resource.getMessage("SAMLSP_SAVE_SUCCESS_TEXT"),INFO_TYPE.SUCCESS);
			navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_SET_UP_USERS"));
			UsersHelper.importADUsers(browserWindow, defaultPassword, new String[]{"ADMINISTRATOR", "FPATEST"});
			getFPAMessageArea().waitForMessage(Resource.getMessage("YOUR_CHANGES_SAVED"),INFO_TYPE.SUCCESS);
			logout();
			
			login("fpatest",defaultPassword, "Abcd1234");
			logout();
			
			login(loginAdmin,loginAdminPwd,null);
			navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_SET_UP_USERS"));
			if(UsersHelper.deleteUsers(browserWindow, "FPATEST")){
				getFPAMessageArea().waitForMessage(Resource.getMessage("YOUR_CHANGES_SAVED"), INFO_TYPE.SUCCESS);
			}
			navigateTo(Resource.getMessage("MENU_SYSTEM"),Resource.getMessage("MENU_SYSTEM_ADMIN"));
			AdministrationHelper.gotoTab(browserWindow, Resource.getMessage("AUTHENTICATION_TAB_TITLE"));
			AdministrationHelper.enableSAMLAuthentication(browserWindow, true);
			getFPAMessageArea().waitForMessage(Resource.getMessage("CHANGE_AUTHENTICATION_SUCCESS_TEXT"),INFO_TYPE.SUCCESS);
			logout();
		
			loginAD("fpatest","Abcd1234");
			logout();
		}catch(Exception ex){
			throw ex;
		}finally{
			String postData = "{\"package\":\"sap.fpa.ui\",\"configuration\":{\"authentication\":[{\"method\":\"Form\"},{\"method\":\"Basic\"}]}}";
			new HttpRequest().sendPost("/sap/hana/xs/admin/server/runtimeConfig/config.xscfunc", postData, null, null);
			
		}
		
	
	}
}
