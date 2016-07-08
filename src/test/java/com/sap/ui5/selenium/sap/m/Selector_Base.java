/*! -----------------------------------------------------------------------------------
 * SAP UI development toolkit for HTML5 (SAPUI5)
 * 
 * (c) Copyright 2009-2014 SAP SE. All rights reserved
 * ------------------------------------------------------------------------------------ */

/* -----------------------------------------------------------------------------------
 * Hint: This is a derived (generated) file. Changes should be done in the non-base
 * source file only (Select.java) or they will be lost after the next generation.
 * ----------------------------------------------------------------------------------- */

package com.sap.ui5.selenium.sap.m;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;

/**
 * The class <b><code>Select_Base</code></b> is the base Selenium wrapper
 * for the SAPUI5 control: <code>sap.m.Select</code>.<br>
 *
 * @author GENERATOR (JavaScript control: sap.m.Select)
 * @version 1.24.0
 */
class Selector_Base extends CompoundControl {

	/**
	 * constructs the class <code>Select_Base</code>
	 */
	Selector_Base(String id, Frame frame) {
		super(id, frame);
	}

	/**
	 * Getter for property <code>name</code>
	 * The name to be used in the HTML code (e.g. for HTML forms that send data to the server via submit).
	 * @return the value of property name  
	 */
	public String getName() {
		return this.js.exec(".getName()").get(String.class);
	}

	/**
	 * Getter for property <code>visible</code>
	 * Determines whether the control is visible or not.
	 * @return the value of property visible  
	 */
	public boolean isVisible() {
		return this.js.exec(".getVisible()").get(Boolean.class);
	}

	/**
	 * Getter for property <code>enabled</code>
	 * Determines whether the user can change the selected value.
	 * @return the value of property enabled  
	 */
	public boolean isEnabled() {
		return this.js.exec(".getEnabled()").get(Boolean.class);
	}

	/**
	 * Getter for property <code>width</code>
	 * Defines the width of the select input. The default width of a select control depends on the width of the widest option/item in the list. This value can be provided in %, em, px? and all CSS units.
	 * Note: The width will be ignored if the "autoAdjustWidth" property is set to true.
	 * @return the value of property width  
	 */
	public String getWidth() {
		return this.js.exec(".getWidth()").get(String.class);
	}

	/**
	 * Getter for property <code>maxWidth</code>
	 * Defines the maximum width. This value can be provided in %, em, px? and all CSS units
	 * @return the value of property maxWidth  
	 */
	public String getMaxWidth() {
		return this.js.exec(".getMaxWidth()").get(String.class);
	}

	/**
	 * Getter for property <code>selectedKey</code>
	 * Key of the selected item. If the key has no corresponding aggregated item, no changes will apply. If duplicate keys exists the first item matching the key is used.
	 * @return the value of property selectedKey  
	 */
	public String getSelectedKey() {
		return this.js.exec(".getSelectedKey()").get(String.class);
	}

	/**
	 * Getter for property <code>selectedItemId</code>
	 * Id of the selected item. If the id has no corresponding aggregated item, no changes will apply.
	 * @return the value of property selectedItemId  
	 */
	public String getSelectedItemId() {
		return this.js.exec(".getSelectedItemId()").get(String.class);
	}

	/**
	 * Getter for property <code>icon</code>
	 * The URI to the icon that will be displayed only when using the ?IconOnly? type.
	 * @return the value of property icon  
	 */
	public String getIcon() {
		return this.js.exec(".getIcon()").get(String.class);
	}

/*	*//**
	 * Getter for property <code>type</code>
	 * Type of a select. Possibles values "Default", "IconOnly".
	 * @return the value of property type  
	 *//*
	public com.sap.ui5.selenium.mobile.SelectType getType() {
		return com.sap.ui5.selenium.mobile.SelectType.valueOf(this.js.exec(".getType()").get(String.class));
	}*/

	/**
	 * Getter for property <code>autoAdjustWidth</code>
	 * If set to true, the width of the select input is determined by the selected item?s content.
	 * @return the value of property autoAdjustWidth  
	 */
	public boolean isAutoAdjustWidth() {
		return this.js.exec(".getAutoAdjustWidth()").get(Boolean.class);
	}

	/**
	 * Getter for aggregation items
	 * Items of the Item control.
	 * @return com.sap.ui5.selenium.core.Item[]
	 */
	public java.util.List<com.sap.ui5.selenium.core.Item> getItems() {
		return this.js.exec(".getItems()").getList(com.sap.ui5.selenium.core.Item.class);
	}

	/**
	 * Getter for single object of aggregation items
	 * Items of the Item control.
	 * @param index the index of the object within the aggregation
	 * @return com.sap.ui5.selenium.core.Item
	 */
	public com.sap.ui5.selenium.core.Item getItem(int index) {
		return this.js.exec(".getItems()[" + index + "]").get(com.sap.ui5.selenium.core.Item.class);
	}

	/**
	 * Checks for the provided <code>com.sap.ui5.selenium.core.Item</code> in the items-aggregation and
	 * returns its index if found or -1 otherwise.
	 * @param item the item whose index is looked for.
	 * @return the index of the provided control in the aggregation.
	 */
	public int indexOfItem(com.sap.ui5.selenium.core.Item item) {
		return this.indexOfAggregation("items", item);
	}

	/**
	 * Retrieves the selected item from the aggregation named <code>items</code>.
	 */
	public com.sap.ui5.selenium.core.Item getSelectedItem() {
		return this.js.exec(".getSelectedItem()").get(com.sap.ui5.selenium.core.Item.class, true);
	}

}
