package test.sap.fpa.workflow;

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

public class IntegratedModelSecurityST extends ScenarioTest {

	static final String tenant1Admin = "ADMIN";
	static final String tenantPassword = "Abcd1234";
	static final String tenantIniPwd = "Initial1";
	static final String tenant1 = "TA1";
	static final String loginLee = "LEE";
	//static final String sqlAPModel ="PERSON_CALC";
	static final String apModel = "PERSON_AP_CAL";
	static final String roleName = "INTEGRATED_TEST";
	static final String versionName = "MyVersion";

	
	@Test
	public void IntegratedModelSecurityScenarioTest() throws Exception {
		
		login(tenant1Admin, tenantPassword,tenant1);
		
		//add Model
		navigateTo(Resource.getMessage("MENU_MODEL_MODELER"));
		//if(ModelingHelper.deleteModels(browserWindow, sqlAPModel)){
		//	getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_DELETE_MODEL_SUCCESS", sqlAPModel),INFO_TYPE.SUCCESS);
		//}
		if(ModelingHelper.deleteModels(browserWindow, apModel)){
			getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_DELETE_MODEL_SUCCESS", apModel),INFO_TYPE.SUCCESS);
		}
		
		//ModelingHelper.importModelFromHana(browserWindow, sqlAPModel);
		//getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_CREATE_MODEL_SUCCESS", sqlAPModel),INFO_TYPE.SUCCESS);
		
		ModelingHelper.importModelFromHana(browserWindow, apModel);
		getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_CREATE_MODEL_SUCCESS", apModel), INFO_TYPE.SUCCESS);
		
		navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_DEFINE_ROLES"));
		if(RolesHelper.deleteRoles(browserWindow, roleName)){
			getFPAMessageArea().waitForMessage(Resource.getMessage("DELETE_ROLE_SUCCESSFULLY"), INFO_TYPE.SUCCESS);
		}
		RolesHelper.addRole(browserWindow, roleName);
		RolesHelper.setPermissions(browserWindow, Resource.getMessage("OBJECT_TYPE_DIMENSION"), Resource.getMessage("READ"),  Resource.getMessage("UPDATE"));
		RolesHelper.setPermissions(browserWindow, Resource.getMessage("OBJECT_TYPE_CUBE"),Resource.getMessage("READ"), Resource.getMessage("UPDATE"));
		//RolesHelper.addLimitedAccess(browserWindow, "test.infra:"+sqlAPModel, "read", new String[]{"ID"}, new String[]{">="}, new String[]{"f"});
		RolesHelper.addLimitedAccess(browserWindow, "test.infra:"+apModel, "read", new String[]{"ID"}, new String[]{">="}, new String[]{"f"});
		
		RolesHelper.saveRole(browserWindow);
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("CREATE_ROLE_SUCCESSFULL_MSG"),INFO_TYPE.SUCCESS);
		
		navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_SET_UP_USERS"));
		if(UsersHelper.deleteUsers(browserWindow, loginLee)){
			this.getFPAMessageArea().waitForMessage(Resource.getMessage("YOUR_CHANGES_SAVED"), INFO_TYPE.SUCCESS);
		}
		UsersHelper.addUser(browserWindow, loginLee, "Bluek", "Lee", "Bluek Lee", "", "", roleName, tenantIniPwd);
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("YOUR_CHANGES_SAVED"), INFO_TYPE.SUCCESS);
		
		logout();
		
		login(loginLee,tenantIniPwd,tenantPassword,tenant1);
		
		/*navigateTo(Resource.getMessage("MENU_REPORT"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		ReportsHelper.openReport(browserWindow, sqlAPModel);
		ReportHelper.assertReportIsOpen(browserWindow, sqlAPModel);
		ReportHelper.dragDropPerspective(browserWindow, "Measures", Resource.getMessage("GRID_LAYOUTER_AXIS_FREE"));
		ReportHelper.setFilter(browserWindow, "Measures",Resource.getMessage("MEMBER_SELECTOR_ID"), null, "NUMBER");
		ReportHelper.dragDropPerspective(browserWindow, Resource.getMessage("MEMBER_SELECTOR_ID"), Resource.getMessage("GRID_LAYOUTER_AXIS_COLUMN"));
		ReportHelper.checkCellValue(browserWindow, "A1", "f", 3);
		ReportHelper.checkCellValue(browserWindow, "A2", "3.00", 3);*/
		
		navigateTo(Resource.getMessage("MENU_REPORT"));
		//leaveWithoutSave();
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		ReportsHelper.openReport(browserWindow, apModel);
		ReportHelper.assertReportIsOpen(browserWindow, apModel);
		ReportHelper.dragDropPerspective(browserWindow, "Measures", Resource.getMessage("GRID_LAYOUTER_AXIS_FREE"));
		ReportHelper.setFilter(browserWindow, "Measures",Resource.getMessage("MEMBER_SELECTOR_ID"), null, "NUMBER");
		ReportHelper.dragDropPerspective(browserWindow, Resource.getMessage("MEMBER_SELECTOR_ID"), Resource.getMessage("GRID_LAYOUTER_AXIS_COLUMN"));
		ReportHelper.checkCellValue(browserWindow, "A1", "f", 3);
		ReportHelper.checkCellValue(browserWindow, "A2", parseDecimal("3.00", ScenarioTest.language), 3);
		logout();
		
		login(tenant1Admin, tenantPassword,tenant1);
		navigateTo(Resource.getMessage("MENU_USERS"),Resource.getMessage("MENU_USERS_DEFINE_ROLES"));
		RolesHelper.switchRole(browserWindow, roleName);
		//RolesHelper.tickAnalyticPrivilege(browserWindow, "EE_ABC");
		RolesHelper.tickAnalyticPrivilege(browserWindow, "test.infra/DD_ABC");
		RolesHelper.saveRole(browserWindow);
		getFPAMessageArea().waitForMessage(Resource.getMessage("ROLE_SAVE_SUCCESSFULLY_MSG"),INFO_TYPE.SUCCESS);
		logout();
		
		
		// assert if the row level DAC working or not
		login(loginLee,tenantPassword,tenant1);
		/*navigateTo(Resource.getMessage("MENU_REPORT"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		ReportsHelper.openReport(browserWindow, sqlAPModel);
		ReportHelper.assertReportIsOpen(browserWindow, sqlAPModel);
		ReportHelper.dragDropPerspective(browserWindow, "Measures", Resource.getMessage("GRID_LAYOUTER_AXIS_FREE"));
		ReportHelper.setFilter(browserWindow, "Measures",Resource.getMessage("MEMBER_SELECTOR_ID"), null, "NUMBER");
		ReportHelper.dragDropPerspective(browserWindow, "ID", Resource.getMessage("GRID_LAYOUTER_AXIS_COLUMN"));
		ReportHelper.checkCellValue(browserWindow, "A1", "a", 3);
		ReportHelper.checkCellValue(browserWindow, "A2", "22.00", 3);*/
		
		navigateTo(Resource.getMessage("MENU_REPORT"));
		//leaveWithoutSave();
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		ReportsHelper.openReport(browserWindow, apModel);
		ReportHelper.assertReportIsOpen(browserWindow, apModel);
		ReportHelper.dragDropPerspective(browserWindow, "Measures", Resource.getMessage("GRID_LAYOUTER_AXIS_FREE"));
		ReportHelper.setFilter(browserWindow,"Measures", Resource.getMessage("MEMBER_SELECTOR_ID"), null, "NUMBER");
		ReportHelper.dragDropPerspective(browserWindow, "ID", Resource.getMessage("GRID_LAYOUTER_AXIS_COLUMN"));
		ReportHelper.checkCellValue(browserWindow, "A1", "d", 3);
		ReportHelper.checkCellValue(browserWindow, "A2", parseDecimal("2.00", ScenarioTest.language), 3);
		ReportHelper.checkCellValue(browserWindow, "B1", "e", 3);
		ReportHelper.checkCellValue(browserWindow, "B2", parseDecimal("2.00", ScenarioTest.language), 3);
		ReportHelper.checkCellValue(browserWindow, "C1", "f", 3);
		ReportHelper.checkCellValue(browserWindow, "C2", parseDecimal("3.00", ScenarioTest.language), 3);
		logout();
				
				
		
	}
	
}
		
