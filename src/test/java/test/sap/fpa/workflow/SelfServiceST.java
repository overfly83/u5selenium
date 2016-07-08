package test.sap.fpa.workflow;

import org.junit.Test;

import test.sap.fpa.security.RolesHelper;
import test.sap.fpa.security.UsersHelper;

import com.sap.ui5.selenium.sap.fpa.ui.control.shell.ShellInfoMessage.INFO_TYPE;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.ScenarioTest;

public class SelfServiceST extends ScenarioTest {

	static final String tenant1Admin = "ADMIN";
	static final String tenantPassword = "Abcd1234";
	static final String tenantIniPwd = "Initial1";
	static final String tenant1 = "TA1";
	static final String loginTom = "TOM";
	static final String loginJack = "JACK";
	static final String loginJustin = "JUSTIN";
	static final String defaultRole = "Default_S";
	static final String reviewerRole = "Reviewer_S";
	static final String approverRole = "Approver_S";


	@Test
	public void selfServiceScenarioTest() throws Exception {
		
		login(tenant1Admin, tenantPassword,tenant1);
			
		navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_DEFINE_ROLES"));

		//create role DEFAULT_S(minimal task) set as self service role, approver is FPA_ADMIN
		if(RolesHelper.deleteRoles(browserWindow, defaultRole,reviewerRole,approverRole)) {
			this.getFPAMessageArea().waitForMessage(Resource.getMessage("DELETE_ROLE_SUCCESSFULLY"),INFO_TYPE.SUCCESS);
		}
		
		RolesHelper.assertRoleExists(browserWindow, defaultRole, false);
		RolesHelper.assertRoleExists(browserWindow, reviewerRole, false);
		RolesHelper.assertRoleExists(browserWindow, approverRole, false);
		
		RolesHelper.addRole(browserWindow, defaultRole);

		RolesHelper.setSetting(browserWindow, true, false, false, tenant1Admin);
		RolesHelper.saveRole(browserWindow);
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("CREATE_ROLE_SUCCESSFULL_MSG"),INFO_TYPE.SUCCESS);
		
		navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_DEFINE_ROLES"));
	
		RolesHelper.assertRoleExists(browserWindow, defaultRole, true);
		
		//create role Reviewer_S( minimal) , set as self service role, approver is manager
				
		RolesHelper.addRole(browserWindow, reviewerRole);

		RolesHelper.setPermissions(browserWindow, Resource.getMessage("OBJECT_TYPE_PROFILE"), Resource.getMessage("READ"), Resource.getMessage("UPDATE"));
		RolesHelper.setPermissions(browserWindow, Resource.getMessage("OBJECT_TYPE_USER"), Resource.getMessage("READ"));
		RolesHelper.setSetting(browserWindow, true, false, false, "MANAGER");
		RolesHelper.saveRole(browserWindow);
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("CREATE_ROLE_SUCCESSFULL_MSG"),INFO_TYPE.SUCCESS);
		
		navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_DEFINE_ROLES"));
		RolesHelper.assertRoleExists(browserWindow, reviewerRole, true);
		
		//create role Approver_S( Include all task) , set as self service role, approver is manager

		RolesHelper.addRole(browserWindow, approverRole);
		RolesHelper.setPermissions(browserWindow, 
				Resource.getMessage("OBJECT_TYPE_DIMENSION"), 
				Resource.getMessage("CREATE"), 
				Resource.getMessage("READ"), 
				Resource.getMessage("UPDATE"), 
				Resource.getMessage("DELETE"), 
				Resource.getMessage("MAINTAIN"));
		RolesHelper.setPermissions(browserWindow, 
				Resource.getMessage("OBJECT_TYPE_RATE"), 
				Resource.getMessage("CREATE"),
				Resource.getMessage("READ"), 
				Resource.getMessage("UPDATE"), 
				Resource.getMessage("DELETE"), 
				Resource.getMessage("MAINTAIN"));
		RolesHelper.setPermissions(browserWindow, 
				Resource.getMessage("OBJECT_TYPE_CUBE"),
				Resource.getMessage("CREATE"),
				Resource.getMessage("READ"), 
				Resource.getMessage("UPDATE"), 
				Resource.getMessage("DELETE"), 
				Resource.getMessage("MAINTAIN"));
		
		RolesHelper.setPermissions(browserWindow, 
				Resource.getMessage("OBJECT_TYPE_PROFILE"),
				Resource.getMessage("CREATE"),
				Resource.getMessage("READ"), 
				Resource.getMessage("UPDATE"), 
				Resource.getMessage("DELETE"));
		RolesHelper.setPermissions(browserWindow, 
				Resource.getMessage("OBJECT_TYPE_USER"),
				Resource.getMessage("CREATE"),
				Resource.getMessage("READ"), 
				Resource.getMessage("UPDATE"), 
				Resource.getMessage("DELETE"),
				Resource.getMessage("ASSIGN"));
		RolesHelper.setPermissions(browserWindow, 
				Resource.getMessage("OBJECT_TYPE_PUBLICFILE"), 
				Resource.getMessage("CREATE"),
				Resource.getMessage("READ"));
		RolesHelper.setSetting(browserWindow, true, false, true, "MANAGER");
		RolesHelper.saveRole(browserWindow);
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("CREATE_ROLE_SUCCESSFULL_MSG"),INFO_TYPE.SUCCESS);
		
		navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_DEFINE_ROLES"));
		RolesHelper.assertRoleExists(browserWindow, approverRole, true);

		navigateTo(Resource.getMessage("MENU_USERS"), Resource.getMessage("MENU_USERS_SET_UP_USERS"));		
		if(UsersHelper.deleteUsers(browserWindow, "JUSTIN","JACK","TOM")){
			getFPAMessageArea().waitForMessage(Resource.getMessage("YOUR_CHANGES_SAVED"), INFO_TYPE.SUCCESS);
		}
		
		//create three users "TOM", "JACK","JUSTIN"
		UsersHelper.addUsers(browserWindow, tenantIniPwd, 
				new String[]{"TOM", "Tom", "Brody", "Tom Brody", "", tenant1Admin, ""},
				new String[]{"JACK", "Jack", "Overman", "Jack Overman", "", "TOM", ""},
				new String[]{"JUSTIN", "Justin", "Hooper", "Justin Hooper", "", "JACK", ""});
		getFPAMessageArea().waitForMessage(Resource.getMessage("YOUR_CHANGES_SAVED"), INFO_TYPE.SUCCESS);
		//logout(); dont' have to logout admin, the browser remains in memory stack for reuse
	
		//login and send request roles
		//check Tom's security control
		login(loginTom, tenantIniPwd, tenantPassword,tenant1);

		this.getShell().toggleNavigationPane();
		//RolesHelper.assertTaskAccessInNavigationMenu(browserWindow, Resource.getMessage("MENU_DASHBOARD"), true);
		getShell().selectShellOption(Resource.getMessage("MAIN_REQUEST_ROLES"));
		RolesHelper.sendRequest(browserWindow, approverRole, "self", "please approve"); 
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("REQUEST_SENT_TO_APPROVERS"),INFO_TYPE.SUCCESS);
		logout();
		
		//login(loginAdmin, loginAdminPwd); is reusing the browser in stack with previous login
		navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_USER_REQUESTS"));
		RolesHelper.processRequest(browserWindow, Resource.getMessage("USER_ID"), loginTom, "approve", "confirm");
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("REQUEST_APPROVED"),INFO_TYPE.SUCCESS);
		//logout();
		
		//check JACK's security control
		//Jacly apply for the new role from TOM
		login(loginJack, tenantIniPwd, tenantPassword,tenant1);
		
		this.getShell().toggleNavigationPane();
		//RolesHelper.assertTaskAccessInNavigationMenu(browserWindow, Resource.getMessage("MENU_DASHBOARD"), true);
		getShell().selectShellOption(Resource.getMessage("MAIN_REQUEST_ROLES"));
		RolesHelper.sendRequest(browserWindow, reviewerRole, "self", "please approve");
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("REQUEST_SENT_TO_APPROVERS"),INFO_TYPE.SUCCESS);
		logout();
		
		login(loginTom, tenantPassword,tenant1);
		getShell().assertItemInNotificationDisplayed(Resource.getMessage("ROLE_REQUEST_APPROVED_BY_USER", new String[] {approverRole, tenant1Admin}), true);
		navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_USER_REQUESTS"));
		RolesHelper.processRequest(browserWindow, Resource.getMessage("USER_ID"), loginJack, "approve", "confirm");
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("REQUEST_APPROVED"),INFO_TYPE.SUCCESS);
		logout();				
		
		//check Justin security control
		//Justin apply for the new role from TOM
		login(loginJustin, tenantIniPwd, tenantPassword,tenant1);
		
		this.getShell().toggleNavigationPane();
		//RolesHelper.assertTaskAccessInNavigationMenu(browserWindow, Resource.getMessage("MENU_DASHBOARD"), true);
		getShell().selectShellOption(Resource.getMessage("MAIN_REQUEST_ROLES"));
		RolesHelper.sendRequest(browserWindow, approverRole, "self", "please approve");
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("REQUEST_SENT_TO_APPROVERS"),INFO_TYPE.SUCCESS);
		//logout();
		
		login(loginJack, tenantPassword,tenant1);
		getShell().assertItemInNotificationDisplayed(Resource.getMessage("ROLE_REQUEST_APPROVED_BY_USER", new String[] {reviewerRole, loginTom}), true);
		navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_USER_REQUESTS"));
		RolesHelper.processRequest(browserWindow, Resource.getMessage("USER_ID"), loginJustin, "reject", "sorry");
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("REQUEST_REJECTED"),INFO_TYPE.SUCCESS);
		logout();
		
		
		//login(loginJustin, loginJustinPwd);
		getShell().assertItemInNotificationDisplayed(Resource.getMessage("ROLE_REQUEST_REJECTED_BY_USER", new String[] {approverRole, loginJack, "sorry"}),true);
		getShell().selectShellOption(Resource.getMessage("MAIN_REQUEST_ROLES"));
		RolesHelper.sendRequest(browserWindow, defaultRole, "self", "please approve");
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("REQUEST_SENT_TO_APPROVERS"),INFO_TYPE.SUCCESS);
		logout();
		
		//login(loginAdmin, loginAdminPwd);
		navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_USER_REQUESTS"));
		RolesHelper.processRequest(browserWindow, Resource.getMessage("USER_ID"), loginJustin, "approve", "confirm");
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("REQUEST_APPROVED"),INFO_TYPE.SUCCESS);
		logout();//logout admin
		
		login(loginJustin, tenantPassword,tenant1);
		getShell().assertItemInNotificationDisplayed(Resource.getMessage("ROLE_REQUEST_APPROVED_BY_USER", new String[] {defaultRole, tenant1Admin}),true);
		logout();	
		
		////////////////////////////////////////////////
		
		
	}
	
}
		
