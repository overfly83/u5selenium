/*package com.sap.ui5.selenium.sap.fpa.ui.control.collaboration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.sap.fpa.common.FPAScenarioHelper;
import test.sap.fpa.report.ReportHelper;

import com.google.common.base.Predicate;
import com.sap.ui5.selenium.commons.Button;
import com.sap.ui5.selenium.commons.ComboBox;
import com.sap.ui5.selenium.commons.Label;
import com.sap.ui5.selenium.commons.TextField;
import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.core.Control;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.control.EPMControl;
import com.sap.ui5.selenium.sap.fpa.ui.control.memberselector.MemberSelector;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarItem;
import com.sap.ui5.selenium.sap.fpa.ui.control.repeat.Repeat;
import com.sap.ui5.selenium.sap.fpa.ui.control.shell.Shell;
import com.sap.ui5.selenium.sap.m.CheckBox;
import com.sap.ui5.selenium.sap.m.Dialog;
import com.sap.ui5.selenium.sap.m.Input;
import com.sap.ui5.selenium.sap.ui.commons.SapUi5Factory;
import com.sap.ui5.selenium.sap.ui.commons.SapUi5Utilities;

//import com.sap.ui5.selenium.core.ManagedObject;

*//**
 * @author I054401
 * 
 *         This control will cover the discussion list page and detail page.
 *//*
@SuppressWarnings("deprecation")
public class CollaborationPanel extends Control {

	public CollaborationPanel(String id, Frame frame) {
		super(id, frame);
		logger = LoggerFactory.getLogger("CollaborationPanel_Control");
	}

	public static final int MAX_WAIT_TIME = 10;
	public static final String SEND_COMMENT = "Send Comment";

	public static final String RESOURCE_TASK = "TASK";
	public static final String RESOURCE_REPORT = "REPORT";
	public static final String RESOURCE_DASHBOARD = "DASHBOARD";
	public static final String RESOURCE_CELL = "CELL";
	public static final String RESOURCE_VERSION = "VERSION";

	public static final String RESOURCE_BTN_IN_TOOL_BUTTON = "Resources";
	public static final String PARTICIPANTS_BTN_IN_TOOL_BUTTON = "Participants";
	public static final String START_LIVE_SESSION_BTN_IN_TOOL_BUTTON = "Start Live Session";
	public static final String CREATE_DISCUSSION_BTN_IN_TOOL_BUTTON = "Create Discussion";
	public static final String CLOSE_DISCUSSION_BTN_IN_TOOL_BUTTON = "Close Discussion";
	public static final String LEAVE_DISCUSSION_BTN_IN_TOOL_BUTTON = "Leave Discussion";

	public static final String NEW_TASK_BUTTON_IN_STAFF_MENU = "New Task";
	public static final String SHARE_VERSION_BUTTON_IN_STAFF_MENU = "Share Version";
	public static final String ADD_ATTACHMENT_BUTTON_IN_STAFF_MENU = "Add Attachment";
	public static final String DISCUSS_CELL_BUTTON_IN_STAFF_MENU = "Discull Cell";

	public static final String LINK_BUTTON_STATUS_PIN = "Link to Discussion";
	public static final String LINK_BUTTON_STATUS_UNPIN = "Unlink from Discussion";

	public static final String RESOURCE_FROM_RESOURCE_PAGE = "Open from resource page";
	public static final String RESOURCE_FROM_MESSAGE_LIST = "Open from message list";
	public static final String DISCUSSION_TYPE_ACTIVE = "Active Discussions";
	public static final String DISCUSSION_TYPE_CLOSED = "Close Discussions";

	public static final int MAX_TIME_OUT = 60;

	static Logger logger;
	static BrowserWindow bw;
	public boolean isPanelOpen(int ...timeout) {
		final By byPanel = By.className("sapEpmUiShellCollaborationPane");
		int defaulttimeout = (timeout!=null && timeout.length==1)?timeout[0]:30;
		long start = System.currentTimeMillis();
		while(defaulttimeout*1000 > System.currentTimeMillis() - start) {
			try {
				Wait<WebDriver> wait = new WebDriverWait(bw.syncAjax().driver(), 10).pollingEvery(100, TimeUnit.MILLISECONDS);
				wait.until(new ExpectedCondition<Boolean>(){ 
				    public Boolean apply(WebDriver webDriver) {
				    	try{
				    		WebElement element = webDriver.findElement(byPanel);
				    		return element != null && !element.getCssValue("margin-right").startsWith("-"); 
				    	}catch(Exception e){
				    		return false;
				    	}
				    } 
				});
				return true;
			} catch(Exception e) { 
				return false;
				}
		}
		return false;
	}
	
	public void assertPannelOpen(int ...timeout) {
		Assert.assertTrue(isPanelOpen(timeout));
	}
	public void assertDiscussionTitle(BrowserWindow bw, String expectedTitle) {
		WebElement discussionTitle = SapUi5Factory.findWebElementByIdEndsWith(bw, "collaboration-panel-detail-page-title-label");
		boolean isSameTitle = discussionTitle.getText().equals(expectedTitle);
		Assert.assertTrue(isSameTitle);
	}
	
	public void createDashboard(String dashboardTitle) {
		Shell shell = Shell.findShell(bw);
		NewToolbarItem.findToolbarItemByTitle(bw, "Create New Dashboard", 30).click();
		By byTitle = By.xpath("//div[@class='sapEpmUiFormItem']/label[text()='Title *']/parent::/div[@class='sapEpmUiInputTextArea']");
		By byBtn = By.xpath("//button[@class='sapEpmUiDialogButton sapEpmUiDialogOkButton sapMBarChild sapMBtn']");
		final WebElement title = SapUi5Factory.safeFindWebElement(bw, byTitle, 10);
		final String value = dashboardTitle;
		windowManager.await().until(new Predicate<WebDriver>() {
			public boolean apply(WebDriver driver) {

				title.clear();
				title.sendKeys(value); // set the value

				String realValue = title.getText();
				boolean success = realValue.equals(value);

				if (!success) {
					
				}

				return success;
			}
		});
		SapUi5Factory.safeFindWebElement(bw, byBtn, 30).click();
		shell.waitForMessage(30, "Dashboard saved");
		SapUi5Utilities.waitUntilMessageDisappear(driver);
	}
	
	public void viewDashboard(String dashboardTitle) {
		Repeat rpt = Repeat.findRepeatByIdEndsWith(bw, "-repeat", 30);
		rpt.clickRow(dashboardTitle);
		try {   	//the collaboration panel will trigger dashboard render, so it will refresh after loaded
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void clickDashboardMenuButton(String buttonText) {
		By byMenuButton = By.xpath("//div[@class='sapEpmDashboardMenuButton sapEpmIcon']");
		By byMenuItem = By.xpath("//div[@class='sapUiMnuItmTxt' and text()='"+buttonText+"']");
		SapUi5Factory.safeFindWebElement(bw, byMenuButton, 30).click();
		SapUi5Factory.safeFindWebElement(bw, byMenuItem, 30).click();
	}
	
	public boolean isLinkStatusSame(String status, int ...timeout) {
		final By byLink = By.id("sap-fpa-ui-collaboration-panel-collaboration-title-link-button"); 
		final String expectedStatus = status;
		int defaulttimeout = (timeout!=null && timeout.length==1)?timeout[0]:5;
		long start = System.currentTimeMillis();
		while(defaulttimeout*1000 > System.currentTimeMillis() - start) {
			try {
				Wait<WebDriver> wait = new WebDriverWait(bw.syncAjax().driver(), 10).pollingEvery(100, TimeUnit.MILLISECONDS);
				wait.until(new ExpectedCondition<Boolean>(){ 
				    public Boolean apply(WebDriver webDriver) {
				    	try{
				    		WebElement element = webDriver.findElement(byLink);
				    		return element != null && element.getAttribute("title").equals(expectedStatus); 
				    	}catch(Exception e){
				    		return false;
				    	}
				    } 
				});
				return true;
			} catch(Exception e) { 
				return false;
				}
		}
		return false;
	}
	
	public void waitToAssertLinkStatus(String status, int ...timeout) {
		Assert.assertTrue(isLinkStatusSame(status, timeout));
	}

	public static CollaborationPanel findCollaborationPanel(BrowserWindow bwExternal) {
		bw = bwExternal;
		return SapUi5Factory.findUI5ObjectByClass(bw, "sapEpmUiControlCollaborationPanel");
		
	}

	// ///////////////////////////Action in Detail Page////////////////////////

	*//**
	 * Add New Task or Version or Attachment
	 *//*
	public void clickButtonInAddStaffMenuList(String ButtonText) {
		String staffButtonId = "sap-fpa-ui-collaboration-panel-detail-page-add-staff";
		//By staffButton = By.id(staffButtonId);
		String buttonId = "";
		//int indices = 0;

		if (ButtonText.equals(CollaborationPanel.NEW_TASK_BUTTON_IN_STAFF_MENU)) {
			buttonId = "sap-fpa-ui-collaboration-panel-detail-page-add-task";
			//indices = 2;
		} else if (ButtonText.equals(CollaborationPanel.SHARE_VERSION_BUTTON_IN_STAFF_MENU)) {
			buttonId = "sap-fpa-ui-collaboration-panel-detail-page-add-version";
			//indices = 3;
		} else if (ButtonText.equals(CollaborationPanel.ADD_ATTACHMENT_BUTTON_IN_STAFF_MENU)) {
			buttonId = "sap-fpa-ui-collaboration-panel-detail-page-add-attachment";
			//indices = 1;
		} else {
			buttonId = "sap-fpa-ui-collaboration-panel-detail-page-cell-hight";
			//indices = 4;
		}

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		safeClick(MAX_TIME_OUT, By.id(staffButtonId), "Click Button " + staffButtonId);

		// TODO: click add staff button again if it's missed. it's strange that
		// we always get stale element error for sharing version.
		try {
			safeClick(MAX_TIME_OUT, By.id(buttonId), "Click Button " + buttonId);
			;

		} catch (Exception ex) {
			safeClick(MAX_TIME_OUT, By.id(staffButtonId), "Click Button " + staffButtonId);
			safeClick(MAX_TIME_OUT, By.id(buttonId), "Click Button " + buttonId);
			;
		}

	}

	*//**
	 * Click Back button in detail page
	 *//*
	public void clickBackButton() {
		String backBtnId = "sap-fpa-ui-collaboration-panel-collaboration-title-back-button";
		try {
			// helper.assertElementVisible(By.id(backBtnId));
			// helper.assertClick(helper.assertFindElement(By.id(backBtnId),"click back button"));
			// longWaiting(1);

			safeClick(MAX_TIME_OUT, By.id(backBtnId), "Click Back Button");

		} catch (Exception e) {
			Assert.fail("Error finding element:backBtn in collaboration panel, " + e.getMessage());
		}
	}

	// private void waitWhenPageBusy() {
	// Wait<WebDriver> wait = new WebDriverWait(bw.driver(), 600);
	// wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("sapUiBusyIndicator")));
	// }
	//
	// public void longWaiting(int iSeconds) {
	// waitWhenPageBusy();
	// try {
	// Thread.sleep(iSeconds * 1000);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// waitWhenPageBusy();
	// }

	// check the visibilty

	// count item in a list
	// public int countElementsByXPath(BrowserWindow bw, String text,
	// By expectedElement, int maxSeconds) {
	// try {
	// Wait<WebDriver> wait = new WebDriverWait(bw.driver(), maxSeconds);
	// wait.until(ExpectedConditions
	// .presenceOfAllElementsLocatedBy(expectedElement));
	//
	// List<WebElement> list = bw.findWebElements(expectedElement);
	// return list.size();
	// } catch (TimeoutException e) {
	// Assert.fail("Error counting item in list: '" + text + "' "
	// + e.getMessage());
	// return 0;
	// }
	//
	// }

	// ////////////////////// Web Element in the Collaboration
	// Panel//////////////////////////////////////
	public Button getInvParBtn() {
		return SapUi5Factory.findButtonById(bw, "sap-fpa-ui-collaboration-panel-apbtn");
	}

	public Button getAddParBtn() {
		return SapUi5Factory.findButtonById(bw, "sap-fpa-ui-collaboration-panel-users-page-add-user-btn");
	}

	public MemberSelector getMemberSelector() {
		return MemberSelector.findMemberSelectorById(bw, "sap-fpa-ui-collaboration-panel-member-selector");
	}

	public WebElement getMessageInput() {
		return SapUi5Factory.findWebElementByIdEndsWith(bw, "sap-fpa-ui-collaboration-panel-detail-page-send-msg-box-msg-inputArea");
	}

	public Button getNewDiscBtn() {
		return SapUi5Factory.findButtonById(bw, "sap-fpa-ui-collaboration-panel-collaboration-title-new-button");
	}

	public com.sap.ui5.selenium.sap.fpa.ui.control.commons.Input getDiscNameInput() {
		return SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "sap-fpa-ui-collaboration-panel-title-input");
	}

	public com.sap.ui5.selenium.sap.fpa.ui.control.commons.Input getDiscDescriptionInput() {
		return SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "sap-fpa-ui-collaboration-panel-Desc-input");
	}

	//
	public Button getOKBtn_Create() {
		return SapUi5Factory.findButtonById(bw, "sap-fpa-ui-collaboration-panel-okbtn");
	}

	public Button getSendMsgBtn() {
		return SapUi5Factory.findButtonById(bw, "sap-fpa-ui-collaboration-panel-detail-page-send-msg-box-send-msg-btn");
	}

	public Button getLinkBtn() {
		String linkBtnId = "sap-fpa-ui-collaboration-panel-collaboration-title-link-button";
		new WebDriverWait(bw.driver(), 10).until(ExpectedConditions.visibilityOfElementLocated(By.id(linkBtnId)));
		return SapUi5Factory.findMobileButtonById(bw, linkBtnId);
	}

	public Label getDiscussionTitleError(){
		return SapUi5Factory.findLabelById(bw, "sap-fpa-ui-collaboration-panel-title-check-label");
	}

	public String getLinkBtnStatus() {
		// String linkBtnId =
		// "//button[@id='sap-fpa-ui-collaboration-panel-collaboration-title-link-button']";
		String linkBtnStatus = getLinkBtn().getTooltip_AsString();
		// SapUi5Factory.findMobileButtonById(bw,
		// linkBtnId).getTooltip_AsString();
		// helper.assertFindElement(By.xpath(linkBtnId),
		// "Get Pin Button Status").getAttribute("title");

		return linkBtnStatus;
	}

	public void assertLinkBtnStatus(String expectedStatus) {
		// String linkBtnId = getLinkBtn().getId();
		// helper.assertFindElement(By.xpath("//button[@id='"+linkBtnId+"' and @title='"+expectedStatus+"']"),"Assert Pin Button Status");
		Assert.assertEquals(expectedStatus, getLinkBtnStatus());
	}

	public Dialog getDiscussionCreationDialog() {
		return Dialog.findPopupConfirmDialogById(bw, "sap-fpa-ui-collaboration-panel-creation-ok-warning-dialog");
	}

	public Dialog getShareVersionDialog() {
		return safeCast(10, By.xpath("//label[text()='Choose Version']/ancestor::div[contains(@class,'sapMDialog')]"));
	}

	public List<String> getAtUserList() {

		this.typeMessage("@");
		// TODO: check developer which control is used here.
		SapUi5Factory.safeFindWebElement(bw, By.id("sap-fpa-ui-collaboration-panel-detail-page-send-msg-box-mention-user-list-at-namelist"), 60);
		List<WebElement> nameElementList = SapUi5Factory.findWebElementsByClassName(bw, "userLi");


		List<String> nameList = new ArrayList<String>();
		for (int i = 0; i < nameElementList.size(); i++) {
			nameList.add(nameElementList.get(i).getText());
		}

		return nameList;
	}

	public void typeMessage(String message) {
		SapUi5Utilities.waitWhenPageBusy(bw.driver(), 30);
		WebElement messagebox = this.getMessageInput();
		messagebox.click();
		messagebox.sendKeys(message);
	}

	public void selectParticipantMenu() {
		String buttonId = "sap-fpa-ui-collaboration-panel-tool-menu-participants";
		safeClick(MAX_TIME_OUT, By.id(buttonId), "select buttonn [" + buttonId + "] in tool button)");
		// (helper.assertFindElement(By.id(buttonId),);
	}

	public void selectResourceMenu() {
		String buttonId = "sap-fpa-ui-collaboration-panel-tool-menu-overview";
		safeClick(MAX_TIME_OUT, By.id(buttonId), "select buttonn [" + buttonId + "] in tool button)");
	}

	public void selectDeleteDiscussionMenu() {
		String buttonId = "sap-fpa-ui-collaboration-panel-tool-menu-close";
		safeClick(MAX_TIME_OUT, By.id(buttonId), "select buttonn [" + buttonId + "] in tool button)");
	}

	public void selectCloseDiscussionMenu() {
		String buttonId = "sap-fpa-ui-collaboration-panel-tool-menu-close";
		safeClick(MAX_TIME_OUT, By.id(buttonId), "select buttonn [" + buttonId + "] in tool button)");
	}

	public void selectLeaveDiscussionMenu() {
		String buttonId = "sap-fpa-ui-collaboration-panel-tool-menu-leave";
		safeClick(MAX_TIME_OUT, By.id(buttonId), "select buttonn [" + buttonId + "] in tool button)");
	}

	public void selectRealTimeMenu() {
		String buttonId = "sap-fpa-ui-collaboration-panel-tool-menu-realtime";
		safeClick(MAX_TIME_OUT, By.id(buttonId), "select buttonn [" + buttonId + "] in tool button)");
	}

	public void selectMenuList() {
		String buttonId = "sap-fpa-ui-collaboration-panel-collaboration-title-tool-button";
		safeClick(10, By.id(buttonId), "select buttonn [" + buttonId + "] in tool button)");
	}

	// ********* for Collaboration Share Version Dialog*******************
	public void selectVersion(String verName, boolean isReadOnly) {
		String checkboxXpath = "//label[text()='" + verName + "']//..";
		CheckBox verCheckBox = SapUi5Factory.findMobileCheckBoxByXpath(bw, checkboxXpath);
		verCheckBox.check();

		// TODO: configure isReadOnly
	}

	public void assertShareVersionDialog() {
		Wait<WebDriver> wait = new WebDriverWait(bw.driver(), 600);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sap-fpa-ui-collaboration-panel-detail-page-version-choose-dialog")));
	}

	// ********* for discuss cell***************
	public void commentDiscussCell(String comment) {
		SapUi5Factory.findTextFieldById(bw, "sap-fpa-ui-collaboration-panel-detail-page-comment-textArea").setValue(comment);
		SapUi5Factory.findButtonById(bw, "sap-fpa-ui-collaboration-panel-detail-page-cell-highlight-dialog-begin").click();
	}

	public void validateDiscussCellDialog() {
		Wait<WebDriver> wait = new WebDriverWait(bw.driver(), 600);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sap-fpa-ui-collaboration-panel-detail-page-cell-highlight-dialog")));
	}

	*//**
	 * Get the list including report, version,
	 * 
	 * @param fromWhere
	 * @return
	 *//*

	public List<WebElement> resList(String fromWhere) {

		List<WebElement> resourceList = new ArrayList<WebElement>();
		if (fromWhere.equalsIgnoreCase(RESOURCE_FROM_RESOURCE_PAGE)) {
			// helper.assertElementVisible(By.id("sap-fpa-ui-collaboration-panel-summary-page-refs-rowrepeater"));
			resourceList = SapUi5Factory.findWebElements(bw, By.xpath("//*[@id='sap-fpa-ui-collaboration-panel-summary-page-refs-rowrepeater']//a"));
		} else {
			resourceList = SapUi5Factory.findWebElements(bw, By.xpath("//div[@id='sap-fpa-ui-collaboration-panel-detail-page']//a"));
		}

		return resourceList;

	}

	public List<WebElement> participantsList() {

		com.sap.ui5.selenium.sap.m.List participantList = SapUi5Factory.findUI5ObjectById(bw, "sap-fpa-ui-collaboration-panel-users-page-list");
		participantList.waitToUpdated();		
		List<WebElement> participants =  bw.findWebElement(participantList.getId()).findElements(By.xpath(".//li"));
		return participants;

	}

	*//**
	 * 
	 * @param resourceType
	 *            : "TASK", "REOURCE", or "DASHBOARD"
	 * @param resouceName
	 *            : taskName, reportName or dashboardName
	 * @param addedBy
	 *            : the people who add the resource
	 * @param taskAssigneeName
	 *            : if resourceType !="TASK", set it null or ''
	 * @param isAppeared
	 *            : if suppose msg to be appear, set it 'true'; else set it
	 *            'false'
	 * 
	 *            Validate the link message and added by message
	 *//*
	public void varifyAddResourceMessageInCommentList(String resourceType, String resourceName, String addedBy, String taskAssigneeName, boolean expected) {
		String msgScrollId = "sap-fpa-ui-collaboration-panel-detail-page-chatListBox";
		String msgXpath = "//div[@id='" + msgScrollId + "']/div/div]";
		String actualDisplayMsg = "";
		String expectDisPlayMsg = "";
		boolean msgFound = false;
		boolean linkMsgCorrect = false;
		boolean addByMsgCorrect = false;
		//boolean timeMsgCorrect = true;// TODO
		try {
			Wait<WebDriver> wait = new WebDriverWait(bw.driver(), MAX_WAIT_TIME);
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(msgXpath)));

			List<WebElement> itemList = null;
			List<WebElement> addByList = null;
			//List<WebElement> timeList = null;

			// linkMsg
			if (resourceType.equals(RESOURCE_TASK)) {
				itemList = bw.findWebElements(By.xpath(msgXpath + "/.."));
				addByList = bw.findWebElements(By.xpath(msgXpath + "/../../../div[last()-1]/label[@title]"));
				//timeList = bw.findWebElements(By.xpath(msgXpath + "/../../../div[last()]/label[@title]"));
				expectDisPlayMsg = "A new task " + resourceName + " has been created for " + taskAssigneeName + ".";
			} else if (resourceType.equals(RESOURCE_REPORT)) {
				itemList = bw.findWebElements(By.xpath(msgXpath));
				addByList = bw.findWebElements(By.xpath(msgXpath + "/../../div[last()-1]/label[@title]"));
				//timeList = bw.findWebElements(By.xpath(msgXpath + "/../../div[last()]/label[@title]"));
				expectDisPlayMsg = "REPORT: " + resourceName;
			} else if (resourceType.equals(RESOURCE_DASHBOARD)) {
				itemList = bw.findWebElements(By.xpath(msgXpath));
				addByList = bw.findWebElements(By.xpath(msgXpath + "/../../div[last()-1]/label[@title]"));
				//timeList = bw.findWebElements(By.xpath(msgXpath + "/../../div[last()]/label[@title]"));
				expectDisPlayMsg = "DASHBOARD: " + resourceName;
			} else {// TODO
				itemList = bw.findWebElements(By.xpath(msgXpath));
				expectDisPlayMsg = resourceType + ": " + resourceName;
			}
			WebElement latestMsg = itemList.get(itemList.size() - 1);
			WebElement addedByMsg = addByList.get(itemList.size() - 1);
			//WebElement timeMsg = timeList.get(itemList.size() - 1);// TODO
			actualDisplayMsg = latestMsg.getText();

			msgFound = true;
			linkMsgCorrect = actualDisplayMsg.equals(expectDisPlayMsg);
			addByMsgCorrect = ("Added by " + addedBy).equals(addedByMsg.getText());

		} catch (TimeoutException e) {
			Assert.fail("Error in find the resource message");

		} finally {

			if (expected) {
				Assert.assertTrue("Message found", msgFound);
				Assert.assertTrue("Message found", linkMsgCorrect);
				Assert.assertTrue("Message found", addByMsgCorrect);
			} else {
				boolean flag = (msgFound && linkMsgCorrect && addByMsgCorrect) ? true : false;
				Assert.assertFalse("Message not found", flag);
			}
		}

	}

	*//**
	 * If there are more than one link with the same name, Click the last one,
	 * that is, the latest created one.
	 * 
	 * @param resourceName
	 *//*
	public void clickLatestResourceLinkInCollaboraionPanel(String resourceType, String resourceName) {
		String msgScrollId = "sap-fpa-ui-collaboration-panel-detail-page-chatListBox";
		String text = "";
		if (resourceType.equals(RESOURCE_TASK)) {
			text = resourceName;
		} else if (resourceType.equals(RESOURCE_REPORT)) {
			text = "REPORT: " + resourceName;
		} else if (resourceType.equals(RESOURCE_DASHBOARD)) {
			text = "DASHBOARD: " + resourceName;
		} else {// TODO
			text = resourceType + ": " + resourceName;
		}

		try {
			// Wait<WebDriver> wait = new WebDriverWait(bw.driver(),
			// MAX_WAIT_TIME);
			// wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(msgXpath)));

			String msgXpath = "(//div[@id='" + msgScrollId + "']//ul/li//a[text()='" + text + "'])[last()]";
			// List<WebElement> itemList =
			// bw.findWebElements(By.xpath(msgXpath));
			safeClick(MAX_TIME_OUT, By.xpath(msgXpath));

		} catch (TimeoutException e) {
			Assert.fail("Error in find the resource message");

		}
	}

	*//**
	 * click resource message in the message list to launch different resource.
	 * 
	 * @param resourceType
	 * @param resourceInfo
	 *            Task: 0-> TaskName Report: 0->ReportName Dashboard:
	 *            0->DashboardName Version: 0-> VersionName, 1-> ReportName, 2->
	 *            isReadOnly Cell: 0-> comment
	 *//*

	public void clickLatestResourceLink(String resourceType, Object... resourceInfo) {
		String msgScrollId = "sap-fpa-ui-collaboration-panel-detail-page-chatListBox";
		String text = "";
		if (resourceType.equals(RESOURCE_TASK)) {
			text = resourceInfo[0].toString();
		} else if (resourceType.equals(RESOURCE_REPORT)) {
			text = "REPORT: " + resourceInfo[0].toString();
		} else if (resourceType.equals(RESOURCE_DASHBOARD)) {
			text = "DASHBOARD: " + resourceInfo[0].toString();
		} else if (resourceType.equals(RESOURCE_VERSION)) {

			if (resourceInfo[2].toString() == Boolean.toString(true)) {
				text = "VERSION: " + resourceInfo[0] + " with Read only access"; //resourceInfo[0] + " of " + resourceInfo[1] + ":Read only";
			} else {
				text = "VERSION: " + resourceInfo[0] + " with Edit access"; //resourceInfo[0] + " of " + resourceInfo[1] + ":Edit";
			}

		} else if (resourceType.equals(RESOURCE_CELL)) {
			text = "Discuss cell(s)";//resourceInfo[0].toString();
		} else {// TODO
			text = resourceType + ": " + resourceInfo[0].toString();
		}

		String msgXpath = "(//div[@id='" + msgScrollId + "']//*[contains(text(),'" + text + "')])[last()]";

		try {
			// Wait<WebDriver> wait = new WebDriverWait(bw.driver(),
			// MAX_WAIT_TIME);
			// wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(msgXpath)));

			// List<WebElement> itemList =
			// bw.findWebElements(By.xpath(msgXpath));
			safeClick(MAX_TIME_OUT, By.xpath(msgXpath));

		} catch (TimeoutException e) {
			// Assert.fail("Error in find the resource message: " +
			// e.getMessage());

		}
	}

	public void leaveDiscussion() {
		selectMenuList();
		safeClick(10, By.id("sap-fpa-ui-collaboration-panel-tool-menu-leave"), "Click leave discussion");
		safeClick(10, By.xpath("//*[contains(@class,'sapMBtn') and text()='OK']"));

	}

	public void closeDiscussion() {
		// assertEnterExpectedDisscussion(expectDisscussionName);
		// SapUi5Factory.findMobileButtonByIdEndsWith(bw,
		// "tool-button").click();
		// SapUi5Factory.findMenuByClass(bw).selectItem(2);
		clickButtonInToolList(CollaborationPanel.CLOSE_DISCUSSION_BTN_IN_TOOL_BUTTON);
		Dialog closeConfirmDialog = Dialog.findPopupConfirmDialogById(bw, "sap-fpa-ui-collaboration-panel-confirmCloseDialog");
		closeConfirmDialog.ok();
	}

	
	 * nameList and Creator -> description name
	 
	public void createDiscussion(String discussionName, String discussionDescription, String[] nameList, String Creator) {

		// click create discussion button
		getNewDiscBtn().click();
		getDiscNameInput().setValue(discussionName);
		//getDiscDescriptionInput().clearValue(true); 
		getDiscDescriptionInput().setValue(discussionDescription);

		// click add button to add people
		getInvParBtn().click();
		MemberSelector ms = getMemberSelector();

		for (int i = 0; i < nameList.length; i++) {
			ms.singleSearch("Display Name", nameList[i], nameList[i]);

		}
	
		ms.ok();

		// TODO: following sleep is temp solution: if we don't sleep, the adding
		// user in selenium test won't work. Strange.
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// click OK button to finish discussion creation
		getOKBtn_Create().click();

	}

	public void deleteDisuccsion(String discussionName) {
		By by = By.xpath(String.format("//label[contains(@class,'discussionItemTitle') and .//text()='%s']", discussionName));
		if (safeExists(1, by)) {
			safeClick(2, by);
			safeClickTooltip(2, "Actions");
			selectDeleteDiscussionMenu();
			safeClickButton(2, "OK");
		}
	}
	
	//<div id="sap-ui-static" data-sap-ui-area="sap-ui-static" style="height: 0px; width: 0px; overflow: hidden; float: left;"><div id="sap-ui-blocklayer-popup" tabindex="0" class="sapUiBLy sapMDialogBlockLayerInit" style="z-index: 98; visibility: hidden; display: none;"></div><div tabindex="0" hidefocus="true" aria-orientation="vertical" role="menu" aria-label="Menu" aria-level="1" class="sapEpmMenu sapUiMnu sapUiShd" id="__menu0" data-sap-ui="__menu0" data-sap-ui-popup="id-1418931274064-7" style="position: absolute; visibility: visible; z-index: 110; display: block; top: 76px; right: 12px;"><ul class="sapUiMnuLst sapUiMnuNoIco sapUiMnuNoSbMnu"><li class="sapUiMnuItm" id="sap-fpa-ui-collaboration-panel-tool-menu-overview" data-sap-ui="sap-fpa-ui-collaboration-panel-tool-menu-overview" role="menuitem" aria-labelledby="__menu0 sap-fpa-ui-collaboration-panel-tool-menu-overview-txt sap-fpa-ui-collaboration-panel-tool-menu-overview-scuttxt" aria-disabled="false" aria-posinset="1" aria-setsize="5"><div class="sapUiMnuItmL"></div><div class="sapUiMnuItmIco"></div><div id="sap-fpa-ui-collaboration-panel-tool-menu-overview-txt" class="sapUiMnuItmTxt">Resources</div><div id="sap-fpa-ui-collaboration-panel-tool-menu-overview-scuttxt" class="sapUiMnuItmSCut"></div><div class="sapUiMnuItmSbMnu"></div><div class="sapUiMnuItmR"></div></li><li class="sapUiMnuItm" id="sap-fpa-ui-collaboration-panel-tool-menu-participants" data-sap-ui="sap-fpa-ui-collaboration-panel-tool-menu-participants" role="menuitem" aria-labelledby="__menu0 sap-fpa-ui-collaboration-panel-tool-menu-participants-txt sap-fpa-ui-collaboration-panel-tool-menu-participants-scuttxt" aria-disabled="false" aria-posinset="2" aria-setsize="5"><div class="sapUiMnuItmL"></div><div class="sapUiMnuItmIco"></div><div id="sap-fpa-ui-collaboration-panel-tool-menu-participants-txt" class="sapUiMnuItmTxt">Participants</div><div id="sap-fpa-ui-collaboration-panel-tool-menu-participants-scuttxt" class="sapUiMnuItmSCut"></div><div class="sapUiMnuItmSbMnu"></div><div class="sapUiMnuItmR"></div></li><li class="sapUiMnuItm" id="sap-fpa-ui-collaboration-panel-tool-menu-close" data-sap-ui="sap-fpa-ui-collaboration-panel-tool-menu-close" role="menuitem" aria-labelledby="__menu0 sap-fpa-ui-collaboration-panel-tool-menu-close-txt sap-fpa-ui-collaboration-panel-tool-menu-close-scuttxt" aria-disabled="false" aria-posinset="3" aria-setsize="5"><div class="sapUiMnuItmL"></div><div class="sapUiMnuItmIco"></div><div id="sap-fpa-ui-collaboration-panel-tool-menu-close-txt" class="sapUiMnuItmTxt">Close Discussion</div><div id="sap-fpa-ui-collaboration-panel-tool-menu-close-scuttxt" class="sapUiMnuItmSCut"></div><div class="sapUiMnuItmSbMnu"></div><div class="sapUiMnuItmR"></div></li><li class="sapUiMnuItm" id="sap-fpa-ui-collaboration-panel-tool-menu-leave" data-sap-ui="sap-fpa-ui-collaboration-panel-tool-menu-leave" role="menuitem" aria-labelledby="__menu0 sap-fpa-ui-collaboration-panel-tool-menu-leave-txt sap-fpa-ui-collaboration-panel-tool-menu-leave-scuttxt" aria-disabled="false" aria-posinset="4" aria-setsize="5"><div class="sapUiMnuItmL"></div><div class="sapUiMnuItmIco"></div><div id="sap-fpa-ui-collaboration-panel-tool-menu-leave-txt" class="sapUiMnuItmTxt">Leave Discussion</div><div id="sap-fpa-ui-collaboration-panel-tool-menu-leave-scuttxt" class="sapUiMnuItmSCut"></div><div class="sapUiMnuItmSbMnu"></div><div class="sapUiMnuItmR"></div></li><li class="sapUiMnuItm" id="sap-fpa-ui-collaboration-panel-tool-menu-delete" data-sap-ui="sap-fpa-ui-collaboration-panel-tool-menu-delete" role="menuitem" aria-labelledby="__menu0 sap-fpa-ui-collaboration-panel-tool-menu-delete-txt sap-fpa-ui-collaboration-panel-tool-menu-delete-scuttxt" aria-disabled="false" aria-posinset="5" aria-setsize="5"><div class="sapUiMnuItmL"></div><div class="sapUiMnuItmIco"></div><div id="sap-fpa-ui-collaboration-panel-tool-menu-delete-txt" class="sapUiMnuItmTxt">Delete Discussion</div><div id="sap-fpa-ui-collaboration-panel-tool-menu-delete-scuttxt" class="sapUiMnuItmSCut"></div><div class="sapUiMnuItmSbMnu"></div><div class="sapUiMnuItmR"></div></li></ul></div></div>

	public void enterExpectedDisscussion(String discussionName) {
		// String disscussionContainerId =
		// "sap-fpa-ui-collaboration-panel-all-list-page-list-scroll-container";
		// List<WebElement> disscussions =
		// SapUi5Factory.findWebElementsByXPath(bw,
		// "//div[@class='epmDiscussionListContainer']/div");
		// List<WebElement> disscussionTitleIds =
		// SapUi5Factory.findWebElementsByXPath(bw,
		// "//label[contains(@class,'discussionItemTitle')]");
		//
		// int disscussionIndex = 0;
		// for (int index = 1; index < disscussionTitleIds.size(); index++) {
		// if (disscussionTitleIds.get(index).getText().equals(discussionName))
		// {
		// disscussionIndex = index;
		// break;
		// }
		// }

		By discussionLocator = By.xpath(String.format("//label[contains(@class,'discussionItemTitle') and .//text()='%s']", discussionName));
		safeClick(MAX_TIME_OUT, discussionLocator);

	}

	*//**
	 * @deprecated can result in element not clickable exception
	 * 
	 *//*
	public void sendMessage(String message) {
		// Type the message to textAera
		typeMessage(message);
		getSendMsgBtn().click();
		FPAScenarioHelper.explicitWait(3000); // wait for the effect of clicking
												// button finish.
		assertMessage(message);
	}

	*//**
	 * @deprecated this method is not stable
	 *//*
	public void assertMessage(String expectedMsg) {
		// TODO: temp solution
		if (expectedMsg.contains("MARY_COOPER")) {
			expectedMsg = expectedMsg.replace("MARY_COOPER", "Mary Cooper");
		} else if (expectedMsg.contains("MARK_MACOY")) {
			expectedMsg = expectedMsg.replace("MARK_MACOY", "Mark Macoy");
		} else if (expectedMsg.contains("MARTIN_BRODY")) {
			expectedMsg = expectedMsg.replace("MARTIN_BRODY", "Martin Brody");
		} else {
			expectedMsg = expectedMsg.replace("MATT_HOOPER", "Matt Hooper");
		}
		// check if message is sent successfully
		String conversionItemXpath = "//div[@id='sap-fpa-ui-collaboration-panel-detail-page']//*[contains(@class,'epmConversationItem')]";
		List<WebElement> conversionItems = SapUi5Factory.findWebElements(bw, By.xpath(conversionItemXpath));
		//boolean isMessageCanBeFoundInConversion = false;
		String text;
		for (int index = 0; index < conversionItems.size(); index++) {
			text = conversionItems.get(index).getText();
			if (text.contains(expectedMsg)) {
			//	isMessageCanBeFoundInConversion = true;
				break;
			}
		}
		// TODO please help to check.
		// Assert.assertEquals(true, isMessageCanBeFoundInConversion);
	}

	*//**
	 * click link button in collaboration panel
	 *//*
	public void assertClickLinkButton() {

		try {
			String statusBeforeClick = getLinkBtnStatus();
			getLinkBtn().click();
			if (statusBeforeClick.equals(CollaborationPanel.LINK_BUTTON_STATUS_PIN)) {
				assertLinkBtnStatus(CollaborationPanel.LINK_BUTTON_STATUS_UNPIN);
			} else {
				// an alert to say resource will be unlinked.

				Dialog breakLinkConfirmDialog = Dialog.findPopupConfirmDialogById(bw, "sap-fpa-ui-collaboration-panel-confirmBreakLinkDialog");
				breakLinkConfirmDialog.ok();

				assertLinkBtnStatus(CollaborationPanel.LINK_BUTTON_STATUS_PIN);
			}

		} catch (Exception e) {
			Assert.fail("Error finding element: linkBtn in collaboration panel, " + e.getMessage());
		}

	}

	*//**
	 * Click the last matched item in resource list of discussion detail
	 * Resource: Report | Dashboard | Task | Version
	 * 
	 * @param verificationList
	 * 
	 *            verification list is an array with below structure
	 *            {{resourceType,resourceName,resourceId,addby},{resourceType,
	 *            resourceName,resourceId,addby}} open the resource from the
	 *            resource list and then check if it's opened correctly
	 * 
	 *            TO_DO: only support report now and continue for others in
	 *            future.
	 * 
	 *//*
	@SuppressWarnings("unused")
	public void assertOpenResource(ResourceItem expectedItem, String fromWhere) {

		// default we start from detail page
		if (fromWhere.equals(CollaborationPanel.RESOURCE_FROM_RESOURCE_PAGE)) {
			// click tool button in collaboration panel
			clickButtonInToolList(CollaborationPanel.RESOURCE_BTN_IN_TOOL_BUTTON);
		}

		
		// List<String> notFoundList = new ArrayList<String>();
		List<WebElement> resourceList = resList(fromWhere);

		boolean isFound = true;
		int num = resourceList.size();
		if (num != 0) {
			if (resourceList.get(num - 1).getText().contains(expectedItem.displayTextInResourceList()) == false) {
				isFound = false;
			} else {
				safeClick(MAX_WAIT_TIME, resourceList.get(num - 1));
				if (expectedItem.resourceType == ResourceItem.RESOURCE_TYPE_REPORT) {
					ReportHelper.assertReportIsOpen(bw,expectedItem.resourceName);
				}
			}
		}

		if (isFound = false) {
			Assert.fail("Find Resource Item in Resource List fails.");
		}
	}

	*//**
	 * Open the setting button and click corresponding item - Resources -
	 * Participants - Close Discussion
	 *//*
	public void clickButtonInToolList(String ButtonText) {

		selectMenuList();

		if (ButtonText.equals(CollaborationPanel.RESOURCE_BTN_IN_TOOL_BUTTON)) {
			selectResourceMenu();
		} else if (ButtonText.equals(CollaborationPanel.PARTICIPANTS_BTN_IN_TOOL_BUTTON)) {
			selectParticipantMenu();
		} else if (ButtonText.equals(CollaborationPanel.START_LIVE_SESSION_BTN_IN_TOOL_BUTTON)) {
			selectRealTimeMenu();
		} else if (ButtonText.equals(CollaborationPanel.LEAVE_DISCUSSION_BTN_IN_TOOL_BUTTON)) {
			selectLeaveDiscussionMenu();
		} else {
			selectCloseDiscussionMenu();
		}

	}

	public void back() {
		clickBackButton();
	}

	// *******start of discuss cell************
	public void selectDiscussCellMenu() {
		clickButtonInAddStaffMenuList(CollaborationPanel.DISCUSS_CELL_BUTTON_IN_STAFF_MENU);
	}

	// *******end of discuss cell************

	public void validateDetailPage() {
		SapUi5Utilities.assertElementPresent(bw, By.xpath("//div[@id='sap-fpa-ui-collaboration-panel-detail-page' and not(contains(@class, 'sapMNavItemHidden'))]"));
	}

	public void validateListPage() {
		SapUi5Utilities.assertElementPresent(bw, By.xpath("//div[@id='sap-fpa-ui-collaboration-panel-all-list-page' and not(contains(@class, 'sapMNavItemHidden'))]"));
	}

	// *******start of share version************
	public void selectShareVersionMenu() {
		clickButtonInAddStaffMenuList(CollaborationPanel.SHARE_VERSION_BUTTON_IN_STAFF_MENU);
	}

	public void shareVersion(String versionName, boolean isReadOnly) {

		Dialog dlg = getShareVersionDialog();
		selectVersion(versionName, isReadOnly);

		dlg.ok();

	}

	public void selectDiscussionType(String discussionType) {
		ComboBox discussionTypeCombo = SapUi5Factory.findComboxBoxById(bw, "sap-fpa-ui-collaboration-panel-all-list-page-discussion-type-selection");
		int selectedIndex = discussionType.equals(CollaborationPanel.DISCUSSION_TYPE_ACTIVE) ? 0 : 1;
		discussionTypeCombo.selectItem(selectedIndex);

	}

	public void validateDiscussionInList(String discussionName, boolean expectedExisting) {

		if (expectedExisting) {
			SapUi5Utilities.assertElementPresent(bw, By.xpath("//*[contains(@class,'discussionItemTitle') and text() = '" + discussionName + "']"));
		} else {
			SapUi5Utilities.assertElementInvisible(bw, By.xpath("//*[contains(@class,'discussionItemTitle') and text() = '" + discussionName + "']"));
		}
	}

	public void createPrivateDiscussion(String userName) {

		String participant = "//ul//li[.//div[@class='userName'] and .//text()='%s']";
		By by = By.xpath(String.format(participant, userName));
		safeClick(10, by);
		By startChat = By.xpath("//button[.//text()='Start Chat']");
		try {
			safeClick(10, startChat);
		} catch (Exception e) {
			safeClick(10, by);
			Wait<WebDriver> wait = new WebDriverWait(bw.driver(), 2);
			wait.until(ExpectedConditions.presenceOfElementLocated(startChat));
			safeClick(10, startChat);
		}
		// TODO: talk with Dev to have a ID for Start Chat Button
	}

	public boolean getOnlineStatus(String userName) {
		List<WebElement> participantList = participantsList();
		String className;
		String id;
		boolean isOnline = true;
		for (WebElement element : participantList) {
			id = element.findElement(By.xpath(".//div[@class='userName']")).getText();
			if (id.equalsIgnoreCase(userName)) {
				className = element.findElement(By.xpath(".//div[contains(@class,'userAvatarWrap')]")).getAttribute("class");
				isOnline = !className.contains("offline");
				break;
			}

		}

		return isOnline;
	}
	
	*//**
	 * Assert if the given user added to discussion.
	 * @param userName
	 *//*
	public void assertUserInDiscussion(String userName, boolean expected){
		List<WebElement> participantList = participantsList();
		String id;
		boolean isExist = false;
		for (WebElement element : participantList) {
			id = element.findElement(By.xpath(".//div[@class='userName']")).getText();
			if (id.equalsIgnoreCase(userName)) {
			    isExist = true;
				break;
			}

		}

		Assert.assertEquals(expected, isExist);
	}
	
	public void assertSysBubble(String text, boolean expected){
		String xp = "//div[@class='sysMsgContent' and text() = '" + text + "']";
		if (expected) {
			SapUi5Utilities.assertElementPresent(bw, By.xpath(xp));
		} else {
			SapUi5Utilities.assertElementInvisible(bw, By.xpath(xp));
		}
	}

}
*/