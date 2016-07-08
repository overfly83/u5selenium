package com.sap.ui5.selenium.sap.fpa.ui.control.navigation;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Control;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;
import com.sap.ui5.selenium.sap.m.Popover;
import com.sap.ui5.selenium.utilities.SapUi5Factory;


public class Navigation extends CompoundControl{

	
	protected Navigation(String id, Frame frame) {
		super(id, frame);
	}
	
	/**
	 * 
	 * @param key
	 */
	public void select(final String... paths) {
		List<Control> items = this.getItems();
		final String navi = this.getId();
		for(int i = 0; i < items.size(); i++){
			final WebElement item = this.getWebElement(By.id(items.get(i).getId()));
			if(paths[0].trim().equals(item.getText().trim())){
				new WebDriverWait(driver, 20).until(new ExpectedCondition<Boolean>(){
					public Boolean apply(WebDriver input) {
						try{
							item.click();
							if(paths.length==2){
								Popover submenu = SapUi5Factory.findUI5ObjectByClass(windowManager.currentWindow(), "navigation-subMenu",1);
								submenu.clickWebElement(By.xpath(".//div[contains(@class,'sapMPopoverScroll')]//div[contains(@class,'Item') and text()='"+paths[1].trim()+"']"),1);
							}
							new WebDriverWait(driver, 3).until(ExpectedConditions.invisibilityOfElementLocated(By.id(navi)));
							return true;
						}catch(Exception e){
							return false;
						}
					}
				});
				
				break;
			}
		}
		

	}

	private List<Control> getItems(){
		ArrayList<Control> items = new ArrayList<Control>();
		ArrayList<Control> allitems = this.js.exec(".getItems()").getList(Control.class);
		for(Control c:allitems){
			if(!((NavigationItem)c).isGroupItem()){
				items.add(c);
			}
		}
		items.addAll(this.js.exec(".getGroups()").getList(Control.class));
		return items;
	}

}