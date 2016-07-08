package test.sap.fpa.workflow.obsolete;

import org.junit.Test;

import com.sap.ui5.selenium.sap.fpa.ui.control.shell.ShellInfoMessage.INFO_TYPE;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;
import com.sap.ui5.selenium.utilities.ScenarioTest;

import test.sap.fpa.system.AdministrationHelper;
import test.sap.fpa.modeling.ModelingHelper;
import test.sap.fpa.connection.ConnectionHelper;
import test.sap.fpa.dataintegration.DataIntegrationNewHelper;


public class BWConnectST extends ScenarioTest {
	static final String tenant1Admin = "ADMIN";
	static final String tenantPassword = "Abcd1234";
	static final String tenantIniPwd = "Initial1";
	static final String tenant1 = "TA1";
	static final String hostName = "shgtgvmwin073.dhcp.pgdev.sap.corp";
	static final String port = "8080";
	static final String DBUser="SYSTEM";
	static final String DBUserPwd="manager";
	static final String connectionName="CONNECTION";
	static final String R3Name="DKA";
	static final String applicationName="lu50802361.dhcp.pvgl.sap.corp";
	static final String sysNum="63";
	static final String clientID="500";
	static final String lanaguage="EN";
	static final String queryName="QUERY";
	static final String selectedQuery="CensusByNeighborhood";
	
	@Test
	public void BWConnectSenarioTest() throws Exception{	

		this.login(tenant1Admin, tenantPassword,tenant1);
		navigateTo(Resource.getMessage("MENU_DM_CONNECTOR"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		if(ConnectionHelper.selectConnections(browserWindow, "CONNECTION")){
			ConnectionHelper.deleteConnections(browserWindow, "CONNECTION");
			Thread.sleep(3000);//Because no message show up
		}
		
		navigateTo(Resource.getMessage("MENU_SYSTEM"),Resource.getMessage("MENU_SYSTEM_ADMIN"));
		AdministrationHelper.gotoTab(browserWindow, Resource.getMessage("CONNECTION"));
		AdministrationHelper.setCloudConnection(browserWindow, hostName, port, DBUser, DBUserPwd);
		getFPAMessageArea().waitForMessage(Resource.getMessage("SAML_SAVE_CONNECTION_SUCCESS_TEXT"),INFO_TYPE.SUCCESS);
		navigateTo(Resource.getMessage("MENU_MODEL_MODELER"));
		SapUi5Utilities.waitWhenPageBusy(browserWindow.driver());
		ModelingHelper.selectOtherModelFromBWViaNewConnection(browserWindow, connectionName, R3Name, applicationName, sysNum, clientID, lanaguage, tenant1Admin, tenantPassword, queryName, selectedQuery);
		DataIntegrationNewHelper.mapColumnForNewModel(browserWindow, 1, "HEADER1", "KeyFigure", "SignedData");
		DataIntegrationNewHelper.mapColumnForNewModel(browserWindow, 2, "HEADER2", "KeyFigure", "SignedData");
		DataIntegrationNewHelper.setFirstRowAsColumnHeadersBySelectColumn(browserWindow, 0, true);
		//DataIntegrationNewHelper.replaceElementInColumnView(browserWindow, 0, "Z'berg Park", "ZbergPark",true);
		//SapUi5Utilities.waitUntilBlockLayerDisappear(browserWindow.driver(), 600);
		DataIntegrationNewHelper.finishMapping(browserWindow);
		SapUi5Utilities.waitUntilBlockLayerDisappear(browserWindow.driver());
		this.logout();
	}
		
}
