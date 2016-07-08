package test.sap.fpa.workflow;

import java.util.Calendar;

import org.junit.Test;

import test.sap.fpa.modeling.ModelingHelper;
import test.sap.fpa.report.ReportHelper;
import test.sap.fpa.report.ReportsHelper;
import test.sap.fpa.security.RolesHelper;
import test.sap.fpa.security.UsersHelper;

import com.sap.ui5.selenium.sap.fpa.ui.control.shell.ShellInfoMessage.INFO_TYPE;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;
import com.sap.ui5.selenium.utilities.ScenarioTest;

public class RowLevelSecurityST extends ScenarioTest {

	static final String tenant1Admin = "ADMIN";
	static final String tenantPassword = "Abcd1234";
	static final String tenantIniPwd = "Initial1";
	static final String tenant1 = "TA1";
	static final String loginBruce = "BRUCE";
	static final String modelName ="rowLevelModel";
	static final String accountName ="rowLevelAccount";
	static final String roleName = "DACTEST";
	static final String versionName = "MyVersion";

	
	@Test
	public void RowLevelSecurityScenarioTest() throws Exception {
		
		login(tenant1Admin, tenantPassword,tenant1);
		String tenantId = this.getTenantId();
		//add Model
		navigateTo(Resource.getMessage("MENU_MODEL_MODELER"));
		if (ModelingHelper.deleteModels(browserWindow, modelName)){
			this.getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_DELETE_MODEL_SUCCESS", modelName),INFO_TYPE.SUCCESS);
		}
		ModelingHelper.goToPerspectivesTab(browserWindow);
		if (ModelingHelper.deletePerspectivesByName(browserWindow, accountName)){
			this.getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_DELETE_DIMEN_SUCCESS", accountName),INFO_TYPE.SUCCESS);
		}
		ModelingHelper.createModel(browserWindow, modelName, "model for access test", true);
		String startdate = String.valueOf(Calendar.getInstance().get(Calendar.YEAR))+"/1/1";
		String enddate = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)+1)+"/12/31";
		ModelingHelper.configureTimeCategories(browserWindow, Resource.getMessage("TIME_GRANULARITY_MONTH"), 
												startdate, 
												enddate, 
												Resource.getMessage("TIME_GRANULARITY_MONTH"),
												Resource.getMessage("TIME_GRANULARITY_MONTH"),
												Resource.getMessage("TIME_GRANULARITY_MONTH"),
												Resource.getMessage("TIME_GRANULARITY_QUARTER"),
												Resource.getMessage("TIME_GRANULARITY_MONTH"), 3, 3);
		ModelingHelper.configureModelPreference(browserWindow, "model preference",null, false, null,true);
		ModelingHelper.createNewAccount(browserWindow, accountName, accountName, "400000\tREVENUE\t\t\t\n"
				+ "400005\tREVENUE-INTERCOMPANY\t\t400000\t\n"
				+ "400010\tRETAIL RETURNS\t\t400000\t\n"
				+ "400015\tREVENUE - DISTRIBUTOR FEE\t\t400005\t\n");
		ModelingHelper.saveModel(browserWindow);
		getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_SAVE_MODEL_SUCCESS", modelName),INFO_TYPE.SUCCESS);
		
		String timeduration = String.valueOf(Calendar.getInstance().get(Calendar.YEAR))+"09";
		this.runSql("insert into \"TENANT_"+tenantId+"\".\"t."+tenantId+"::TRNS_"+modelName+"\" values('400015','"+timeduration+"','public.Actual',100,'UNUSED')");
		this.runSql("insert into \"TENANT_"+tenantId+"\".\"t."+tenantId+"::TRNS_"+modelName+"\" values('400010','"+timeduration+"','public.Actual',200,'UNUSED')");
		this.runSql("insert into \"TENANT_"+tenantId+"\".\"t."+tenantId+"::TRNS_"+modelName+"\" values('400005','"+timeduration+"','public.Actual',100,'UNUSED')");
		this.runSql("insert into \"TENANT_"+tenantId+"\".\"t."+tenantId+"::TRNS_"+modelName+"\" values('400000','"+timeduration+"','public.Actual',300,'UNUSED')");
				
		//add role DACTEST and user Bruce, and assign role to user Bruce
		navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_DEFINE_ROLES"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		if(RolesHelper.deleteRoles(browserWindow, roleName)) {
			this.getFPAMessageArea().waitForMessage(Resource.getMessage("DELETE_ROLE_SUCCESSFULLY"),INFO_TYPE.SUCCESS);
			SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		}
		RolesHelper.addRole(browserWindow, roleName);
		RolesHelper.setPermissions(browserWindow, Resource.getMessage("OBJECT_TYPE_DIMENSION"), Resource.getMessage("READ"), Resource.getMessage("UPDATE"));
		RolesHelper.setPermissions(browserWindow, Resource.getMessage("OBJECT_TYPE_CUBE"), Resource.getMessage("READ"), Resource.getMessage("UPDATE"));
	
		RolesHelper.addLimitedAccess(browserWindow, modelName, "read", new String[]{accountName+".ID"}, new String[]{"="}, new String[]{"400015"});
		RolesHelper.addLimitedAccess(browserWindow, modelName, "write", new String[]{accountName+".ID"}, new String[]{"="}, new String[]{"400015"});
		RolesHelper.saveRole(browserWindow);		
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("CREATE_ROLE_SUCCESSFULL_MSG"),INFO_TYPE.SUCCESS);
		
		navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_SET_UP_USERS"));
		if(UsersHelper.deleteUsers(browserWindow, loginBruce)){
			this.getFPAMessageArea().waitForMessage(Resource.getMessage("YOUR_CHANGES_SAVED"),INFO_TYPE.SUCCESS);
		}
		UsersHelper.addUser(browserWindow, loginBruce, "", "", "BRUCE", "", "", roleName, tenantIniPwd);
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("YOUR_CHANGES_SAVED"),INFO_TYPE.SUCCESS);
		

		//check authorization in the report.
		login(loginBruce, tenantIniPwd, tenantPassword,tenant1);
		navigateTo(Resource.getMessage("MENU_REPORT"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		//ReportsHelper.gotoTab(browserWindow, Resource.getMessage("REPORTS_MNU_ALL"));
		ReportsHelper.openReport(browserWindow, modelName);
		ReportHelper.assertReportIsOpen(browserWindow, modelName);
		ReportHelper.checkCellValue(browserWindow, "A5", "REVENUE",20);
		ReportHelper.checkCellValue(browserWindow, "B5", ReportHelper.formatNumber(100, 2),20);
		ReportHelper.checkCellValue(browserWindow, "A6", "REVENUE-INTERCOMPANY",20);
		ReportHelper.checkCellValue(browserWindow, "B6", ReportHelper.formatNumber(100, 2),20);
		ReportHelper.copyVersion(browserWindow, "Actual", versionName);
		ReportHelper.checkCellValue(browserWindow, "C3", versionName,20);
		ReportHelper.DrillRows(browserWindow, 0, 5);
		ReportHelper.checkCellValue(browserWindow, "A7", "REVENUE - DISTRIBUTOR FEE",30);
		ReportHelper.changeCellValue(browserWindow, "C7", "333");
		ReportHelper.checkCellValue(browserWindow, "C7", ReportHelper.formatNumber(333, 2),20);
		ReportHelper.setFilter(browserWindow, accountName, "ID", null, "400015");
		ReportHelper.checkCellValue(browserWindow, "B5", ReportHelper.formatNumber(100, 2),20);
		ReportHelper.saveReport(browserWindow);
		logout();
		
	}
	
}
		
