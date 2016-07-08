package com.sap.ui5.selenium.sap.fpa.ui.security.audit;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.core.UI5By;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;

public class FilterTree extends CompoundControl {

	protected FilterTree(String id, Frame frame) {
		super(id, frame);
		
	}

	public List<FilterTreeNode> getNodes() {
		List<WebElement> nodes = this.findRootWebElement().findElements(By.className("sapUiTreeNode"));
		List<FilterTreeNode> ftreenodes = new ArrayList<FilterTreeNode>();
		for(WebElement node:nodes){
			FilterTreeNode ft = this.find(UI5By.id(node.getAttribute("id")));
			ftreenodes.add(ft);
		}
		return ftreenodes;
	}
	
}
