package test.sap.fpa.collaboration;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.sap.fpa.ui.control.shell.Shell;
import com.sap.ui5.selenium.sap.m.Dialog;
import com.sap.ui5.selenium.utilities.SapUi5Factory;

public class CollaborationHelper {

	public static boolean isPanelOpen(BrowserWindow bw, int ...timeout) {
		final By byPanel = By.className("sapEpmUiShellCollaborationPane");
		int defaulttimeout = (timeout!=null && timeout.length==1)?timeout[0]:30;
		long start = System.currentTimeMillis();
		while(defaulttimeout*1000 > System.currentTimeMillis() - start) {
			try {
				Wait<WebDriver> wait = new WebDriverWait(bw.syncAjax().driver(), 10).pollingEvery(100, TimeUnit.MILLISECONDS);
				wait.until(new ExpectedCondition<Boolean>(){ 
				    public Boolean apply(WebDriver webDriver) {
				    	try{
				    		WebElement element = webDriver.findElement(byPanel);
				    		return element != null && !element.getCssValue("margin-right").startsWith("-"); 
				    	}catch(Exception e){
				    		return false;
				    	}
				    } 
				});
				return true;
			} catch(Exception e) { 
				return false;
				}
		}
		return false;
	}
	
	public static void assertCollaborationEnabled(BrowserWindow bw, boolean expected) {
		if (expected) {
			((Shell) SapUi5Factory.findUI5ObjectByClass(bw, "sapEpmUiShell", 100)).toggleDiscussions(60);
			Assert.assertTrue("expect collaborationPanel open",isPanelOpen(bw));
		} else {
			((Shell) SapUi5Factory.findUI5ObjectByClass(bw, "sapEpmUiShell", 100)).toggleDiscussions(3);
			Dialog dlg = SapUi5Factory.findUI5ObjectByClass(bw, "sapEpmUiDialog");
			dlg.assertDisplay(true);
			dlg.cancel();
		}

	}
}
