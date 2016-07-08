package com.sap.ui5.selenium.sap.fpa.ui.control.navigation;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;


public class NavigationItem extends CompoundControl{

	
	protected NavigationItem(String id, Frame frame) {
		super(id, frame);
	}
	
	
	public boolean isGroupItem(){
		return !this.js.exec(".getGroupKey()").get().toString().isEmpty();
	}
}