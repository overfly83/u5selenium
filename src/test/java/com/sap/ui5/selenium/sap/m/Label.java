package com.sap.ui5.selenium.sap.m;

/*! -----------------------------------------------------------------------------------
 * SAP UI development toolkit for HTML5 (SAPUI5)
 * 
 * (c) Copyright 2009-2014 SAP AG. All rights reserved
 * ------------------------------------------------------------------------------------ */



import org.junit.Assert;

import com.sap.ui5.selenium.core.Frame;

/**
 * The class <b><code>Label</code></b> is the Selenium wrapper 
 * for the SAPUI5 control: <code>sap.ui.commons.Label</code>.<br>
 *
 * @author GENERATOR (JavaScript control: sap.ui.commons.Label)
 */
public class Label extends com.sap.ui5.selenium.commons.Label {

	/**
	 * Constructs the class <code>Label</code>
	 */
	protected Label(String id, Frame frame) {
		super(id, frame);
	}

	/** 
	 * check if label value is as expected
	 * @param expected label value
	 */
	public boolean checkText(String expected) {
		String actual = this.getText();
		if(actual == null) {
			return expected == null;
		}	
		
		return actual.equals(expected);
	}
	
	/** 
	 * assert if label value is as expected
	 * @param expected label value
	 */
	public void assertText(String expected) {
		Assert.assertEquals("assert text  of label '"+ this.getId() +"', ", expected, this.getText());
	}
}


