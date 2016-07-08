package com.sap.ui5.selenium.sap.fpa.ui.control.shell;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarItem;


public class ShellHeaderItem extends NewToolbarItem{

	protected ShellHeaderItem(String id, Frame frame) {
		super(id, frame);
	}


	public boolean isVisible() {
		return this.findRootWebElement().isDisplayed();
	}

	public boolean isEnabled(){
		return this.findRootWebElement().isEnabled();
	}
}
