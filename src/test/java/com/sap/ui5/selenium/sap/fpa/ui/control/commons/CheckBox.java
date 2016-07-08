package com.sap.ui5.selenium.sap.fpa.ui.control.commons;

import org.openqa.selenium.By;
import com.sap.ui5.selenium.core.Frame;


public class CheckBox extends  com.sap.ui5.selenium.commons.CheckBox{


	protected CheckBox(String id, Frame frame) {
		super(id, frame);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void check() {
		if (isChecked()) {
			//throw new InvalidControlStateException("CheckBox is already checked.");
		}else{
			getCheckBoxElement().click();
		}

		
	}
	@Override
	public void uncheck() {
		if (isChecked()) {
			getCheckBoxElement().click();
		}else{
	
		}
	}
	
	public boolean isChecked(){
		boolean checkStatus = false;
		
		if(this.findRootWebElement().findElements(By.className("sapEpmUiCheckBoxStateIndicatorUnchecked")).size() == 0){
			checkStatus = true;
		}
		return checkStatus;
	}
}
