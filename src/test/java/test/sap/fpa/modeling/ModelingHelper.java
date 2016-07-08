package test.sap.fpa.modeling;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.commons.ComboBox;
import com.sap.ui5.selenium.commons.TextField;
import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.sap.fpa.ui.control.commons.Input;
import com.sap.ui5.selenium.sap.fpa.ui.control.memberselector.MemberSelector;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.OptionSlider;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.datagrid.DataGrid;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarItem;
import com.sap.ui5.selenium.sap.fpa.ui.control.modeller.toolbar.NewToolbarMenu;
import com.sap.ui5.selenium.sap.fpa.ui.control.repeat.Repeat;
import com.sap.ui5.selenium.sap.fpa.ui.control.tabContainer.TabContainer;
import com.sap.ui5.selenium.sap.m.CheckBox;
import com.sap.ui5.selenium.sap.m.Dialog;
import com.sap.ui5.selenium.sap.m.SearchField;
import com.sap.ui5.selenium.sap.m.Switch;
import com.sap.ui5.selenium.sap.ui.unified.Menu;
import com.sap.ui5.selenium.utilities.Resource;
import com.sap.ui5.selenium.utilities.SapUi5Factory;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;


public class ModelingHelper {

	private final static String ACCOUNT_PERSPECTIVE_NAME = Resource.getMessage("MODELS_ACCOUNTS");
	private final static String CREATE_NEW_PERSPECTIVE = Resource.getMessage("MODELS_NEW_DIMEN");
	private final static String SELECT_EXISTING_PERSPECTIVE = Resource.getMessage("MODELS_SELECT_EXIST");
	private final static String PREFERENCES = Resource.getMessage("UTILITY_PREFERENCE");
	private final static String YEAR = Resource.getMessage("TIME_GRANULARITY_YEAR");


	public static void createModel(BrowserWindow bw, String modelName, String modelDescription, Boolean isPlanningEnabled) {
		((TabContainer) SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-dimension-tab-container")).clickTab(Resource.getMessage("UTILITY_MODELER"));
		Repeat.findRepeatByIdEndsWith(bw, "repeat").watiUntilRefreshed();

		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-model-createBtn")).click();
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-createDialog");
		((TextField)dlg.getUI5ObjectByIdEndsWith("name")).setValue(modelName, false);
		((TextField)dlg.getUI5ObjectByIdEndsWith("desc")).setValue(modelDescription, false);
		CheckBox planingEnabledCB = dlg.getUI5ObjectByIdEndsWith("modelPlanningFlag");
		if(isPlanningEnabled && !planingEnabledCB.isChecked()){
			planingEnabledCB.check();
		}
		else if(!isPlanningEnabled && planingEnabledCB.isChecked()){
			planingEnabledCB.uncheck();
		}
		dlg.ok();
	}
	/*
	* @param secured, if "" means don't handel secured,"on" means turn secured on, "off" means turn off
	*/
	public static void configureModelPreference(BrowserWindow bw, String desc, String defaultCurrency, boolean auditOn, String currency, boolean secured) {
		TabContainer container = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-dimension-tab-container");
		container.clickLeftToolbarButton();
		NewToolbarMenu menu = SapUi5Factory.findUI5ObjectByClass(bw, "sapEpmModelerToolbarMenu");
		menu.select(PREFERENCES);

		Dialog dialog = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "models-preferenceDialog");
		
		((TextField)dialog.getUI5ObjectByIdEndsWith("dimensionDescText")).setValue(desc, false);
		if(defaultCurrency!=null && !defaultCurrency.equalsIgnoreCase("")){
			((TextField)dialog.getUI5ObjectByIdEndsWith("defaultCurrencyText")).setValue(defaultCurrency, false);
		}
		Switch auditSwitch = dialog.getUI5ObjectByIdEndsWith("auditFlag");
		if (auditOn) {
			auditSwitch.switchOn();
		} else {
			auditSwitch.switchOff();
		}

		Switch currencyswitch = dialog.getUI5ObjectByIdEndsWith("currencyFlag");

		if (currency != null && !currency.equalsIgnoreCase("")) {
			currencyswitch.switchOn();
				((ComboBox)dialog.getUI5ObjectByIdEndsWith("currencyBox")).setValue(currency);
		} else {
			currencyswitch.switchOff();
		}
		Switch securedSwitch = dialog.getUI5ObjectByIdEndsWith("securedFlag");
		if(secured) {
			securedSwitch.switchOn();
		}else{
			securedSwitch.switchOff();
		}

		dialog.ok();
	}

	public static void createNewCurrency(BrowserWindow bw, String currencyname, String currencyDesc, String dataContent) {

		((TabContainer) SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-dimension-tab-container")).clickTab(Resource.getMessage("UTILITY_CURRS"));
		Repeat.findRepeatByIdEndsWith(bw, "repeat").watiUntilRefreshed();
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "curr-addBtn")).click();
		
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "currs-createDialog");
		((TextField)dlg.getUI5ObjectByIdEndsWith("name")).setValue(currencyname, false);
		((TextField)dlg.getUI5ObjectByIdEndsWith("desc")).setValue(currencyDesc, false);
		dlg.ok();

		inputPerspectiveData(bw, dataContent);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		SapUi5Utilities.waitUntilMessageDisappear(bw.driver());
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "currencyView-saveToolbarItem")).click();

	}

	public static void updatePerspective(BrowserWindow bw, String perspectiveName, String dataContent) {
		TabContainer container = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-dimension-tab-container");

		container.clickTab(perspectiveName);
		inputPerspectiveData(bw, dataContent);
	}

	public static void configureTimeCategories(BrowserWindow bw, String granularity, String startdate, String enddate, String budget,
			String planning, String forecast, String range, String rollforecast, Integer lookahead,Integer lookback) {
		if(budget==null){
			((TabContainer)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "dimension-tab-container")).clickTab(Resource.getMessage("MODELS_TIME"));
		}else{
			((TabContainer)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "dimension-tab-container")).clickTab(Resource.getMessage("MODELS_TIME_VERSION"));
		}
		
		
		// Set the granularity
		OptionSlider slider = SapUi5Factory.findUI5ObjectByClass(bw, "sapEpmOptionSlider");
		slider.select(granularity);

		List<ComboBox> cbs = SapUi5Factory.findUI5Objects(bw, By.className("sapUiTfCombo"));
		
		String[] start = startdate.split("/");
		String[] end = enddate.split("/");
		if(budget!=null){
			try {
				budget = budget.trim().substring(0, 1).toUpperCase()
						.concat(budget.trim().substring(1, budget.length()).toLowerCase());
			} catch (Exception e) {
				budget = YEAR;
			}
			try {
				planning = planning.trim().substring(0, 1).toUpperCase()
						.concat(planning.trim().substring(1, planning.length()).toLowerCase());
			} catch (Exception e) {
				planning = YEAR;
			}
			try {
				forecast = forecast.trim().substring(0, 1).toUpperCase()
						.concat(forecast.trim().substring(1, forecast.length()).toLowerCase());
			} catch (Exception e) {
				forecast = YEAR;
			}
			try {
				rollforecast = rollforecast.trim().substring(0, 1).toUpperCase()
						.concat(rollforecast.trim().substring(1, rollforecast.length()).toLowerCase());
			} catch (Exception e) {
				rollforecast = YEAR;
			}
		}
		
		if (granularity.trim().equalsIgnoreCase(Resource.getMessage("TIME_GRANULARITY_YEAR"))) {

			cbs.get(0).setValue(start[0]);
			cbs = SapUi5Factory.findUI5Objects(bw, By.className("sapUiTfCombo"));
			
			cbs.get(1).setValue(end[0]);
			// set category
			if(budget!=null){
				cbs.get(2).setValue(budget);
				cbs.get(3).setValue(planning);
				cbs.get(4).setValue(forecast);
				cbs.get(5).setValue(range);
				cbs.get(6).setValue(rollforecast);
				List<TextField> rollingforecasts = SapUi5Factory.findUI5Objects(bw, By.xpath("//input[contains(@class,'sapEpmCategoryListLookAhead')]"));
				rollingforecasts.get(0).clearValue();
				rollingforecasts.get(0).setValue(String.valueOf(lookahead));
				rollingforecasts.get(1).clearValue();
				rollingforecasts.get(1).setValue(String.valueOf(lookback));
			}
		} else if (granularity.trim().equalsIgnoreCase(Resource.getMessage("TIME_GRANULARITY_QUARTER"))) {

			cbs.get(0).setValue(start[0]);
			cbs = SapUi5Factory.findUI5Objects(bw, By.className("sapUiTfCombo"));
			
			try {
				
				cbs.get(1).selectItem(Integer.parseInt(start[1]) - 1);
			} catch (Exception e) {
				cbs.get(1).setValue(start[1]);
			}
			cbs = SapUi5Factory.findUI5Objects(bw, By.className("sapUiTfCombo"));
			
			cbs.get(2).setValue(end[0]);
			cbs = SapUi5Factory.findUI5Objects(bw, By.className("sapUiTfCombo"));
			
			try {
				
				cbs.get(3).selectItem(Integer.parseInt(end[1]) - 1);
			} catch (Exception e) {
				cbs.get(3).setValue(end[1]);
			}
			// set category
			if(budget!=null){
				cbs.get(4).setValue(budget);
				cbs.get(5).setValue(planning);
				cbs.get(6).setValue(forecast);
				cbs.get(7).setValue(range);
				cbs.get(8).setValue(rollforecast);
				List<TextField> rollingforecasts = SapUi5Factory.findUI5Objects(bw, By.xpath("//input[contains(@class,'sapEpmCategoryListLookAhead')]"));
				rollingforecasts.get(0).clearValue();
				rollingforecasts.get(0).setValue(String.valueOf(lookahead));
				rollingforecasts.get(1).clearValue();
				rollingforecasts.get(1).setValue(String.valueOf(lookback));
			}
		} else if (granularity.trim().equalsIgnoreCase(Resource.getMessage("TIME_GRANULARITY_MONTH"))) {

			cbs.get(0).setValue(start[0]);
			cbs = SapUi5Factory.findUI5Objects(bw, By.className("sapUiTfCombo"));
			
			try {
				cbs.get(1).selectItem(Integer.parseInt(start[1]) - 1);
			} catch (Exception e) {
				cbs.get(1).setValue(start[1]);
			}
			cbs = SapUi5Factory.findUI5Objects(bw, By.className("sapUiTfCombo"));
			
			cbs.get(2).setValue(end[0]);
			cbs = SapUi5Factory.findUI5Objects(bw, By.className("sapUiTfCombo"));
			
			try {
				cbs.get(3).selectItem(Integer.parseInt(end[1]) - 1);
			} catch (Exception e) {
				cbs.get(3).setValue(end[1]);
			}
			// set category
			if(budget!=null){
				cbs.get(4).setValue(budget);
				cbs.get(5).setValue(planning);
				cbs.get(6).setValue(forecast);
				cbs.get(7).setValue(range);
				cbs.get(8).setValue(rollforecast);
				List<TextField> rollingforecasts = SapUi5Factory.findUI5Objects(bw, By.xpath("//input[contains(@class,'sapEpmCategoryListLookAhead')]"));
				rollingforecasts.get(0).clearValue();
				rollingforecasts.get(0).setValue(String.valueOf(lookahead));
				rollingforecasts.get(1).clearValue();
				rollingforecasts.get(1).setValue(String.valueOf(lookback));
			}
		} else if (granularity.trim().equalsIgnoreCase(Resource.getMessage("TIME_GRANULARITY_WEEK"))) {

			cbs.get(0).setValue(start[0]);
			cbs = SapUi5Factory.findUI5Objects(bw, By.className("sapUiTfCombo"));

			cbs.get(1).setValue(start[1]);
			cbs = SapUi5Factory.findUI5Objects(bw, By.className("sapUiTfCombo"));

			cbs.get(2).selectItem(Integer.parseInt(start[2]) - 1);
			cbs = SapUi5Factory.findUI5Objects(bw, By.className("sapUiTfCombo"));

			cbs.get(3).setValue(end[0]);
			cbs = SapUi5Factory.findUI5Objects(bw, By.className("sapUiTfCombo"));

			cbs.get(4).setValue(end[1]);
			cbs = SapUi5Factory.findUI5Objects(bw, By.className("sapUiTfCombo"));

			cbs.get(5).selectItem(Integer.parseInt(end[2]) - 1);
			// set category
			if(budget!=null){
				cbs.get(6).setValue(budget);
				cbs.get(7).setValue(planning);
				cbs.get(8).setValue(forecast);
				cbs.get(9).setValue(range);
				cbs.get(10).setValue(rollforecast);
				List<TextField> rollingforecasts = SapUi5Factory.findUI5Objects(bw, By.xpath("//input[contains(@class,'sapEpmCategoryListLookAhead')]"));
				rollingforecasts.get(0).clearValue();
				rollingforecasts.get(0).setValue(String.valueOf(lookahead));
				rollingforecasts.get(1).clearValue();
				rollingforecasts.get(1).setValue(String.valueOf(lookback));
			}
		}


	}

	public static void createNewPerspective(BrowserWindow bw, String perspectiveType, String perspectiveName, String perspectiveDescription,Boolean areacode, String dataContent) {
		((TabContainer) SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-dimension-tab-container")).clickRightToolbarButton();
		NewToolbarMenu menu = SapUi5Factory.findUI5ObjectByClass(bw, "sapEpmModelerToolbarMenu");
		menu.select(CREATE_NEW_PERSPECTIVE);
		
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-dimCreateDialog");
		ComboBox type = dlg.getUI5ObjectByIdEndsWith("-type-dropbox");
		type.setValue(perspectiveType);
		((TextField)dlg.getUI5ObjectByIdEndsWith("-focus")).setValue(perspectiveName, false);
		((TextField)dlg.getUI5ObjectByIdEndsWith("-unfocus")).setValue(perspectiveDescription, false);

		if (perspectiveType.equalsIgnoreCase("Organization")) {
			if(areacode!=null){
				com.sap.ui5.selenium.sap.m.CheckBox CB = dlg.getUI5ObjectByIdEndsWith("areaCode");

				if (areacode) {
					if (!CB.isChecked()) CB.check();
				} else{
					if (CB.isChecked()) CB.uncheck();
				}
			}
		}
		dlg.ok();
		
		if (perspectiveType.equalsIgnoreCase(Resource.getMessage("UTILITY_DIMEN_ORG"))) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {}
		}
		inputPerspectiveData(bw, dataContent);
		if (perspectiveType.equalsIgnoreCase(Resource.getMessage("UTILITY_DIMEN_ORG"))) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {}
		}

	}

	public static void createWithExistingPerspective(BrowserWindow bw, String perspectiveName) {
		TabContainer container = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-dimension-tab-container");

		container.clickRightToolbarButton();
		NewToolbarMenu menu = SapUi5Factory.findUI5ObjectByClass(bw, "sapEpmModelerToolbarMenu");
		menu.select(SELECT_EXISTING_PERSPECTIVE);
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-dimCreateDialog");
		SearchField searchField =dlg.getUI5ObjectByIdEndsWith("-search");
		searchField.setValue(perspectiveName);
		searchField.triggerSearch();
		dlg.getWebElement(By.xpath(".//td//label[text()='"+perspectiveName+"']")).click();
		dlg.ok();
		
	}

	public static void createNewAccount(BrowserWindow bw, String accountName, String accountDescription, String dataContent) {
		TabContainer container = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-dimension-tab-container");
		container.clickTab(ACCOUNT_PERSPECTIVE_NAME);
		NewToolbarMenu menu = SapUi5Factory.findUI5ObjectByClass(bw, "sapEpmModelerToolbarMenu");
		menu.select(CREATE_NEW_PERSPECTIVE);
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-dimCreateDialog");
		((TextField) dlg.getUI5ObjectByIdEndsWith("-focus")).setValue(accountName,false);
		((TextField) dlg.getUI5ObjectByIdEndsWith("-unfocus")).setValue(accountDescription,false);
		dlg.ok();
		inputPerspectiveData(bw, dataContent);
	}

	public static void createWithExistingAccount(BrowserWindow bw, String accountName) {
		TabContainer container = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-dimension-tab-container");
		container.clickTab(ACCOUNT_PERSPECTIVE_NAME);
		NewToolbarMenu menu = SapUi5Factory.findUI5ObjectByClass(bw, "sapEpmModelerToolbarMenu");
		menu.select(SELECT_EXISTING_PERSPECTIVE);
		Dialog dlg = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-dimCreateDialog");
		SearchField searchField =dlg.getUI5ObjectByIdEndsWith("-search");
		searchField.setValue(accountName);
		searchField.triggerSearch();
		dlg.getWebElement(By.xpath(".//td//label[text()='"+accountName+"']")).click();
		dlg.ok();

	}

	private static void inputPerspectiveData(BrowserWindow bw, String dataContent) {
		DataGrid dataGrid = SapUi5Factory.findUI5ObjectByClass(bw, "sapEPMUIModellerGridModeller");
		dataGrid.selectFirstCell();
		dataGrid.pasteDataIntoGrid(dataContent);
		//dataGrid.selectFirstCell();
	}

	public static void saveModel(BrowserWindow bw) {
		SapUi5Utilities.waitUntilMessageDisappear(bw.driver());
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByLocator(bw, By.xpath("//div[@class='sapEpmUiTabContainerLeftToolbar']//div[@class='sapEpmModelerToolbarLeftItems']/div[2]"))).click();
		//SapUi5Utilities.waitWhenPageBusy(bw.driver());
	}

	public static void openModel(BrowserWindow bw, String modelname) {
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
		Repeat rpt = Repeat.findRepeatByIdEndsWith(bw, "repeat");
		rpt.clickRow( modelname);
		SapUi5Utilities.waitUntilPageBusy(bw.driver());
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
	}

	private static boolean selectByName(BrowserWindow bw, String... modelnames) {
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
		Repeat rpt = Repeat.findRepeatByIdEndsWith(bw, "repeat");
		return rpt.selectRows(modelnames);
	}

	public static void goToModelsTab(BrowserWindow bw) {
		((TabContainer) SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-dimension-tab-container")).clickTab(Resource.getMessage("UTILITY_MODELER"));
	}

	public static void goToPerspectivesTab(BrowserWindow bw) {
		((TabContainer) SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-dimension-tab-container")).clickTab(Resource.getMessage("UTILITY_DIMENS"));
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
	}

	public static void goToCurrenciesTab(BrowserWindow bw) {
		((TabContainer) SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-dimension-tab-container")).clickTab(Resource.getMessage("UTILITY_CURRS"));
	}

	public static void refresh(BrowserWindow bw) {
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-model-refreshBtn")).click();
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
	}

	public static void search(BrowserWindow bw, String modelname) {
		SearchField searchfield = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-seachField");
		searchfield.clearValue();
		searchfield.setValue(modelname);
		searchfield.triggerSearch();
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
	}

	public static boolean deleteModels(BrowserWindow bw, String... modelnames) {
		if(!selectByName(bw, modelnames)){
			return false;
		}
		((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "model-deleteBtn")).click();
		SapUi5Utilities.waitUntilMessageDisappear(bw.driver());
		((Dialog)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "deleteDialog")).ok();
		return true;
		
	}

	public static boolean deletePerspectivesByName(BrowserWindow bw, String... perspectivenames) {
		if(selectByName(bw, perspectivenames)){
			((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-dim-deleteBtn")).click();
			SapUi5Utilities.waitUntilMessageDisappear(bw.driver());
			((Dialog)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "deleteDialog")).ok();
			return true;
		}
		return false;
	}

	public static boolean deleteCurrenciesByName(BrowserWindow bw, String... currencynames) {
		if(selectByName(bw, currencynames)){
			((NewToolbarItem)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "-curr-deleteBtn")).click();
			((Dialog)SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "deleteDialog")).ok();
			return true;
		}
		return false;
	}

	public static boolean checkModelExist(BrowserWindow bw, String model) {
		try {
			Repeat rpt = Repeat.findRepeatByIdEndsWith(bw, "repeat");
			return rpt.checkItemExist(model);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean checkPerspectiveExist(BrowserWindow bw, String perspective) {
		try {
			Repeat rpt = Repeat.findRepeatByIdEndsWith(bw, "repeat");
			return rpt.checkItemExist(perspective);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean checkCurrencyExist(BrowserWindow bw, String currency) {
		try {
			Repeat rpt = Repeat.findRepeatByIdEndsWith(bw, "repeat");
			return rpt.checkItemExist(currency);
		} catch (Exception e) {
			return false;
		}

	}
	
	public static void assertCreatePermission(BrowserWindow bw, boolean expected) {
		((NewToolbarItem) SapUi5Factory.findUI5ObjectByIdEndsWith(bw,
				"-model-createBtn")).assertButtonEnabledStatus(expected);
	}
	
	public static void assertDeletePermission(BrowserWindow bw, boolean expected) {
		((NewToolbarItem) SapUi5Factory.findUI5ObjectByIdEndsWith(bw,
				"-model-deleteBtn")).assertButtonEnabledStatus(expected);
	}
	
	/**
	 * 
	 * @param bw
	 * @param analyticView
	 */
	public static void importModelFromHana(final BrowserWindow bw,String analyticView){
		new WebDriverWait(bw.driver(), 30).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver input) {
				try{
					((NewToolbarItem) SapUi5Factory.findUI5ObjectByIdEndsWith(bw,"model-importBtn")).click();
					Menu selection = Menu.findByIdEndsWith(bw, "model-importBtn-rootmenu");
					selection.selectItem(Resource.getMessage("UTILITY_CREATE_MODEL_FROM_HANA"));
					return true;
				}catch(Exception e){
					return false;
				}
			}
		});
		
		MemberSelector ms = SapUi5Factory.findUI5ObjectByIdEndsWith(bw, "analyticModel-selector");
		ms.singleSearch(Resource.getMessage("MEMBER_SELECTOR_ID"), analyticView, analyticView);
		ms.ok();
	}
	
	/**
	 * Find or create a BW connection, and create a query, this method implement importing model from bw connecting, please call data integration help to 
	 * automate the workflow in data integration page.
	 * @param bw 
	 * @param connectionName: new connection name
	 * @param R3Name: information of new connection
	 * @param applicationName: application server address of new connection
	 * @param sysNum: system number of application server
	 * @param clientID: client ID
	 * @param lanaguage: information of new connection
	 * @param lanaguage: information of new connection
	 * @param userName: login user of new connection
	 * @param pwd: password
	 * @param queryName: new connection name
	 * @param selectedQuery: selected Query
	 */
	public static void selectOtherModelFromBWViaNewConnection(final BrowserWindow bw,String connectionName, String R3Name,String applicationName,String sysNum,String clientID,String lanaguage,String userName,String pwd,String queryName,String selectedQuery){
		
		new WebDriverWait(bw.driver(), 30).until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver input) {
				try{
					((NewToolbarItem) SapUi5Factory.findUI5ObjectByIdEndsWith(bw,"model-importBtn")).click();
					Menu selection = Menu.findByIdEndsWith(bw, "model-importBtn-rootmenu");
					selection.selectItem(Resource.getMessage("DI_CHOOSE_OTHER_MODEL"));
					return true;
				}catch(Exception e){
					return false;
				}
			}
		});
		
		Dialog selectDataModelDlg = (Dialog)SapUi5Factory.findUI5ObjectByClass(bw, "sapEpmUiDialog");
		((ComboBox) selectDataModelDlg.getUI5ObjectByIdEndsWith("-select-conneciton-type")).setValue("BW");
		((ComboBox)selectDataModelDlg.getUI5ObjectByIdEndsWith("-select-conneciton-name")).setValue(Resource.getMessage("DI_CREATE_NEW_CONNECTION"),true);
		
		
		//Create new connection dlg
		Dialog newConnectionDlg = SapUi5Factory.findUI5ObjectByLocator(bw, By.xpath("//label[@class='sapEpmUiFormTitle' and text()='New Connection']/ancestor::div[contains(@class,'sapEpmUiDialog')]"));
		((Input)newConnectionDlg.getUI5ObjectByIdEndsWith("-connection-name-field")).setValue(connectionName, false);
		newConnectionDlg.ok();
		//New BW connection dlg
		Dialog BWConnectionDlg = (Dialog)SapUi5Factory.findUI5ObjectByLocator(bw, By.xpath("//label[@class='sapEpmUiFormTitle' and text()='"+Resource.getMessage("DI_FORM_NEW_BW_CONNECTION")+"']/ancestor::div[contains(@class,'sapEpmUiDialog')]"));
		//((Input)BWConnectionDlg.getUI5ObjectByIdEndsWith("-r3name-input")).setValue(R3Name, false);
		((Input)BWConnectionDlg.getUI5ObjectByIdEndsWith("-application-server-input")).setValue(applicationName, false);
		((Input)BWConnectionDlg.getUI5ObjectByIdEndsWith("-system-number-input")).setValue(sysNum, false);
		((Input)BWConnectionDlg.getUI5ObjectByIdEndsWith("-client-input")).setValue(clientID, false);
		((Input)BWConnectionDlg.getUI5ObjectByIdEndsWith("-language-input")).setValue(lanaguage, false);
		((Input)BWConnectionDlg.getUI5ObjectByIdEndsWith("-user-name-input")).setValue(userName, false);
		((Input)BWConnectionDlg.getUI5ObjectByIdEndsWith("-password-field")).setValue(pwd, false);
		BWConnectionDlg.ok();

		
		ComboBox queryNameCOM = (ComboBox)selectDataModelDlg.getUI5ObjectByIdEndsWith("-select-query-name");
		queryNameCOM.setValue(Resource.getMessage("DI_CREATE_NEW_QUERY"));
		//New Query dlg
		Dialog queryDlg = (Dialog)SapUi5Factory.findUI5ObjectByLocator(bw, By.xpath("//label[@class='sapEpmUiFormTitle' and text()='New Query']/ancestor::div[contains(@class,'sapEpmUiDialog')]"));
		((Input)queryDlg.getUI5ObjectByIdEndsWith("-query-name-field")).setValue(queryName, false);
		queryDlg.ok();
		//select BW query
		Dialog selectedQueryDlg = SapUi5Factory.findUI5ObjectByClass(bw, "bwQuerySelectionDialog");
		SearchField sf = selectedQueryDlg.getUI5ObjectByIdEndsWith( "-query-search-field");
		sf.clearValue();
		sf.setValue(selectedQuery);

		SapUi5Utilities.waitWhenPageBusy(bw.driver());
		selectedQueryDlg.clickWebElement(By.xpath("//span[text()='"+selectedQuery+"']"));
		selectedQueryDlg.ok();
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
		Dialog selectionDlg = (Dialog)SapUi5Factory.findUI5ObjectByLocator(bw, By.xpath("//label[@class='sapEpmUiFormTitle' and text()='"+Resource.getMessage("DI_BW_SELECTION")+"']/ancestor::div[contains(@class,'sapEpmUiDialog')]"));
		
		selectionDlg.ok();
		//Go back to select data model
		SapUi5Utilities.waitWhenPageBusy(bw.driver());
		selectDataModelDlg.ok();
		SapUi5Factory.findWebElementByClass(bw, "sapEPMUIModellerGridContent");
	}
	
}
