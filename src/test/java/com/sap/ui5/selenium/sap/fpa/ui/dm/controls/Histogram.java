package com.sap.ui5.selenium.sap.fpa.ui.dm.controls;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;

public class Histogram extends CompoundControl {

	protected Histogram(String id, Frame frame) {
		super(id, frame);
		// TODO Auto-generated constructor stub
	}
	
	private List<HistogramItem> recordList = new ArrayList<HistogramItem>();
	
	public List<HistogramItem> getHistogramItems(){
		return this.getUI5Objects(By.className("dm-histogramItem"),3);
	}
	
	public HistogramItem getHistogramItemByValue(String value){
		recordList = this.getHistogramItems();
		for(HistogramItem item: recordList){
			if(item.getText().equalsIgnoreCase(value)){
				return item;
			}
		}		
		return null;
	}

}
