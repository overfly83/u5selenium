package test.sap.fpa.report;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.commons.Button;
import com.sap.ui5.selenium.commons.ComboBox;
import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.core.exception.ControlNotFoundException;
import com.sap.ui5.selenium.core.proxy.locators.NoSuchElementException;
import com.sap.ui5.selenium.sap.fpa.ui.control.analyticgrid.DialogInput;
import com.sap.ui5.selenium.sap.fpa.ui.control.analyticgrid.Grid;
import com.sap.ui5.selenium.sap.fpa.ui.control.commons.DropDown;
import com.sap.ui5.selenium.sap.fpa.ui.control.commons.Input;
import com.sap.ui5.selenium.sap.fpa.ui.control.commons.RadioButton;

import com.sap.ui5.selenium.sap.fpa.ui.control.gridlayouter.GridLayouter;
import com.sap.ui5.selenium.sap.fpa.ui.control.memberselector.MemberSelector;
import com.sap.ui5.selenium.sap.fpa.ui.control.menu.Menu;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbar;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarItem;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarMenu;
import com.sap.ui5.selenium.sap.fpa.ui.control.shell.Shell;
import com.sap.ui5.selenium.sap.fpa.ui.control.tabContainer.TabContainer;
import com.sap.ui5.selenium.sap.fpa.ui.control.toolbar.ButtonToolbarItem;
import com.sap.ui5.selenium.sap.fpa.ui.control.toolbar.MenuToolbarItem;
import com.sap.ui5.selenium.sap.m.Dialog;
import com.sap.ui5.selenium.sap.ui.table.Table;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.SapUi5Factory;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;

public class ReportHelper {
	
	private final static String PRIVATEFOLDER = Resource.getMessage("CONTENTLIB_PRIVATE");
	private final static String PUBLICFOLDER = Resource.getMessage("CONTENTLIB_PUBLIC");
	
	public static void saveReportAs(BrowserWindow defaultWindow,String RptName, String RptDesc, String folderName){	
	   	((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "saveReport")).click();
		((NewToolbarMenu)SapUi5Factory.findUI5ObjectByClass(defaultWindow, "paGridMenuPopover")).select(Resource.getMessage("SAVE_AS"));
		Assert.assertTrue(isSaveReportDialogOpen(defaultWindow));
		saveToContentLib(defaultWindow,RptName, RptDesc, folderName);
	}
	
	public static void saveReport(BrowserWindow defaultWindow){	
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "saveReport")).click();
		((NewToolbarMenu)SapUi5Factory.findUI5ObjectByClass(defaultWindow, "paGridMenuPopover")).select(Resource.getMessage("SAVE_REPORT"));
		
	}
	
	
	public static void saveToContentLib(final BrowserWindow defaultWindow,String name, String desc, String folderName)
	{
		final Dialog dlg = SapUi5Factory.findUI5ObjectByLocator(defaultWindow, By.className("sapMDialog"));
		final Shell shell = SapUi5Factory.findUI5ObjectByClass(defaultWindow, "sapEpmUiShell",100);
		new WebDriverWait(defaultWindow.driver(), 20).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					webdriver.manage().timeouts().implicitlyWait((long) 0.5, TimeUnit.SECONDS);
					List<WebElement> privatefolder = webdriver.findElement(By.id(dlg.getId())).findElements(By.linkText(shell.getUserId().toString()+" "+PRIVATEFOLDER));
					List<WebElement> publicfolder = webdriver.findElement(By.id(dlg.getId())).findElements(By.linkText(PUBLICFOLDER));
					webdriver.manage().timeouts().implicitlyWait((long) 10, TimeUnit.SECONDS);
					return privatefolder.size()>0 || publicfolder.size()>0;
				}catch(Exception e){
					return false;
				}
			}
		});
		
		
		SapUi5Utilities.waitUntilPageBusy(defaultWindow.driver(), 1);
		SapUi5Utilities.waitWhenPageBusy(defaultWindow.driver(), 10);
		
		if(folderName!=null){
			final String[] folders = folderName.split("/");
			if(folders.length<1) return;
			new WebDriverWait(defaultWindow.driver(), 3).until(new ExpectedCondition<Boolean>(){
				public Boolean apply(WebDriver input) {
					try{
						((NewToolbarItem)dlg.getUI5Object(By.xpath(".//div[contains(@class,'sapEpmModelerToolbarItem') and contains(@id,'nav-menu-btn')]"))).click();
						((NewToolbarMenu)SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow,"nav-toolbar-menu")).select(folders[0]);
						return true;
					}catch(Exception e){
						return false;
					}
				}
				
			});
			NewToolbar ntb = dlg.getUI5Object(By.xpath(".//div[contains(@class,'sapEpmModelerToolbar') and contains(@id,'content-header')]"));
	    	ntb.waitUntilTextEndsWith(folders[0]);
	    	
	    	Table table = dlg.getUI5Object(By.xpath(".//div[contains(@class,'sapUiTable') and contains(@id,'content-grid')]"));
	    	for(int i=1;i<folders.length;i++ ){
	    		table.waitUntilTableInitialized();
	    		table.clickCell("Name", folders[i]);
	    		ntb.waitUntilTextEndsWith(folders[i]);
	    	}
		}
		if(dlg.getTitle().equalsIgnoreCase(Resource.getMessage("CREATE_DASHBOARD"))){
			((Input)dlg.getUI5Object(By.xpath(".//div[contains(@id,'txtDashTitle') and contains(@class,'sapEpmUiInput')]"))).setValue(name);
			if(desc!=null){
				((Input)dlg.getUI5Object(By.xpath(".//div[contains(@id,'txtDashDesc') and contains(@class,'sapEpmUiInput')]"))).setValue(desc);
			}
		}else{
			((Input)dlg.getUI5Object(By.xpath(".//*[contains(@class,'sapEpmUiInput') and contains(@id,'name')]"))).setValue(name,false);
			if(desc!=null){
				((Input)dlg.getUI5Object(By.xpath(".//*[contains(@class,'sapEpmUiInput') and contains(@id,'desc')]"))).setValue(desc,false);
			}
		}	
		SapUi5Utilities.waitUntilMessageDisappear(defaultWindow.driver());
		dlg.ok();

		try{
			
			Dialog overwritendlg = SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "overwrite-dialog",3);
			defaultWindow.driver().manage().timeouts().implicitlyWait((long) 1, TimeUnit.SECONDS);
			SapUi5Utilities.waitUntilMessageDisappear(defaultWindow.driver());
			overwritendlg.ok();
			
		}catch(Exception e){}
		defaultWindow.driver().manage().timeouts().implicitlyWait((long) 10, TimeUnit.SECONDS);
	}
	
	

	public static void assertReportIsOpen(BrowserWindow defaultWindow,String RptName){
		Grid grid = SapUi5Factory.findUI5ObjectByClass(defaultWindow, "sapEpmUiControlAnalyticgridGrid");
		grid.waitUntilGridInitialized();
	}
	
	public static void pinReportToHomeScreen(BrowserWindow defaultWindow){
		((ButtonToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "reportPushActionBtn")).click();
		com.sap.ui5.selenium.sap.fpa.ui.dashboard.DashboardPublishMenu pinMenu = SapUi5Factory.findUI5ObjectByClass(defaultWindow, "sapUiMnu");
		SapUi5Utilities.waitUntilMessageDisappear(defaultWindow.driver());
		pinMenu.selectItem(Resource.getMessage("PUBLISH_TO_HS"));
	}
	
	public static void pinReportToNewDashbord(BrowserWindow defaultWindow,String tileName,String tileDesc,String folder){
		((ButtonToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "reportPushActionBtn")).click();
		com.sap.ui5.selenium.sap.fpa.ui.dashboard.DashboardPublishMenu pinMenu = SapUi5Factory.findUI5ObjectByClass(defaultWindow, "sapUiMnu");
		pinMenu.selectItem(Resource.getMessage("CREATE_NEW_DB"));
		saveToContentLib(defaultWindow,tileName, tileDesc,folder);
	}
	
	/**
	 * 
	 * @param number, "12345.789"
	 * @param precision, decimal precision
	 * @return
	 */
	public static String formatNumber(double number, int precision){
		String language = System.getProperty("language");
		language = language!=null?language:"en";
		NumberFormat formater = NumberFormat.getNumberInstance();
		List<String> dotSeparateLang = Arrays.asList("en", "zh_TW", "ja"); //country separate decimal by '.'
		List<String> commaSeparateLang = Arrays.asList("de", "fr", "es", "pt", "ru", "it"); //country separate decimal by ','
		if(dotSeparateLang.contains(language)) {
			formater = NumberFormat.getNumberInstance(Locale.CHINA);
		}
		else if(commaSeparateLang.contains(language)) {
			formater = NumberFormat.getNumberInstance(Locale.GERMAN);
		}
		formater.setMaximumFractionDigits(precision);
		formater.setMinimumFractionDigits(precision);
		return formater.format(number);
	}

	/**
	 * 
	 * @param defaultWindow
	 * @param position "A1","B12"
	 * @return
	 */
	public static String getCellValue(BrowserWindow defaultWindow,String position){
		Grid grid = defaultWindow.find(By.className("sapEpmUiControlAnalyticgridGrid"));
		String val = grid.getCellString(position);
		return val;
	}
	
	
	/**
	 * 
	 * @param defaultWindow
	 * @param position "A1","B12"
	 * @param timeout
	 * @param expectedValue
	 */
	public static void checkCellValue(BrowserWindow defaultWindow,String position ,final String expectedValue,long timeout){
		Grid grid = defaultWindow.find(By.className("sapEpmUiControlAnalyticgridGrid"));
		grid.waitUntilHasValue(position,expectedValue,timeout);
	}
	
	public static void changeCellValue(BrowserWindow defaultWindow, String position, String value){
		Grid grid = defaultWindow.find(By.className("sapEpmUiControlAnalyticgridGrid"));
		grid.changeCellValue(position, value);
	}
	
	public static void DrillRows(BrowserWindow defaultWindow, int x, int y) {
        Grid grid = defaultWindow.find(By.className("sapEpmUiControlAnalyticgridGrid"));
        grid.myDrillRow(defaultWindow, x,y);
    }
	

	private static void operaSingleVersion(BrowserWindow defaultWindow,String versionName, String operation) {
		
		WebElement dlg = SapUi5Factory.findWebElementByIdEndsWith(defaultWindow, "versionMgmt");
		List<WebElement> versionList = dlg.findElements(By.className("sapEpmVersionManagementItem"));
		for(WebElement ver:versionList){
			if(ver.getText().equalsIgnoreCase(versionName)){
				ver.findElements(By.xpath(".//div[@title='"+operation+"']")).get(0).click();
				return;
			}
		}
	}
	
	public static void copyVersion( BrowserWindow defaultWindow, String from, String versionName)
	{
		ButtonToolbarItem verBTN = SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "versionMgmtBtn");
		verBTN.click();

		operaSingleVersion(defaultWindow,from, Resource.getMessage("VER_MGMT_COPY_BUTTON_TOOLTIP"));
		
		Dialog dg = SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "-versionMgmt-Copy");
		DialogInput di = dg.getUI5Object(By.xpath(".//div[@class='DialogInput']/input[last()]"));
		di.setValue(versionName, false);
		dg.ok();

	}
	
	public static void deleteVersion(final BrowserWindow defaultWindow, String versionName)
	{
		ButtonToolbarItem verBTN = SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "versionMgmtBtn");
		verBTN.click();

		operaSingleVersion(defaultWindow,versionName, "Delete");
		Dialog dg = SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "-confirmdelete-dialog");
		dg.ok();

	}
	
	/**
	 * 
	 * @param defaultWindow
	 * @param from is the original version name
	 * @param isNewVersion: whether to push as a new version or not
	 * @param versionName: the target version name to publish
	 * @param Category: if it is a new version, this is mandatory to define. otherwise you can give null if it overwrites the existing version.
	 */
	public static void publishVersion(BrowserWindow defaultWindow,String from, boolean isNewVersion,  String versionName, String Category)
	{
		ButtonToolbarItem verBTN = SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "versionMgmtBtn");
		verBTN.click();
		operaSingleVersion(defaultWindow,from, Resource.getMessage("VER_MGMT_PUBLISH_BUTTON_TOOLTIP"));
		Dialog dg = SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "-versionMgmt-Publish");
		if(!isNewVersion){
			((RadioButton)dg.getUI5Object(By.xpath(".//*[text()='"+Resource.getMessage("VER_MGMT_PUBLISH_RADIOBUTTON_TEXT")+"']/parent::div[contains(@class,'sapMRb')]"))).select();
			((ComboBox)dg.getUI5Object(By.xpath(".//*[@role='combobox']"))).setValue(versionName, false);
		}
		else{
			((RadioButton)dg.getUI5Object(By.xpath(".//*[text()='"+Resource.getMessage("VER_MGMT_COPYTO_NEW_TITLE")+"']/parent::div[contains(@class,'sapMRb')]"))).select();
			((ComboBox)dg.getUI5Object(By.xpath(".//*[@role='combobox']"))).setValue(Category, false);
			DialogInput di = dg.getUI5Object(By.xpath(".//div[@class='DialogInput']/input[last()]"));
			di.setValue(versionName, false);
		}
		dg.ok();
	}
	
	public static boolean isSaveReportDialogOpen(BrowserWindow defaultWindow){
		try{
			Dialog dg = SapUi5Factory.findUI5ObjectByClass(defaultWindow, "sapEpmUiDialog");
			if(dg.isDisplay() && dg.getTitle().equalsIgnoreCase("Save Report"))
			{
				return true;
			}else{
			    return false;	
			}
		}catch(NoSuchElementException ex){
			return false;
		}catch(ControlNotFoundException ex){
			return false;
		}
	}
	
	public static void dragDropPerspective(BrowserWindow bw,String fromPerspectiveName, String to){
		GridLayouter gl = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "grid-layouter");
		gl.dragDropPerspective(fromPerspectiveName, to);
	}
	
	public static void setMemberFilter(BrowserWindow defaultWindow, String IDAndDesc, Boolean isHierarchy,String... items)
	{
		MemberSelector ms = SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "Member-Filter");
		if(isHierarchy!=null&& isHierarchy){
			ms.changeDisplayView(defaultWindow, "parentId");
		}
		ms.multiSearch(IDAndDesc, items);
		ms.ok();
	}

	
	public static void setFilter(BrowserWindow defaultWindow,String nameOfPerspective,String IdDescription, Boolean isHierarchy, String... items){
		try{
			SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "Member-Filter",3);
		}catch(Exception e){
			GridLayouter gl = SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "grid-layouter");
			gl.clickFilterByPerspectiveName(nameOfPerspective);
		}
		
		setMemberFilter(defaultWindow,IdDescription,isHierarchy,items);
	}
	
	public static void setAttributes(BrowserWindow defaultWindow,String nameOfPerspective,String[] items)
	{
		GridLayouter gl = SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "grid-layouter");
		gl.clickAttributesByPerspectiveName(nameOfPerspective);
		
		Dialog dlg = SapUi5Factory.findUI5ObjectByClass(defaultWindow, "sapMDialog");
		
		for(int i = 0; i < items.length; i ++)
		{
			By byattr = By.xpath(".//div[.//div[text()='"+items[i]+"'] and @class='sapEPMUIGridLayouterListItem']//div[@class='sapEPMUIGridLayouterListItemCheckbox']");
			WebElement attribute = dlg.getWebElement(byattr);
			attribute.click();
		}
		dlg.ok();
	}
	
	public static void focusOnCell(BrowserWindow defaultWindow,String cellCoords){
		Grid grid = SapUi5Factory.findUI5ObjectByClass(defaultWindow, "sapEpmUiControlAnalyticgridGrid");
		grid.selectCell(cellCoords);
	}

	
	public static void openSummary(BrowserWindow defaultWindow,String summaryName)
	{
		TabContainer tbc = SapUi5Factory.findUI5ObjectByClass(defaultWindow,"sapEpmUiTabContainer");
		tbc.clickTab(summaryName);
	}
	
	
	/**
	 * All parameters are case sensitive, following is the value example
	 * range: "A1:D5" or null
	 * fontSize: "36px" or null
	 * fontColor: "rgb(255, 170, 73)" or null
	 * fontType: "Verdana" or null
	 * bold: boolean or null
	 * italic: boolean or null
	 * underline: boolean or null
	 * strike: boolean or null
	 * alignment:"Set text alignment to right" or null
	 */
	public static void setFont(BrowserWindow defaultWindow,String range, String fontSize, String fontColor, String fontType,Boolean bold,Boolean italic,Boolean underline, Boolean strike, String alignment){
		Grid grid = SapUi5Factory.findUI5ObjectByClass(defaultWindow, "sapEpmUiControlAnalyticgridGrid");
		grid.selectRange(range);
		if(fontSize!=null){
			DropDown fontSizeCB = SapUi5Factory.findUI5ObjectByLocator(defaultWindow, By.xpath("//div[@title='"+Resource.getMessage("SUMMARY_DESIGNER_FONTSIZE")+"']"));
			fontSizeCB.setValue(fontSize);
		}
		if(fontColor!=null){
			By colortrigger = By.xpath("//label[contains(@class,'sapEpmUiFormItemTitle') and text()='"+Resource.getMessage("SUMMARY_DESIGNER_FORMAT_COLOR")+"']/parent::*//span[contains(@class,'sapEpmUiSelectButtonArrowIcon')]");
			SapUi5Factory.findWebElementByLocator(defaultWindow, colortrigger).click();
			By colorLocator = By.xpath("//div[@class='sapEPMUIMenu sapEPMUIMenuHorizontal' and contains(@style,'display: block')]//span[contains(@style,'"+fontColor+"')]");
			SapUi5Factory.findWebElementByLocator(defaultWindow, colorLocator).click();
		}
		if(fontType!=null){
			DropDown fontTypeCB = SapUi5Factory.findUI5ObjectByLocator(defaultWindow, By.xpath("//div[@title='"+Resource.getMessage("SUMMARY_DESIGNER_FONTTYPE")+"']"));

			fontTypeCB.setValue(fontType);
		}
		setFontFace(defaultWindow,range, bold, italic, underline,  strike);

		if(alignment!=null){
			WebElement alignmentBtn = SapUi5Factory.findWebElementByLocator(defaultWindow, By.xpath("//button[@title='"+alignment+"']"));
			if(!alignmentBtn.getAttribute("class").contains("sapEpmUiButtonPressed")){
				((Button)SapUi5Factory.findUI5ObjectById(defaultWindow, alignmentBtn.getAttribute("id"))).click();
			}
		}
	}
	
	
	/**
	 * 
	 * mergeORSplit: 1 or 2, 1 means merge, or null, any other number is not valid
	 * fillColor: "rgb(255, 170, 73)" or null
	 * addORdeleteRow: 1,2
	 * addORdeleteColumn:1,2
	 * 
	 */
	public static void setCellStyle(BrowserWindow defaultWindow,String range,Integer mergeORSplit, String fillColor, Integer addORdeleteRow, Integer addORdeleteColumn){
		Grid grid = SapUi5Factory.findUI5ObjectByClass(defaultWindow, "sapEpmUiControlAnalyticgridGrid");
		grid.selectRange(range);
		//MERGE OR SPLIT
		if(mergeORSplit!=null){                                    
			By mergetrigger = By.xpath("//label[contains(@class,'sapEpmUiFormItemTitle') and text()='"+Resource.getMessage("SUMMARY_DESIGNER_FORMAT_COLOR")+"']/parent::*//span[contains(@class,'sapEpmUiSelectButtonArrowIcon')]");
			
			SapUi5Factory.findWebElementByLocator(defaultWindow, mergetrigger).click();
			By mergeOrSplit = By.xpath("//div[@class='sapEPMUIMenu sapEPMUIMenuHorizontal' and contains(@style,'display: block')]//div["+mergeORSplit+"]");
			SapUi5Factory.findWebElementByLocator(defaultWindow, mergeOrSplit).click();
		}
		//fILL color for cell
		if(fillColor!=null){
			By fillTrigger = By.xpath("//label[contains(@class,'sapEpmUiFormItemTitle') and text()='"+Resource.getMessage("SUMMARY_DESIGNER_FORMAT_COLOR")+"']/parent::*//span[contains(@class,'sapEpmUiSelectButtonArrowIcon')]");
			SapUi5Factory.findWebElementByLocator(defaultWindow, fillTrigger).click();
			By fillLocator = By.xpath("//div[@class='sapEPMUIMenu sapEPMUIMenuHorizontal' and contains(@style,'display: block')]//span[contains(@style,'"+fillColor+"')]");
			SapUi5Factory.findWebElementByLocator(defaultWindow, fillLocator).click();
		}
		//add/delete row/column
		if(addORdeleteRow!=null){
			if(addORdeleteRow==1){
				WebElement addrowBtn = SapUi5Factory.findWebElementByLocator(defaultWindow, By.xpath("//button[@title='"+Resource.getMessage("SUMMARY_DESIGNER_ROW_ADD")+"']")); 
				if(!addrowBtn.getAttribute("class").contains("sapEpmUiButtonPressed")){
					((Button)SapUi5Factory.findUI5ObjectById(defaultWindow, addrowBtn.getAttribute("id"))).click();
				}
			}else if(addORdeleteRow==2){
				WebElement deleterowBtn = SapUi5Factory.findWebElementByLocator(defaultWindow, By.xpath("//button[@title='"+Resource.getMessage("SUMMARY_DESIGNER_ROW_DELETE")+"']"));
				if(!deleterowBtn.getAttribute("class").contains("sapEpmUiButtonPressed")){
					((Button)SapUi5Factory.findUI5ObjectById(defaultWindow, deleterowBtn.getAttribute("id"))).click();
				}
			}
		}
		
		if(addORdeleteColumn!=null){
			if(addORdeleteColumn==1){
				WebElement addrowBtn = SapUi5Factory.findWebElementByLocator(defaultWindow, By.xpath("//button[@title='"+Resource.getMessage("SUMMARY_DESIGNER_COLUMN_ADD")+"']")); 
				if(!addrowBtn.getAttribute("class").contains("sapEpmUiButtonPressed")){
					((Button)SapUi5Factory.findUI5ObjectById(defaultWindow, addrowBtn.getAttribute("id"))).click();
				}
			}else if(addORdeleteColumn==2){
				WebElement deleterowBtn = SapUi5Factory.findWebElementByLocator(defaultWindow, By.xpath("//button[@title='"+Resource.getMessage("SUMMARY_DESIGNER_COLUMN_DELETE")+"']")); 
				if(!deleterowBtn.getAttribute("class").contains("sapEpmUiButtonPressed")){
					((Button)SapUi5Factory.findUI5ObjectById(defaultWindow, deleterowBtn.getAttribute("id"))).click();
				}
			}
		}
		
	}
	
	
	/**
	 * 
	 */
	public static void setFontFace(BrowserWindow defaultWindow, String coord, Boolean bold,Boolean italic,Boolean underline, Boolean strike )
	{
		Grid grid = SapUi5Factory.findUI5ObjectByClass(defaultWindow, "sapEpmUiControlAnalyticgridGrid");
		grid.selectRange(coord);
		
		//font bold
		if(bold!=null){
			WebElement emphasizeBtn = SapUi5Factory.findWebElementByLocator(defaultWindow, By.xpath("//button[@title='"+Resource.getMessage("SUMMARY_DESIGNER_BOLD")+"']")); 
			if(bold){
				if(!emphasizeBtn.getAttribute("class").contains("sapEpmUiButtonPressed")){
					((Button)SapUi5Factory.findUI5ObjectById(defaultWindow, emphasizeBtn.getAttribute("id"))).click();
				}
			}else{
				if(emphasizeBtn.getAttribute("class").contains("sapEpmUiButtonPressed")){
					((Button)SapUi5Factory.findUI5ObjectById(defaultWindow, emphasizeBtn.getAttribute("id"))).click();
				}
			}
		}
		
		//Set/remove italic style
		if(italic!=null){
			WebElement italicBtn = SapUi5Factory.findWebElementByLocator(defaultWindow, By.xpath("//button[@title='"+Resource.getMessage("SUMMARY_DESIGNER_ITALIC")+"']"));
			if(italic){
				if(!italicBtn.getAttribute("class").contains("sapEpmUiButtonPressed")){
					((Button)SapUi5Factory.findUI5ObjectById(defaultWindow, italicBtn.getAttribute("id"))).click();
				}
			}else{
				if(italicBtn.getAttribute("class").contains("sapEpmUiButtonPressed")){
					((Button)SapUi5Factory.findUI5ObjectById(defaultWindow, italicBtn.getAttribute("id"))).click();
				}
			}
		}
		
		//Set/remove underline OR Set/remove strike
		if(underline!=null){
			WebElement underlineBtn = SapUi5Factory.findWebElementByLocator(defaultWindow, By.xpath("//button[@title='"+Resource.getMessage("SUMMARY_DESIGNER_UNDERLINE")+"']")); 
			if(underline){
				if(!underlineBtn.getAttribute("class").contains("sapEpmUiButtonPressed")){
					((Button)SapUi5Factory.findUI5ObjectById(defaultWindow, underlineBtn.getAttribute("id"))).click();
				}
			}else{
				if(underlineBtn.getAttribute("class").contains("sapEpmUiButtonPressed")){
					((Button)SapUi5Factory.findUI5ObjectById(defaultWindow, underlineBtn.getAttribute("id"))).click();
				}
			}
		}
		
		if(strike!=null){
			WebElement strikeBtn = SapUi5Factory.findWebElementByLocator(defaultWindow, By.xpath("//button[@title='"+Resource.getMessage("SUMMARY_DESIGNER_STRIKE")+"']")); 
			if(strike){
				if(!strikeBtn.getAttribute("class").contains("sapEpmUiButtonPressed")){
					((Button)SapUi5Factory.findUI5ObjectById(defaultWindow, strikeBtn.getAttribute("id"))).click();
				}
			}else{
				if(strikeBtn.getAttribute("class").contains("sapEpmUiButtonPressed")){
					((Button)SapUi5Factory.findUI5ObjectById(defaultWindow, strikeBtn.getAttribute("id"))).click();
				}
			}	
		}
	}
	
	public static void selectRange(BrowserWindow defaultWindow, String range){
		Grid grid = SapUi5Factory.findUI5ObjectByClass(defaultWindow, "sapEpmUiControlAnalyticgridGrid");
		grid.selectRange(range);
	}
	
	

	
	public static void copyPasteFromDrillstate(BrowserWindow defaultWindow, String key){
		((MenuToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "copyActionBtn")).click();
		Menu mn = SapUi5Factory.findUI5ObjectByClass(defaultWindow, "sapEPMUIMenu");
		mn.select(key);
		
	}	
	
	
	public static boolean checkVersionExist(BrowserWindow defaultWindow, String versionName) {
		NewToolbarItem versionManagement = SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "versionMgmtBtn");
		versionManagement.click();
		WebElement dlg = SapUi5Factory.findWebElementByIdEndsWith(defaultWindow, "versionMgmt");
		List<WebElement> versionList = dlg.findElements(By.className("sapEpmVersionManagementItem"));
		for(WebElement ver:versionList){
			if(ver.getText().equalsIgnoreCase(versionName)){
				return true;
			}
		}
		return false;
	}
	
	public static void addSummary(BrowserWindow defaultWindow, String summaryName){
		NewToolbarItem add = SapUi5Factory.findUI5ObjectByIdEndsWith(defaultWindow, "addTabButton");
		add.click();
		NewToolbarMenu menu = SapUi5Factory.findUI5ObjectByClass(defaultWindow, "paGridMenuPopover");
		menu.select(Resource.getMessage("ADD_SUMMARY")); 
		Dialog dlg = SapUi5Factory.findUI5ObjectByClass(defaultWindow, "sapMDialog");
		Input input = dlg.getUI5Object(By.className("sapEpmUiInput"));
		input.setValue(summaryName, false);
		dlg.ok();
	}
	
	
	
		
		
}
