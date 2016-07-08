package com.sap.ui5.selenium.sap.fpa.ui.control.memberselector;

import java.util.List;

import org.openqa.selenium.By;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;
/**
 * 
 * @author I058399
 *
 */
public class CustomTree extends CompoundControl{

	protected CustomTree(String id, Frame frame) {
		super(id, frame);
	}
	
	public List<CustomTreeNode> getNodes() {
		return this.getUI5Objects(By.className("sapUiTreeNode"));

	}
	
	
}