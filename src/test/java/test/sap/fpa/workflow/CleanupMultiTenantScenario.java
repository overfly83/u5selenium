package test.sap.fpa.workflow;

import org.junit.Test;

import com.sap.ui5.selenium.utilities.ScenarioTest;

import test.sap.fpa.console.ConsoleHelper;


public class CleanupMultiTenantScenario extends ScenarioTest {
	static final String loginSystem = "SYSTEM";
	static final String loginPassword = "manager";
	static final String tenant1 = "TA1";
	static final String tenant1Admin ="ADMIN";
	static final String tenant2 = "TA2";
	static final String tenant2Admin ="ADMIN";
	static final String tenantIniPwd ="Initial1";
	static final String tenantPassword ="Abcd1234";
	
	@Test
	public void EnableMultiTenantScenarioMain(){
		
		this.loginConsole(loginSystem, loginPassword);
		
		ConsoleHelper.gotoTab(browserWindow, "Tenant Management");
		if(ConsoleHelper.multiSelectTanants(browserWindow, tenant1,tenant2)){
			ConsoleHelper.deleteTenants(browserWindow, tenant1,tenant2);
		}
		
		this.logoutConsole();
		
	}
		
}
