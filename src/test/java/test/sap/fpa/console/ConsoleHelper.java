package test.sap.fpa.console;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.commons.Button;
import com.sap.ui5.selenium.commons.Tab;
import com.sap.ui5.selenium.commons.TabStrip;
import com.sap.ui5.selenium.commons.TextField;
import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.sap.fpa.ui.control.commons.Input;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarItem;
import com.sap.ui5.selenium.sap.m.Dialog;
import com.sap.ui5.selenium.sap.m.RadioButton;
import com.sap.ui5.selenium.sap.m.SearchField;
import com.sap.ui5.selenium.sap.m.Switch;
import com.sap.ui5.selenium.sap.ui.table.Table;
import com.sap.ui5.selenium.utilities.SapUi5Factory;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;

public class ConsoleHelper {

	
	public static void gotoTab(BrowserWindow bw,String tabName){

		TabStrip tabbar = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "tabStrip");
		List<Tab> tabs = tabbar.getTabs();
		for(Tab tab:tabs){
			if(tab.getText().equalsIgnoreCase(tabName)){
				tabbar.selectTab(tab);
			}
		}
		
	}
	
	public static void addTenant(BrowserWindow bw,String tenant,String tenantAdminPassword){
		NewToolbarItem addbtn = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "addTenant-btn");
		addbtn.click();
		Dialog addTenantDlg = SapUi5Factory.findUI5ObjectByClass(bw, "sapEpmUiDialog");
		Input tenantname = addTenantDlg.getUI5ObjectByIdEndsWith("tenantName");
		tenantname.setValue(tenant);
		Input tenantPasswordInput = addTenantDlg.getUI5ObjectByIdEndsWith("tenantPwd");
		tenantPasswordInput.setValue(tenantAdminPassword, false);
		Input tenantconfirmPasswordInput = addTenantDlg.getUI5ObjectByIdEndsWith("tenantConfirmPwd");
		tenantconfirmPasswordInput.setValue(tenantAdminPassword, false);
		
		addTenantDlg.ok();
		SapUi5Utilities.waitUntilBlockLayerDisappear(bw.driver(),300);
	}
	
	public static void setTenantPackage(BrowserWindow bw,String tenant,String packagename){
		Table tenantTable = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "tenantMgnt-table");
		tenantTable.clickCell("Name", tenant);
		
		Dialog tenantConfig = SapUi5Factory.findUI5ObjectByClass(bw, "sapMDialog");
		Table configTable = tenantConfig.getUI5ObjectByIdEndsWith("configTenant-table");
		configTable.inputCell("Name", "AUTHORIZED_HANA_PACKAGES", "Value", packagename);
		tenantConfig.ok();
	}
	
	public static boolean checkTenantExists(BrowserWindow bw, String tenant){
		Table tenantList = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "tenantMgnt-table");
		return tenantList.checkItemExist("Name", tenant);
	}
	
	public static boolean multiSelectTanants(BrowserWindow bw, String...tenants){
		Table tenantList = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "tenantMgnt-table");
		return tenantList.multiSelectByColumn("Name", tenants);
	}
	
	public static void deleteTenants(BrowserWindow bw, String...tenants){
		NewToolbarItem delete = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "delTenant-btn");
		delete.click();
		Dialog confirmDelete = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "confirmRemoveTenantDlg");
		confirmDelete.ok();
		SapUi5Utilities.waitUntilBlockLayerDisappear(bw.driver(),300);
	}
	
	
	public static void waitOverviewLoaded(final BrowserWindow bw) {
		new WebDriverWait(bw.driver(), 30).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					webdriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
					By highchartContainer = By.xpath("//div[@class='highcharts-container']");
					List<WebElement> pieContianers = webdriver.findElements(highchartContainer);
					if(pieContianers.size() >= 3 ){
						webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
						return true;
					}else{
						webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
						return false;
					}
				}catch(Exception e){
					e.printStackTrace();
					webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					return false;
				}
			}
		});
	}
	public static void setSystemConfiguration(BrowserWindow bw,String configName,Object value) {
		SearchField sf = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "configSearchField");
		sf.clearValue();
		sf.setValue(configName);
		sf.triggerSearch();
		Table featureTable = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-configs-table");
		if(value.getClass().equals(java.lang.Boolean.class)){
			Switch featureSwitch = featureTable.getUi5ObjectFromCell("Name", configName, "Value", "sapMSwtCont");
			if(Boolean.valueOf(value.toString())){
				featureSwitch.switchOn();
			}else{
				featureSwitch.switchOff();
			}
			
		}else if(value.getClass().equals(java.lang.String.class)||value.getClass().equals(java.lang.Integer.class)){
			TextField input = featureTable.getUi5ObjectFromCell("Name", configName, "Value", "sapUiTf");
			input.setValue(value.toString());
		}
		Button save = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "configSaveBtn");
		save.click();
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
	}
	
	public static void turnOnToggle(BrowserWindow bw,String featureName) {
		RadioButton rd = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "featureShowAllRadio");
		rd.select();
		
		SearchField sf = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "featureSearchField");
		sf.clearValue();
		sf.setValue(featureName);
		sf.triggerSearch();
		
		Table featureTable = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "features-table");
		Switch featureSwitch = featureTable.getUi5ObjectFromCell("Feature", featureName, "Off/On", "sapMSwtCont");
		featureSwitch.switchOn();
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
	}
	
	public static void turnOffToggle(BrowserWindow bw,String featureName) {
		RadioButton rd = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "featureShowAllRadio");
		rd.select();
		
		SearchField sf = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "featureSearchField");
		sf.clearValue();
		sf.setValue(featureName);
		sf.triggerSearch();
		
		Table featureTable = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "features-table");
		Switch featureSwitch = featureTable.getUi5ObjectFromCell("Feature", featureName, "Off/On", "sapMSwtCont");
		featureSwitch.switchOff();
	}
	
	public static void assertTotalUser(BrowserWindow bw) {
		By byUser = By.xpath("//div[@class='sapEpmUiUserTypeContent']//span");
		List<WebElement> userSpan = SapUi5Factory.findWebElements(bw, byUser);
		int used = Integer.parseInt(userSpan.get(0).getText());
		int total = Integer.parseInt(userSpan.get(1).getText().substring(1));
		Assert.assertTrue("Used user should be positive" , used > 0);
		Assert.assertTrue("Total user should be positive" , total > 0);
		Assert.assertTrue("used user should be no more than total user" , used <= total);
	}
	
	
	public static void assertSystemAllocation(BrowserWindow bw) {
		By linechartContainer = By.xpath("//div[@class='sapEpmUiLineChartBox']//div[contains(@class,'highcharts-container')]");
		List<WebElement> linechartContainers = SapUi5Factory.findWebElements(bw, linechartContainer);
		Assert.assertTrue("Assert hardware usage linechart is initialized",linechartContainers.size()==3);
	}
	
}
