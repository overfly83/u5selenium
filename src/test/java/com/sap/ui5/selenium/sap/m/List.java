package com.sap.ui5.selenium.sap.m;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.junit.Assert;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;

public class List extends CompoundControl {

	protected List(String id, Frame frame) {
		super(id, frame);
	}

	public void selectAll(){
		this.js.exec(".selectAll");
	}
	
	public void selectItem(String item){
		java.util.List<StandardListItem> items = this.js.exec(".getItems()").getList(com.sap.ui5.selenium.sap.m.StandardListItem.class);
		for(StandardListItem it:items){
			if(it.getText().trim().equals(item.trim())){
				it.click();
				break;
			}
		}
	}
	
	/** 
	 * check if expected item exists in List
	 * @param expected item value
	 */
	public boolean checkItemExists(String expected) {
		java.util.List<WebElement> items = this.findRootWebElement().findElements(By.className("sapMLIB"));
		if(items.size()==0){
			items = this.findRootWebElement().findElements(By.xpath(".//li[contains(@class, 'sapMCLI')]"));
		}
		for(WebElement it:items){
			if(it.getText().contains("\n")){
				if(it.getText().split("\\n")[0].equals(expected)){
					return true;
				}
			}else{
				if(it.getText().equals(expected)){
					return true;
				}
			}
		}
		return false;
	}
	
	/** 
	 * assert if expected item exists in List
	 * @param expected item value
	 */
	public void assertItemExists(String text, boolean expected) {
		Assert.assertEquals("assert  List '"+ this.getId() +"' contains item " +text, 
				expected, this.checkItemExists(text));
	}
}
