package com.sap.ui5.selenium.sap.fpa.ui.dm.controls;

import org.openqa.selenium.By;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;

public class HistogramItem extends CompoundControl {

	protected HistogramItem(String id, Frame frame) {
		super(id, frame);
		// TODO Auto-generated constructor stub
	}
	
	public void click(){
		this.findRootWebElement().click();
	}
	
	public String getText() {
		return this.getWebElement(By.className("histogramItemName")).getText();
	}

}
