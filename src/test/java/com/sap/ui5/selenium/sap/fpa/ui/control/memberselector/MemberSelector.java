package com.sap.ui5.selenium.sap.fpa.ui.control.memberselector;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.commons.Button;
import com.sap.ui5.selenium.commons.ComboBox;
import com.sap.ui5.selenium.commons.Link;
import com.sap.ui5.selenium.sap.m.SearchField;
import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.m.Dialog;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;

/**
 * 
 * @author I058399
 * Example: MemberSelector ms = MemberSelector.findToolbarMenuByIdEndsWith(defaultWindow, "role-selector-dialog");
 *			ms.SingleSearch("ID", "aa", "aa");
 */
public class MemberSelector extends Dialog{

	protected MemberSelector(String id, Frame frame) {
		super(id, frame);
	}
	
	/**
	 * 
	 * @param IdDescription
	 * @param searchtext
	 * @param item
	 * @return
	 */
	public boolean singleSearch(String IdDescription, String searchtext, String item) {

		waitUntilInitialized();
		this.changeDisplayMode(IdDescription);
		this.searchContent(searchtext);
		return this.selectFromTree(item);
	}
	
	/**
	 * 
	 * @param IdDescription
	 * @param item
	 * @return
	 */
	public boolean singleSelect(String IdDescription, String item) {
		waitUntilInitialized();
		this.changeDisplayMode(IdDescription);
		return this.selectFromTree(item);
	}
	
	/**
	 * 
	 * @param IdDescription
	 * @param items
	 * @return
	 */
	public boolean multiSearch(String IdDescription, String... items) {				//multi version of singlesearch
		
		boolean isSelected = true;
		for(int i = 0;i < items.length;i++) {
			if(!this.singleSearch(IdDescription, items[i], items[i])) {
				isSelected = false;
			}
		}
		return isSelected;
	}
	
	/**
	 * 
	 * @param IdDescription
	 * @param searchtext
	 * @param item.
	 * @return
	 */
	public boolean unSelect(String IdDescription, String searchtext, String item) { 							//select without search it
		waitUntilInitialized();
		
		this.changeDisplayMode(IdDescription);
		this.searchContent(searchtext);
		return this.unSelectFromTree(item);
	}
	
	
    public void clearSelection(){
        SapUi5Utilities.waitWhenPageBusy(driver, 30);
        //((Link)getThisDialog().getUI5ObjectByIdEndsWith("di-mapping-memberselectorms-delall-btn")).click();
        ((Link)getThisDialog().getUI5ObjectByIdEndsWith("-delall-btn")).click();
	}
    
	/**
	 *when you want to change the display way from default to Flat view or other way you can call this function
	 *@value is the name of the display way you want to choose
	 *Example: changeDisplay(bw, "Flat view")
	 */
	public void changeDisplayView(BrowserWindow bw, String value){
		((ComboBox)getThisDialog().getUI5ObjectByIdEndsWith("hierarchy-list")).setValue(value);
	}
	
	private void waitUntilInitialized(){
		Wait<WebDriver> wait = new WebDriverWait(driver, 30);
		wait.until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					webdriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
					return webdriver.findElement(By.className("sapUiLocalBusyIndicator")) == null;
				}catch(NoSuchElementException e){
					webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					return true;
				}catch(StaleElementReferenceException e){
					webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					return true;
				}
			}
		});
		wait.until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					WebElement treecont = getThisDialog().getWebElement(By.className("sapUiTreeCont"));
					return treecont.getText().length() > 0;
				}catch(Exception e){
					return false;
				}
			}
		});
	}
	
	private void changeDisplayMode(String IdDescription){
		new WebDriverWait(driver, 30).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					try{
						Button btn = getUI5ObjectByIdEndsWith("showHideContentBtn",1);
						btn.click();
					}catch(Exception e){}
					Thread.sleep(500);
					getUI5ObjectByIdEndsWith("-ms-text-switcher",1);
					return true;
				}catch(Exception e){
					return false;
				}
			}
		});
		
		ComboBox cb =  this.getUI5ObjectByIdEndsWith("-ms-text-switcher");
		if(IdDescription.equalsIgnoreCase("ID")){
			cb.selectItem(2);
		}else if(IdDescription.toUpperCase().startsWith("DESCRIPTION")){
			cb.selectItem(0);
		}else if(IdDescription.equalsIgnoreCase("ID and Description")){
			cb.selectItem(1);
		}
	}
	public void searchContent(String searchtext){
		
		final SearchField sf =  this.getUI5ObjectByIdEndsWith("-ms-search-field");
		new WebDriverWait(driver, 30).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					if(webdriver.findElement(By.id(sf.getId())).getAttribute("class").contains("StartCollapsed")){
						webdriver.findElement(By.id(sf.getId())).findElement(By.className("sapMSFS")).click();
						Thread.sleep(500);
					}
					return !webdriver.findElement(By.id(sf.getId())).getAttribute("class").contains("StartCollapsed");
				}catch(Exception e){
					return false;
				}
			}
		});
		sf.clearValue();
		sf.setValue(searchtext);
		//sf.triggerSearch();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {}
	}
	
	private boolean selectFromTree(String item){
		CustomTree tr =  this.getUI5ObjectByIdEndsWith("-custom-tree");
		List<CustomTreeNode> tnodes = tr.getNodes();
		for(CustomTreeNode tn:tnodes){
			if(tn.getDisplayText().equalsIgnoreCase(item.trim())){
				tn.select();
				return true;
			}
		}
		return false;
	}
	
	private boolean unSelectFromTree(String item){
		CustomTree tr =  this.getUI5ObjectByIdEndsWith("-custom-tree");
		List<CustomTreeNode> tnodes = tr.getNodes();
		for(CustomTreeNode tn:tnodes){
			if(tn.getDisplayText().equalsIgnoreCase(item.trim())){
				tn.unSelect();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check whether Item is selectable
	 */
	public boolean checkItemSelectable(String item){
		CustomTree tr =  this.getUI5ObjectByIdEndsWith("-custom-tree");
		List<CustomTreeNode> tnodes = tr.getNodes();
		for(CustomTreeNode tn:tnodes){
			if(tn.getDisplayText().equalsIgnoreCase(item.trim())){
				return tn.isSelectable();
			}
		}
		return false;
	}
	
	/**
	 * Assert whether Item is selected as expected
	 */	
	public void assertItemIsSelectedStatus(String item, Boolean expected){
		CustomTree tr =  this.getUI5ObjectByIdEndsWith("-custom-tree");
		List<CustomTreeNode> tnodes = tr.getNodes();
		Boolean actual = false;
		for(CustomTreeNode tn:tnodes){
			if(tn.getDisplayText().equalsIgnoreCase(item.trim()) && tn.isIsSelected()){
				actual = true;
			}
		}
		Assert.assertEquals("assert Item" + item + "is selected", expected, actual);
	}
	
}
