package com.sap.ui5.selenium.sap.fpa.ui.control.shell;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;


public class ShellInfoMessage extends CompoundControl{


	public enum INFO_TYPE{
		INFO("information",1),
		SUCCESS("success",2),
		WARNING("warning",3),
		ERROR("error",4);
		
		String name;
		int index;
		INFO_TYPE(String name,int index){
			this.name = name;
			this.index = index;
		}
	}
	
	protected ShellInfoMessage(String id, Frame frame) {
		super(id, frame);
	}
	

	protected String getMessageInformation(){
		return this.js.exec(".getTitle()").get().toString();
	}
	
	protected INFO_TYPE getMessageType(){
		int type = Integer.parseInt(this.js.exec(".getType()").get().toString());
		switch(type){
		case 1:return INFO_TYPE.INFO;
		case 2:return INFO_TYPE.SUCCESS;
		case 3:return INFO_TYPE.WARNING;
		case 4:return INFO_TYPE.ERROR;
		}
		return null;
	}
	
	public void assertMessageInformation(String info){
	    String actualmsg = this.getMessageInformation();
		Assert.assertTrue("Expected Message is: "+info+" && actual message is "+actualmsg , actualmsg.trim().matches(info.trim()));
	}
	
	public boolean checkMessageInformation(String info){
	    String actualmsg = this.getMessageInformation();
		return actualmsg.trim().matches(info.trim());
	}
	public boolean checkMessageInformationContains(String info){
	    String actualmsg = this.getMessageInformation();
		return actualmsg.trim().contains(info.trim());
	}
	public void close(){
		List<WebElement> close = this.findRootWebElement().findElements(By.xpath(".//div[@class='sapEpmUiShellInfoMessageCloseButton']/span"));
		if(close.size()>0){
			String id = close.get(0).getAttribute("id");
			close.get(0).click();
			driver.manage().timeouts().implicitlyWait((long) 0.5, TimeUnit.SECONDS);
			new WebDriverWait(driver, 1).pollingEvery(1, TimeUnit.SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By.id(id)));	
		}
		
		driver.manage().timeouts().implicitlyWait((long) 10, TimeUnit.SECONDS);
		
	}


}
