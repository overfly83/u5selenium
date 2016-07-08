package com.sap.ui5.selenium.sap.fpa.ui.control.shell;

import java.util.List;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;
import com.sap.ui5.selenium.sap.fpa.ui.control.shell.ShellInfoMessage.INFO_TYPE;


public class ShellInfoMessageArea extends CompoundControl{


	protected ShellInfoMessageArea(String id, Frame frame) {
		super(id, frame);
	}
	

	public static ShellInfoMessageArea findShellInfoMessageArea(BrowserWindow bw){
		long start = System.currentTimeMillis();
		while (30 * 1000 > System.currentTimeMillis() - start) {	
	    	try{
	    		return bw.find(By.className("sapEpmUiShellInfoMessageArea"));
	    	}catch(Exception e){}
		} 	
		throw new TimeoutException("Could not find element within timeout 30 seconds: sapEpmUiShellInfoMessageArea");
	}
	
	
	private List<ShellInfoMessage> getMessages(){
		try{
			String getMsg = "return sap.fpa.ui.App.getShell().getInfoMessageArea().getMessages()";
			
			List<ShellInfoMessage> msgList = this.js.exec(getMsg).getList(com.sap.ui5.selenium.sap.fpa.ui.control.shell.ShellInfoMessage.class);
			return msgList;
		}catch(Exception e){
			return null;
		}
	}
	
	public ShellInfoMessage getLastInfoMessage(INFO_TYPE msgType){
		long start = System.currentTimeMillis();
		while (30 * 1000 > System.currentTimeMillis() - start) {	
	    	try{
	    		List<ShellInfoMessage> infos = getMessages();
	    		if(infos.size()>0){
	    			if(msgType!=null){
		    			for(int i = infos.size()-1;i>=0;i--){
		    				if(infos.get(i).getMessageType().equals(msgType)){
								return infos.get(i);
							}
		    			}
	    			}
	    		}
	    	}catch(Exception e){}
		} 	
		throw new TimeoutException("Message with type: "+msgType+" is not showing up after 30 seconds");
	}
	
	public void closeInfoMessage(final String text){
		new WebDriverWait(driver,10).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					List<ShellInfoMessage> infos = getMessages();
					for(ShellInfoMessage msg: infos){
						if(msg.checkMessageInformation(text)){
							msg.close();
							return true;
						}
					}
				}catch(Exception e){
					return false;
				}
				return false;
			}
		});
	}
	
	public void closeInfoMessageContains(final String text){
		new WebDriverWait(driver,10).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					List<ShellInfoMessage> infos = getMessages();
					for(ShellInfoMessage msg: infos){
						if(msg.checkMessageInformationContains(text)){
							msg.close();
							return true;
						}
					}
				}catch(Exception e){
					return false;
				}
				return false;
			}
		});
	}
	
	public void closeAllInfoMessages(){
		new WebDriverWait(driver,10).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					List<ShellInfoMessage> infos = getMessages();
					for(ShellInfoMessage msg: infos){
						msg.close();
					}
					return true;
				}catch(Exception e){
					return false;
				}
			}
		});
	}
	
	public void closeAllWarningInfoMessages(){
		new WebDriverWait(driver,10).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					List<ShellInfoMessage> infos = getMessages();
					for(ShellInfoMessage msg: infos){
						if(msg.getMessageType().equals(INFO_TYPE.WARNING)){
							msg.close();
						}
					}
					return true;
				}catch(Exception e){
					return false;
				}
			}
		});
	}
	
	public void closeAllErrorInfoMessages(){
		new WebDriverWait(driver,10).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					List<ShellInfoMessage> infos = getMessages();
					for(ShellInfoMessage msg: infos){
						if(msg.getMessageType().equals(INFO_TYPE.ERROR)){
							msg.close();
						}
					}
					return true;
				}catch(Exception e){
					return false;
				}
			}
		});
	}
	
	public void waitForMessage(final String text, final INFO_TYPE msgType, int...timeout){
		String actualMsg = "";
		INFO_TYPE actualType = null;
		long start = System.currentTimeMillis();
		int defaultTimeout = (timeout!=null && timeout.length==1)?timeout[0]:60;
		while (defaultTimeout * 1000 > System.currentTimeMillis() - start) {	
	    	try{
	    		List<ShellInfoMessage> infos = getMessages();
				for(ShellInfoMessage msg: infos){
					String tempMsg = msg.getMessageInformation();
					INFO_TYPE tempTYPE = msg.getMessageType();
					if(tempMsg!=null && !tempMsg.trim().equalsIgnoreCase("")){
						actualMsg = tempMsg;
						actualType = tempTYPE;
					}
					if(actualMsg.trim().matches(text.trim())&&actualType.equals(actualType)){
						Assert.assertTrue("Expected Message is: "+text+" && actual message is "+actualMsg , true);
						return;
					}
					
				}
	    	}catch(Exception e){
	    	}
		} 	
		Assert.assertTrue("Expected Message is: "+text+" && actual message is "+actualMsg , false);
	}
	
	public void waitForMessageContains(final String text, int...timeout){
		String actualMsg = "";
		INFO_TYPE actualType = null;
		long start = System.currentTimeMillis();
		int defaultTimeout = (timeout!=null && timeout.length==1)?timeout[0]:60;
		while (defaultTimeout * 1000 > System.currentTimeMillis() - start) {	
	    	try{
	    		List<ShellInfoMessage> infos = getMessages();
				for(ShellInfoMessage msg: infos){
					String tempMsg = msg.getMessageInformation();
					INFO_TYPE tempTYPE = msg.getMessageType();
					if(tempMsg!=null && !tempMsg.trim().equalsIgnoreCase("")){
						actualMsg = tempMsg;
						actualType = tempTYPE;
					}
					if(actualMsg.trim().contains(text.trim())&&actualType.equals(actualType)){
						Assert.assertTrue("Expected Message is: "+text+" contains actual message: "+actualMsg , true);
						return;
					}
					
				}
	    	}catch(Exception e){
	    	}
		} 	
		Assert.assertTrue("Expected Message is: "+text+" && actual message is "+actualMsg , false);
	}
	
	
}
