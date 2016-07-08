package test.sap.fpa.system;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarItem;
import com.sap.ui5.selenium.sap.fpa.ui.control.tabContainer.TabContainer;
import com.sap.ui5.selenium.sap.m.CheckBox;
import com.sap.ui5.selenium.sap.m.Input;
import com.sap.ui5.selenium.sap.m.Switch;
import com.sap.ui5.selenium.sap.ui.fileuploader.FileUploader;
import com.sap.ui5.selenium.utilities.SapUi5Factory;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;

public class AdministrationHelper {
	
	/**
	 * click tab according to tabName
	 * @param bw 
	 * @param tabName: tabname
	 */
	public static void gotoTab(BrowserWindow bw,String tabName){
		((TabContainer) SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-setting-tab-container")).clickTab(tabName);
	}
	
	/**
	 * set cloud connection information
	 * @param bw 
	 * @param hostName
	 * @param port
	 * @param user
	 * @param pwd
	 */
	public static void setCloudConnection(BrowserWindow bw,String hostName, String port, String user, String pwd) {

		NewToolbarItem edit = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-connectionButtonEdit");
		edit.click();
		((Input)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-connectionHost")).setValue(hostName, false);
		((Input)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-connectionPort")).setValue(port, false);
		((Input)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-connectionUserName")).setValue(user, false);
		((Input)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-connectionPassword")).setValue(pwd, false);
		NewToolbarItem save = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-connectionButtonSave");
		save.click();
	}
	
	/**
	 * set service provider information
	 * @param bw 
	 * @param displayName		"SAP"
	 * @param organizationName	"SAP"
	 * @param url				"SAP"
	 */
	public static void setServiceProviderInformation(BrowserWindow bw, String displayName, String organizationName, String url){
		
		NewToolbarItem edit = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-idButtonEdit");
		edit.click();
		((Input)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-orgDisplayName")).setValue(displayName, false);
		((Input)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-orgName")).setValue(organizationName, false);
		((Input)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-orgURL")).setValue(url, false);
		NewToolbarItem save = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-idButtonSave");
		save.click();
	}
	
	/**
	 * set SMAL identity provider information
	 * @param bw 
	 * @param localpath          			
	 */
	public static void setSMALIdentityProvider(BrowserWindow bw, String localPath){
		
		NewToolbarItem edit = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-idpButtonEdit");
		edit.click();
		FileUploader fu = FileUploader.findFileUploaderByIdEndsWith(bw, "-uploader");
		fu.upload(localPath);
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
		CheckBox cb = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-changeIdpDynamicUserCreation");
		cb.check();
		NewToolbarItem save = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-idpButtonSave");
		save.click();
	}
	
	/**
	 * enable SAML authentication
	 * @param bw 
	 * @param change
	 */
	public static void enableSAMLAuthentication(BrowserWindow bw, Boolean change){
		Switch authentication = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-enableSamlSwitch");
		if (change) {
			authentication.switchOn();	
		} 
		else {
			authentication.switchOff();
		}
	}


	/**
	 * enable X509 authentication
	 * @param bw 
	 * @param change
	 */
	public static void enableX509Authentication(BrowserWindow bw, Boolean change){
		Switch authentication = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-enableX509Switch");
		if (change) {
			authentication.switchOn();
		}
		else {
			authentication.switchOff();
		}
	}
	
	
}