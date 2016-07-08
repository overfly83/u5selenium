package test.sap.fpa.workflow;

import java.sql.SQLException;
import java.util.Calendar;

import org.junit.Test;

import test.sap.fpa.modeling.ModelingHelper;
import test.sap.fpa.report.ReportHelper;
import test.sap.fpa.report.ReportsHelper;
import test.sap.fpa.security.AuditHelper;
import test.sap.fpa.security.UsersHelper;

import com.sap.ui5.selenium.sap.fpa.ui.control.shell.ShellInfoMessage.INFO_TYPE;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;
import com.sap.ui5.selenium.utilities.ScenarioTest;

public class ActivityDataAuditST  extends ScenarioTest{
	static final String tenant1Admin = "ADMIN";
	static final String tenantPassword = "Abcd1234";
	static final String tenantIniPwd = "Initial1";
	static final String tenant1 = "TA1";
	static final String loginKevin = "KEVIN";
	static final String loginKevinPwd = "Abcd1234";
	static final String modelName = "auditTest";
	static final String modelDesc = modelName+"desc";
	static final String accountName = "auditAccount";
	static final String versionName = "auditTempVsn";
	@Test
	public void ActivityDataAuditScenarioTest() throws SQLException{
		
		//add new user "Kevin" who has full access
		login(tenant1Admin, tenantPassword,tenant1);
		String tenantId = this.getTenantId();
		navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_SET_UP_USERS"));
		if(UsersHelper.deleteUsers(browserWindow, loginKevin)){
			getFPAMessageArea().waitForMessage(Resource.getMessage("YOUR_CHANGES_SAVED"),INFO_TYPE.SUCCESS);
		}
		UsersHelper.addUser(browserWindow, loginKevin, "KEVIN", "Garnet", loginKevin,  "", "" , "sap.epm:Admin", tenantIniPwd);
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("YOUR_CHANGES_SAVED"),INFO_TYPE.SUCCESS);
		logout();
		
		//create a model
		login(loginKevin,  tenantIniPwd, loginKevinPwd,tenant1);
		navigateTo(Resource.getMessage("MENU_MODEL_MODELER"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		if (ModelingHelper.deleteModels(browserWindow, modelName)){
			this.getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_DELETE_MODEL_SUCCESS", modelName),INFO_TYPE.SUCCESS);
		}
		ModelingHelper.goToPerspectivesTab(browserWindow);
		if (ModelingHelper.deletePerspectivesByName(browserWindow, accountName)){
			this.getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_DELETE_DIMEN_SUCCESS", accountName),INFO_TYPE.SUCCESS);
		}
		
		ModelingHelper.createModel(browserWindow, modelName, "model for audit test", true);
		ModelingHelper.configureModelPreference(browserWindow, modelName,null, true, null,false);
		ModelingHelper.createNewAccount(browserWindow, accountName, accountName + "Desc",
				"400000\tREVENUE\t\t\n"
				+ "400005\tREVENUE-INTERCOMPANY\t\t400000\n"
				+ "400010\tRETAIL RETURNS\t\t400000\n"
				+ "400015\tREVENUE - DISTRIBUTOR FEE\t\t400005\n");
		SapUi5Utilities.waitUntilMessageDisappear(browserWindow.driver());
		ModelingHelper.saveModel(browserWindow);
		getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_SAVE_MODEL_SUCCESS", modelName),INFO_TYPE.SUCCESS);		
		
		String timeduration = String.valueOf(Calendar.getInstance().get(Calendar.YEAR))+"09";
		this.runSql("insert into \"TENANT_"+tenantId+"\".\"t."+tenantId+"::TRNS_"+modelName+"\" values('400015','"+timeduration+"','public.Actual',100,'UNUSED')");
		this.runSql("insert into \"TENANT_"+tenantId+"\".\"t."+tenantId+"::TRNS_"+modelName+"\" values('400010','"+timeduration+"','public.Actual',200,'UNUSED')");
		this.runSql("insert into \"TENANT_"+tenantId+"\".\"t."+tenantId+"::TRNS_"+modelName+"\" values('400005','"+timeduration+"','public.Actual',100,'UNUSED')");
		this.runSql("insert into \"TENANT_"+tenantId+"\".\"t."+tenantId+"::TRNS_"+modelName+"\" values('400000','"+timeduration+"','public.Actual',300,'UNUSED')");
		
		ModelingHelper.openModel(browserWindow, modelName);	
		ModelingHelper.configureModelPreference(browserWindow, modelDesc,null, true, null,false);
		ModelingHelper.saveModel(browserWindow);
		getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_MODEL_UPDATED", modelName),INFO_TYPE.SUCCESS);
		//publish a version
		navigateTo(Resource.getMessage("MENU_REPORT"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		//ReportsHelper.gotoTab(browserWindow, Resource.getMessage("REPORTS_MNU_ALL"));
		ReportsHelper.openReport(browserWindow, modelName);
		ReportHelper.assertReportIsOpen(browserWindow, modelName);
		ReportHelper.copyVersion(browserWindow, "Actual", versionName);

		ReportHelper.checkCellValue(browserWindow, "C3", versionName,20);
		ReportHelper.changeCellValue(browserWindow, "C7", "333");
		ReportHelper.publishVersion(browserWindow, versionName, true, versionName+"new", Resource.getMessage("CATEGORY_DEFAULTNAME_PLANNING"));

		
		//check Data Changes works
		navigateTo(Resource.getMessage("MENU_USERS"), Resource.getMessage("MENU_USERS_DATA_CHANGE_LOG"));
		AuditHelper.selectModel(browserWindow, modelName);
		AuditHelper.checkItemsExist(browserWindow, new String[] {
				"auditAccountDesc", "Category", Resource.getMessage("DATA_AUDIT_REPORT_AUDIT_USER"), "SignedData - " + Resource.getMessage("DATA_AUDIT_REPORT_OLD"), 
				"SignedData - " + Resource.getMessage("DATA_AUDIT_REPORT_NEW"), "SignedData - " + Resource.getMessage("DATA_AUDIT_REPORT_DELTA"), Resource.getMessage("DATA_AUDIT_REPORT_AUDIT_ACTION")}, 
				new String[] {"400010", "public."+versionName+"new", loginKevin, "0", "333", "333", "New"});
		
		//delete model and perspective
		navigateTo(Resource.getMessage("MENU_MODEL_MODELER"));
		ModelingHelper.deleteModels(browserWindow, modelName);
		getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_DELETE_MODEL_SUCCESS", modelName),INFO_TYPE.SUCCESS);
		ModelingHelper.goToPerspectivesTab(browserWindow);
		ModelingHelper.deletePerspectivesByName(browserWindow, accountName);
		getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_DELETE_DIMEN_SUCCESS", accountName),INFO_TYPE.SUCCESS);

		//check activity model create, update and delete
		navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_ACTIVITY_LOG"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());

		AuditHelper.setActivityFilter(browserWindow, null, null, Resource.getMessage("OBJECT_TYPE_CUBE"), null, null,null, null);

		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		AuditHelper.assertItemsExist(browserWindow, new String[]{Resource.getMessage("AUDIT_REPORT_OBJECT_TYPE"), Resource.getMessage("AUDIT_REPORT_PACKAGE"),
																 Resource.getMessage("AUDIT_REPORT_OBJECT_NAME"), Resource.getMessage("AUDIT_REPORT_ACTIVITY"), Resource.getMessage("AUDIT_REPORT_USER_NAME")}, 
													new String[]{Resource.getMessage("OBJECT_TYPE_CUBE"), "t."+tenantId, modelName, Resource.getMessage("CREATE"), loginKevin}, true);
		AuditHelper.assertItemsExist(browserWindow, new String[]{Resource.getMessage("AUDIT_REPORT_OBJECT_TYPE"), Resource.getMessage("AUDIT_REPORT_PACKAGE"),
				 												 Resource.getMessage("AUDIT_REPORT_OBJECT_NAME"), Resource.getMessage("AUDIT_REPORT_ACTIVITY"), Resource.getMessage("AUDIT_REPORT_USER_NAME")}, 
				 									new String[]{Resource.getMessage("OBJECT_TYPE_CUBE"), "t."+tenantId, modelDesc, Resource.getMessage("UPDATE"), loginKevin}, true);
		AuditHelper.assertItemsExist(browserWindow, new String[]{Resource.getMessage("AUDIT_REPORT_OBJECT_TYPE"), Resource.getMessage("AUDIT_REPORT_PACKAGE"),
				 												 Resource.getMessage("AUDIT_REPORT_OBJECT_NAME"), Resource.getMessage("AUDIT_REPORT_ACTIVITY"), Resource.getMessage("AUDIT_REPORT_USER_NAME")}, 
				 									new String[]{Resource.getMessage("OBJECT_TYPE_CUBE"), "t."+tenantId, modelName, Resource.getMessage("DELETE"), loginKevin}, true);

		logout();
	}
		
}
