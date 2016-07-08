package test.sap.fpa.workflow;

import java.io.File;
import java.util.Date;

import org.junit.Test;

import com.sap.ui5.selenium.sap.fpa.ui.control.shell.ShellInfoMessage.INFO_TYPE;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;
import com.sap.ui5.selenium.utilities.ScenarioTest;

import test.sap.fpa.deployment.ExportHelper;
import test.sap.fpa.deployment.ImportHelper;
import test.sap.fpa.library.LibraryHelper;
import test.sap.fpa.modeling.ModelingHelper;
import test.sap.fpa.report.ReportsHelper;

public class ExportImportST extends ScenarioTest {
	static final String tenant1Admin = "ADMIN";
	static final String tenant2Admin = "ADMIN";
	static final String tenantPassword = "Abcd1234";
	static final String tenantIniPwd = "Initial1";
	static final String tenant1 = "TA1";
	static final String tenant2 = "TA2";
	String exportName = "EXPORT" + new Date().getTime();
	static final String preparedData = "EXPORTIMPORT_TEST1";
	static final String modelName = "AExportImport_Model_Test";
	static final String reportName = "0ExportImport_Report_Test";
	static final String perspectiveName = "AExportImport_Perspective_Test";
	static final String fileName = "0ExportImport_File_Test";
	String preparedDataPath = getResourceFolder() + "Library"+File.separator + preparedData+".tgz";
	
	
	
	@Test
	public void ExportImportScenarioTest(){
		
		login(tenant1Admin, tenantPassword,tenant1);
		
		//delete import file to support rerun
		navigateTo(Resource.getMessage("MENU_REPORT"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		//ReportsHelper.gotoTab(browserWindow, Resource.getMessage("REPORTS_MNU_ALL"));
		if (ReportsHelper.checkReportExist(browserWindow, reportName)){
			ReportsHelper.selectReport(browserWindow, Resource.getMessage("CONTENTLIB_NAME"), reportName);
			ReportsHelper.delete(browserWindow);
			this.getFPAMessageArea().waitForMessage(Resource.getMessage("CONTENTLIB_SELECTED_DELETED"),INFO_TYPE.SUCCESS);
		}
		
		navigateTo(Resource.getMessage("MENU_LIBRARY"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		if (LibraryHelper.deleteObjects(browserWindow, fileName)){
			this.getFPAMessageArea().waitForMessage(Resource.getMessage("CONTENTLIB_SELECTED_DELETED"),INFO_TYPE.SUCCESS);
		}
		
		navigateTo(Resource.getMessage("MENU_MODEL_MODELER"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		if (ModelingHelper.deleteModels(browserWindow, modelName)){
			this.getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_DELETE_MODEL_SUCCESS", modelName),INFO_TYPE.SUCCESS);
		}
		ModelingHelper.goToPerspectivesTab(browserWindow);
		if (ModelingHelper.deletePerspectivesByName(browserWindow, perspectiveName)){
			this.getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_DELETE_DIMEN_SUCCESS", perspectiveName),INFO_TYPE.SUCCESS);
		}
		
		
		//import file from local
		navigateTo(Resource.getMessage("MENU_LM"), Resource.getMessage("MENU_LM_IMPORT"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		ImportHelper.uploadFile(browserWindow, preparedDataPath);
		navigateTo(Resource.getMessage("MENU_LM"), Resource.getMessage("MENU_LM_IMPORT"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		ImportHelper.assertItemExists(browserWindow, preparedData, true);
		ImportHelper.openObject(browserWindow, preparedData);
		ImportHelper.checkImportButtonEnabled(browserWindow);
		ImportHelper.assertsubItemExists(browserWindow, reportName + " (Report)", true);

		//export file to service
		navigateTo(Resource.getMessage("MENU_LM"), Resource.getMessage("MENU_LM_EXPORT"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		ExportHelper.createExport(browserWindow, exportName, 
				new String[] {Resource.getMessage("OBJECT_TYPE_CUBE")+"/" + modelName,
				Resource.getMessage("LIBRARY")+"/"+Resource.getMessage("CONTENTLIB_PUBLIC")+"/"+ reportName + " ("+Resource.getMessage("CONTENTLIB_REPORT")+")",				
				Resource.getMessage("LIBRARY")+"/"+Resource.getMessage("CONTENTLIB_PUBLIC")+"/" + fileName + " ("+Resource.getMessage("CONTENTLIB_FOLDER")+")"}, "local");
		
		String exprtedDataPath = 
				System.getProperty("user.home")+ File.separator+"Downloads"+File.separator+"FPA_EXPORT_T_"+getTenantId()+"_"+exportName+"-sap.com.tgz";
		
		
		logout();
		

		
		/*//delete import file
		navigateTo(Resource.getMessage("MENU_REPORT"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		//ReportsHelper.gotoTab(browserWindow, Resource.getMessage("REPORTS_MNU_ALL"));
		ReportsHelper.selectReport(browserWindow, Resource.getMessage("CONTENTLIB_NAME"), reportName);
		ReportsHelper.delete(browserWindow);
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("CONTENTLIB_SELECTED_DELETED"),INFO_TYPE.SUCCESS);
		
		navigateTo(Resource.getMessage("MENU_LIBRARY"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		LibraryHelper.deleteObjects(browserWindow, fileName);
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("CONTENTLIB_SELECTED_DELETED"),INFO_TYPE.SUCCESS);
	
		navigateTo(Resource.getMessage("MENU_MODEL_MODELER"));
		ModelingHelper.goToModelsTab(browserWindow);
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		ModelingHelper.deleteModels(browserWindow, modelName);
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_DELETE_MODEL_SUCCESS", modelName),INFO_TYPE.SUCCESS);
		ModelingHelper.goToPerspectivesTab(browserWindow);
		ModelingHelper.deletePerspectivesByName(browserWindow, perspectiveName);
		this.getFPAMessageArea().waitForMessage(Resource.getMessage("MSG_DELETE_DIMEN_SUCCESS", perspectiveName),INFO_TYPE.SUCCESS);
		*/
		//import file from service
		login(tenant2Admin, tenantPassword,tenant2);
		navigateTo(Resource.getMessage("MENU_LM"), Resource.getMessage("MENU_LM_IMPORT"));
		
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		
		ImportHelper.uploadFile(browserWindow, exprtedDataPath);
		
		//check the existence of import file
		navigateTo(Resource.getMessage("MENU_REPORT"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		//ReportsHelper.gotoTab(browserWindow, Resource.getMessage("REPORTS_MNU_ALL"));
		ReportsHelper.checkReportExist(browserWindow, reportName);
		navigateTo(Resource.getMessage("MENU_LIBRARY"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		LibraryHelper.checkItemExist(browserWindow, fileName);
		navigateTo(Resource.getMessage("MENU_MODEL_MODELER"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		ModelingHelper.checkModelExist(browserWindow, modelName);
		ModelingHelper.goToPerspectivesTab(browserWindow);
		ModelingHelper.checkPerspectiveExist(browserWindow, perspectiveName);
			
		logout();
	
	}
	
	
}
