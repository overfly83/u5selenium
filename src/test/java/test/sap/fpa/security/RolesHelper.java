package test.sap.fpa.security;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.commons.CheckBox;
import com.sap.ui5.selenium.commons.ComboBox;
import com.sap.ui5.selenium.commons.RadioButton;
import com.sap.ui5.selenium.commons.TextField;
import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.sap.fpa.ui.control.commons.Input;
import com.sap.ui5.selenium.sap.fpa.ui.control.memberselector.MemberSelector;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarItem;
import com.sap.ui5.selenium.sap.fpa.ui.control.repeat.Repeat;
import com.sap.ui5.selenium.sap.fpa.ui.control.tabContainer.TabContainer;
import com.sap.ui5.selenium.sap.m.Dialog;
import com.sap.ui5.selenium.sap.m.Popover;
import com.sap.ui5.selenium.sap.m.SearchField;
import com.sap.ui5.selenium.sap.ui.table.Table;
import com.sap.ui5.selenium.sap.ui.table.TreeTable;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.SapUi5Factory;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;

public class RolesHelper {
	

	/**
	 * 
	 * @param role
	 */
	public static void switchRole(BrowserWindow bws, String role) {
		Repeat rpt = Repeat.findRepeatByIdEndsWith(bws, "repeat");
		rpt.clickRow(role);
	}

	public static void addRole(BrowserWindow bws, String rolename) {
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "addRole-btn")).click();
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "addRoleDlg");
		((TextField) dlg.getUI5ObjectByIdEndsWith("txtfield-roleName")).setValue(rolename,false);
		dlg.ok();
		
	}
	
	public static boolean deleteRoles(BrowserWindow bws, String... rolename){
		Repeat rpt = Repeat.findRepeatByIdEndsWith(bws, "repeat");
		if(!rpt.selectRows(rolename)){
			return false;
		}
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "delRole-btn")).click();
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "confirmRemoveRoleDlg");
		SapUi5Utilities.waitUntilMessageDisappear(bws.driver());
		dlg.ok();
		return true;
	}

	public static void saveRole(BrowserWindow bws) {
		SapUi5Utilities.waitUntilMessageDisappear(bws.driver());
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "role-save-btn")).click();
	}
	public static void assignRole(BrowserWindow bws,String role, String...users){
		switchRole(bws,role);
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "assign-user-btn")).click();
		MemberSelector  ms =  SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "user-selector-dialog");
		ms.multiSearch(Resource.getMessage("MEMBER_SELECTOR_ID"), users);
		ms.ok();
		
	}
	public static void refreshRequests(BrowserWindow bws){
		NewToolbarItem refresh = SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "request-refresh-btn");
		refresh.click();
		Table tb = SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "request-grid");
		tb.waitUntilTableInitialized();
	}
	public static void sendRequest(BrowserWindow bws, String Role, String RoleType, String... comment) {
		String radioBtnId = RoleType.equalsIgnoreCase("self")
				?"request_account_self_service_role_radio":"request_account_default_role_radio";
		Dialog dlg = SapUi5Factory.findUI5ObjectById(bws, "request_account_dialog", 10);
		RadioButton radioBtn = dlg.getUI5ObjectByIdEndsWith(radioBtnId);
		Table tb =  dlg.getUI5ObjectByIdEndsWith("request_account_table");
		SapUi5Utilities.waitWhenPageBusy(bws.driver());
		radioBtn.select();
		tb.waitUntilTableInitialized();
		tb.assertItemExist(Resource.getMessage("ROLES"), Role, true);
		tb.selectRowByColumn(Resource.getMessage("ROLES"), Role);
		if(comment!=null && comment.length==1) {
			Input iptComment = dlg.getUI5ObjectByIdEndsWith("request_account_comment_field");
			iptComment.setValue(comment[0], false);
		}
		dlg.ok();
	}
	/**
	 * 
	 * @param requestAttribute
	 * 				locate request by which Attribute
	 * @param attributeValue
	 * 				value of the specified attribute
	 * @return approveOrReject
	 */
	public static void processRequest(BrowserWindow bws, String requestAttribute, String attributeValue, String approveOrReject, String... reason) {
		String toolBarId = "";
		String dlgId = "";
		if(approveOrReject.equalsIgnoreCase("approve")) {
			toolBarId = "approve-btn";
			dlgId = "request-approve-dialog";
		}
		if(approveOrReject.equalsIgnoreCase("reject")) {
			toolBarId = "reject-btn";
			dlgId = "request-reject-dialog";
		}
		SapUi5Utilities.waitWhenPageBusy(bws.driver());
		refreshRequests(bws);
		Table tb = SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "request-grid");
		tb.waitUntilTableInitialized();
		tb.selectRowByColumn(requestAttribute, attributeValue);
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bws, toolBarId)).click();
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bws, dlgId);
		if(reason!=null && reason.length==1 && approveOrReject.equalsIgnoreCase("reject")) {
			((Input)dlg.getUI5ObjectByIdEndsWith("request-reject-reason-field")).setValue(reason[0], false);
		}
		dlg.ok();
	}
	/**
	 * 
	 * @param role
	 * @param RoleDisplayMode
	 * @return
	 */
	public static Boolean checkRoleExists(BrowserWindow bws, String role) {
		Repeat rpt = Repeat.findRepeatByIdEndsWith(bws, "repeat");
		return rpt.checkItemExist(role);
	}

	public static void assertRoleExists(BrowserWindow bws, String role, boolean expected) {
		Repeat rpt = Repeat.findRepeatByIdEndsWith(bws, "repeat");
		rpt.assertItemExist(role, expected);
	}
	
	public static void setPermissions(BrowserWindow bw, String name, String... securityControls){
		TreeTable profileTree = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "task-profile-table");
		profileTree.collapseAllNodes();
		profileTree.tickCheckBoxInLine(Resource.getMessage("NAME"), name, securityControls);
		
	}
	
	/**
	 * 
	 * @param tabName
	 * @param accessName
	 *            should be "read" or "write"
	 * @param attribute
	 *            should be the full text of attribute
	 * @param operator
	 *            should be the full text of
	 * @param value
	 * @throws InterruptedException
	 */
	public static void addLimitedAccess(final BrowserWindow bws, final String model, String accessName, String[] attribute,
		String[] operator, String[] value){

		new WebDriverWait(bws.driver(), 60).until(new ExpectedCondition<Boolean>(){

			public Boolean apply(WebDriver input) {
				try{
					SapUi5Factory.findWebElementByLocator(bws, By.xpath("//span[contains(@class,'sapEpmUiTabContainerTabActionIcon ')]"),2).click();
					Thread.sleep(500);
					Popover pop = SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "selectModel-popover",2);
					SearchField sf = pop.getUI5Object(By.className("sapMSF"),2);
					sf.clearValue();
					sf.setValue(model);
					sf.triggerSearch();
					try{
						pop.clickWebElement(By.xpath(".//label[text()='"+model+"']"),2);
					}catch(Exception e){
						//if not defined before click select new model instead
						pop.clickWebElement(By.xpath(".//label[text()='"+Resource.getMessage("SELECT_NEW_MODEL")+"']"),2);
						Thread.sleep(500);
						Dialog dlg = SapUi5Factory.findUI5ObjectByClass(bws, "sapMDialog",2);
						
						sf = dlg.getUI5ObjectByIdEndsWith("addNewModelSearchField",2);
						sf.clearValue();
						sf.setValue(model);
						sf.triggerSearch();
						dlg.clickWebElement(By.xpath(".//label[text()='"+model+"']"),2);
						dlg.ok();
					}
					return true;
				}catch(Exception e){
					//e.printStackTrace();
					return false;
				}
			}
			
		});
		
		
		
		SapUi5Utilities.waitWhenPageBusy(bws.driver());
		((RadioButton)SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "-limitedAccessRadio")).select();

		
		if (accessName.equalsIgnoreCase("READ")) {
			com.sap.ui5.selenium.commons.Button AddBtn = SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "-dataRegion-addReadAccessBtn");
			AddBtn.click();
		} else if (accessName.equalsIgnoreCase("WRITE")) {
			com.sap.ui5.selenium.commons.Button AddBtn = SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "-dataRegion-addWriteAccessBtn");
			AddBtn.click();
		}
		
		SapUi5Utilities.waitWhenPageBusy(bws.driver());
		Dialog accessDialog = SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "-editDataRegion-dialog");
		final String dlgid = accessDialog.getId();
		new WebDriverWait(bws.driver(),10).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				return !webdriver.findElement(By.id(dlgid)).getAttribute("class").contains("sapUiLocalBusy");
			}
		});
		
		for(int i=0;i<attribute.length;i++){
			((com.sap.ui5.selenium.commons.Button)accessDialog.getUI5ObjectByIdEndsWith("-addDataRegionBtn")).click();

			((ComboBox)accessDialog.getUI5ObjectByIdEndsWith("-col0-row"+i)).setValue(attribute[i], false);

			((ComboBox)accessDialog.getUI5ObjectByIdEndsWith("-col1-row"+i)).setValue(operator[i], false);


			WebElement input = accessDialog.getWebElementByIdEndsWith("-col2-row"+i+"-inner");
			input.click();
			input.sendKeys(value[i]);
		}

		accessDialog.ok();
	}
	
	
	public static void tickAnalyticPrivilege(BrowserWindow bw, String analyticPrivilege){
		TabContainer tb = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "roleMgmt-tab-container");
		tb.clickTab(Resource.getMessage("ANALYTIC_PRIVILEGE_TAB_TITLE"));
		Table apTable = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "ap-table");
		
		apTable.tickCell(Resource.getMessage("ANALYTIC_PRIVILEGE_INFO"), Resource.getMessage("ANALYTIC_PRIVILEGE"), analyticPrivilege);
		
	}
	public static void unTickAnalyticPrivilege(BrowserWindow bw, String analyticPrivilege){
		TabContainer tb = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "roleMgmt-tab-container");
		tb.clickTab(Resource.getMessage("ANALYTIC_PRIVILEGE_TAB_TITLE"));
		Table apTable = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "ap-table");
		
		apTable.unTickCell(Resource.getMessage("ANALYTIC_PRIVILEGE_INFO"), Resource.getMessage("ANALYTIC_PRIVILEGE"), analyticPrivilege);
		
	}
	/**
	 * 
	 * @param selfservice
	 * @param defaultRole
	 * @param approver
	 *            MANAGER or other certain approver: need to specified here
	 */
	public static void setSetting(BrowserWindow bws, boolean selfservice, boolean defaultRole, boolean fullaccess, String... approver) {
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "setting-btn")).click();
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bws, "-setting-dialog");
		RolesHelper._setSetting(bws, dlg, selfservice, defaultRole);
		CheckBox full = dlg.getUI5ObjectByIdEndsWith("forFullDataAccess-checkbox");
		if(fullaccess){
			if(!full.isChecked())full.check();
		}else{
			if(full.isChecked())full.uncheck();
		}
		
		if(approver.length>0){
			RolesHelper._setApprover(bws, dlg, approver);
		}
		dlg.ok();
	}

	private static void _setApprover(final BrowserWindow bws, Dialog dlg, String... approver) {
		RadioButton approverrbtn = null;
		if (approver[0].equalsIgnoreCase("MANAGER")) {
			approverrbtn = dlg.getUI5ObjectByIdEndsWith("-approver-manager-radio");
			if(!approverrbtn.isSelected()) approverrbtn.select();
		} else {
			approverrbtn = dlg.getUI5ObjectByIdEndsWith("-approver-others-radio");
			if(!approverrbtn.isSelected()) approverrbtn.select();

			WebElement approvers = SapUi5Factory.findWebElementByIdEndsWith(bws, "-approver-others-valueHelper-input");
			approvers.clear();
			String approverList ="";
			for(int i=0;i<approver.length;i++){
				approverList = approverList+","+approver[i];
			}
			approverList = approverList.replaceFirst(",", "");
			
			approvers.sendKeys(approverList);
		}
	}

	
	private static void _setSetting(BrowserWindow bws, Dialog dlg, boolean selfservice, boolean defaultrole) {
		CheckBox selfService = dlg.getUI5ObjectByIdEndsWith("-forSelfService-checkbox");

		CheckBox defaultRole = dlg.getUI5ObjectByIdEndsWith("-forDefaultRole-checkbox");

		if(selfservice){
			if(!selfService.isChecked())selfService.check();
		}else{
			if(selfService.isChecked())selfService.uncheck();
		}
		
		if (defaultrole){
			if(!defaultRole.isChecked())defaultRole.check();
		}else{
			if(defaultRole.isChecked())defaultRole.uncheck();
		}
	}
	
	/**
	 * assert whether NavigationMenu works as the role designed.
	 * @param bws
	 * @param menu  example: Users/Roles,  Users
	 * @param expected
	 */
	/*public static void assertTaskAccessInNavigationMenu(BrowserWindow bws, String menu, boolean expected){
		Navigation navi = SapUi5Factory.findUI5ObjectById(bws, "HomeNavigation");
		String[] menus = menu.split("/");
		navi.assertItemVisible(menus[0], expected);
		if (menus.length == 2){
			navi.assertSubItemVisible(menus[0], menus[1], expected);
		}
	}*/
	

	
}
