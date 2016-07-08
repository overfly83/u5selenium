package com.sap.ui5.selenium.sap.fpa.ui.control.collaboration;

public class ResourceItem{
	public String resourceType;
	public String resourceName;
	public String resourceId;
	public String addedBy;
	
	public final static String RESOURCE_TYPE_REPORT = "REPORT";
		
	public ResourceItem(String resType, String resName, String resId, String addedPerson){
		resourceType = resType;
		resourceName = resName;
		resourceId = resId;
		addedBy = addedPerson;
		
	}	
	
	public String displayTextInResourceList(){
		//return resourceType + ": " + resourceName + "\n" + "Added by " + addedBy; 
		return resourceType + ": " + resourceName; 
	}
}
