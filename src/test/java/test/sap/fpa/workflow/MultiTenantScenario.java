package test.sap.fpa.workflow;

import java.sql.SQLException;

import org.junit.Test;

import com.sap.ui5.selenium.utilities.ScenarioTest;

import test.sap.fpa.console.ConsoleHelper;


public class MultiTenantScenario extends ScenarioTest {
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
		try{
			runSql("insert into  \"SAP_FPA_SERVICES\".\"sap.fpa.services.core.system::Config\" values('DEVELOPMENT_MODE','true')");
		} catch (SQLException e) {
			try {
				runSql("upsert  \"SAP_FPA_SERVICES\".\"sap.fpa.services.core.system::Config\" values('DEVELOPMENT_MODE','true') with primary key");
			} catch (SQLException ex) {}
		}
		this.loginConsole(loginSystem, loginPassword);
		
		ConsoleHelper.gotoTab(browserWindow, "Feature List");
		ConsoleHelper.turnOnToggle(browserWindow,"INFRA_MULTI_TENANT");
		//ConsoleHelper.turnOffToggle(browserWindow,"GEO");
		ConsoleHelper.gotoTab(browserWindow, "System Configuration");
		ConsoleHelper.setSystemConfiguration(browserWindow, "MULTI_TENANT", true);

		ConsoleHelper.gotoTab(browserWindow, "Tenant Management");
		if(ConsoleHelper.multiSelectTanants(browserWindow, tenant1,tenant2)){
			ConsoleHelper.deleteTenants(browserWindow, tenant1,tenant2);
		}
		ConsoleHelper.addTenant(browserWindow, tenant1, tenantIniPwd);
		ConsoleHelper.setTenantPackage(browserWindow, tenant1, "test.infra");
		
		ConsoleHelper.addTenant(browserWindow, tenant2, tenantIniPwd);
		this.logoutConsole();
		
		login(tenant1Admin, tenantIniPwd, tenantPassword,tenant1);
		logout();
		login(tenant2Admin, tenantIniPwd, tenantPassword,tenant2);
		logout();
		
		try{
			String TA1_SCHEMA = runQuery("select \"SCHMEA\" from \"SAP_FPA_SERVICES\".\"sap.fpa.services.core.cloudops::Tenant.TENANT\" where description ='"+tenant1+"'");
			String TA2_SCHEMA = runQuery("select \"SCHMEA\" from \"SAP_FPA_SERVICES\".\"sap.fpa.services.core.cloudops::Tenant.TENANT\" where description ='"+tenant2+"'");
			runSql("UPSERT \""+TA1_SCHEMA+"\".\"sap.fpa.services.customerInfo::CustomerInfo\" VALUES('thresholdStandardUser', 20) with primary key");
			runSql("UPSERT \""+TA1_SCHEMA+"\".\"sap.fpa.services.customerInfo::CustomerInfo\" VALUES('thresholdProfessionalUser', 200) with primary key");
			runSql("UPSERT \""+TA1_SCHEMA+"\".\"sap.fpa.services.customerInfo::CustomerInfo\" VALUES('thresholdC4AUser', 20) with primary key");
			runSql("UPSERT \""+TA1_SCHEMA+"\".\"sap.fpa.services.customerInfo::CustomerInfo\" VALUES('thresholdBIUser', 20) with primary key");
			
			runSql("UPSERT \""+TA2_SCHEMA+"\".\"sap.fpa.services.customerInfo::CustomerInfo\" VALUES('thresholdStandardUser', 20) with primary key");
			runSql("UPSERT \""+TA2_SCHEMA+"\".\"sap.fpa.services.customerInfo::CustomerInfo\" VALUES('thresholdProfessionalUser', 200) with primary key");
			runSql("UPSERT \""+TA2_SCHEMA+"\".\"sap.fpa.services.customerInfo::CustomerInfo\" VALUES('thresholdC4AUser', 20) with primary key");
			runSql("UPSERT \""+TA2_SCHEMA+"\".\"sap.fpa.services.customerInfo::CustomerInfo\" VALUES('thresholdBIUser', 20) with primary key");
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
		
}
