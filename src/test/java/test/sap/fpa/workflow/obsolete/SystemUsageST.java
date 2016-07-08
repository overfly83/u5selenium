package test.sap.fpa.workflow.obsolete;

import org.junit.Test;

import test.sap.fpa.console.ConsoleHelper;

import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.ScenarioTest;

public class SystemUsageST extends ScenarioTest {
	static final String loginAdmin = "SYSTEM";
	static final String loginAdminPwd = "manager";
	@Test
	public void  SystemUsageMain() {
		loginConsole(loginAdmin, loginAdminPwd);
		ConsoleHelper.gotoTab(browserWindow,Resource.getMessage("SYSTEM_USAGE"));
		ConsoleHelper.waitOverviewLoaded(browserWindow);
		ConsoleHelper.assertTotalUser(browserWindow);
		ConsoleHelper.assertSystemAllocation(browserWindow);
		logoutConsole();
	}
}