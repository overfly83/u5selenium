package com.sap.ui5.selenium.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.SimpleLayout;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.core.BrowserWindowManager;
import com.sap.ui5.selenium.sap.fpa.ui.control.navigation.Navigation;
import com.sap.ui5.selenium.sap.fpa.ui.control.shell.Shell;
import com.sap.ui5.selenium.sap.fpa.ui.control.shell.ShellInfoMessageArea;
import com.sap.ui5.selenium.sap.m.Dialog;

public class ScenarioTest{

	
	private static String HANA_HOST = "hanaHost";
	private static String HANA_WEB_PORT = "hanaHTTPPort";
	private static String IS_CLOUD = "isCloud";
	private static String IS_SAML = "isSAML";
	private static String REPOSITORY_ROOT_PATH = "repoRootPath";
	private static String HANA_SYSTEM_USER = "hanaUser";
	private static String HANA_SYSTEM_USER_PASSWORD = "hanaPassword";
	private static String BROWSER = "browser";
	private static String hanaHost;
	private static String hanaHTTPPort;
	private static String hanaSystemUser;
	private static String hanaSystemUserPassword;
	private static boolean isCloud;
	private static boolean isSAML;
	public static String language;
	private static String repositoryRootPath;
	private static String browserType;
	private static String baseUrl;
	private static FirefoxProfile firefoxProfile;
	private static String driverPath;
	private static String fireBugPath;
	private Stack<WebDriver> drivers = new Stack<WebDriver>();
	private WebDriver driver;
	private Stack<String> users = new Stack<String>();
	private String user;
	private enum DRIVER{IE,CHROME,FIREFOX;}
	
	protected BrowserWindow browserWindow;
	
	public Logger logger;
	
	/**
	 * constructor
	 */
	public ScenarioTest(){
		logger = LoggerFactory.getLogger(this.getClass());
		configureLogging(this.getClass().getName()+".log",org.apache.log4j.Level.OFF);		
	}
	
	
	@Rule
	public TestWatcher watchman = new TestWatcher(){
	
		@Override
		public void failed(Throwable e, Description description) {
			try{
				updateBanner(e.getMessage());
			}catch(Exception ex){}
	    	takeScreenshot(driver, user+"-"+description.getMethodName() + ".png");
	    	analyzeLog(user+"-"+description.getMethodName() + ".log");
	    	closeCurrentDriver();
	    }
		@Override
		public void finished(Description description) {
	    	quitAllDrivers();
	    }
	    @Override
	    public void starting(Description description)  {
	    }
	      
	};

	public void analyzeLog(String filename) {
		StringBuffer sf = new StringBuffer();
        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        for (LogEntry entry : logEntries) {
        	sf.append(entry.getMessage()+"\n");
        }
        String content = sf.toString();
		try {
			FileUtils.writeStringToFile(new File("target" + File.separator + "consolelog" + File.separator + filename), content);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * Set FPA URL for cloud or local HANA instance, called before class.
	 */
	@BeforeClass
	public static void setUrl(){

		hanaHost = System.getProperty(HANA_HOST);
		Assert.assertNotNull("Must set: -D" + HANA_HOST + "=<e.g. pvgl50832455a.apj.global.corp.sap>", hanaHost);

		hanaHTTPPort = System.getProperty(HANA_WEB_PORT);
		Assert.assertNotNull("Must set: -D" + HANA_WEB_PORT + "=<e.g. 8002>", hanaHTTPPort);

		isCloud = new Boolean(System.getProperty(IS_CLOUD)).booleanValue();
		isSAML = new Boolean(System.getProperty(IS_SAML)).booleanValue();

		String port = "";
		if(isCloud){
			port="";
		}else if(isSAML){
			port = hanaHTTPPort.replaceFirst("80", ":43");
		}else{
			port = ":"+hanaHTTPPort;
		}
		baseUrl = ((isCloud||isSAML)?"https://":"http://")+hanaHost+port+"/sap/fpa/ui/";
		
		language = System.getProperty("language");
		language = language!=null?language:"en";
	}
	
	/**
	 * Set driver path, called before class.
	 */
	@BeforeClass
	public static void setDriverPath(){
		if(driverPath!=null && driverPath.length()>0){
			return;
		}
		browserType = System.getProperty(BROWSER);
		Assert.assertNotNull("Must set: -D" + BROWSER + "=<e.g. ie, chrome, firefox>", browserType);
		
		repositoryRootPath = System.getProperty(REPOSITORY_ROOT_PATH);
		Assert.assertNotNull("Must set: -D" + REPOSITORY_ROOT_PATH + "=<absolute path to fpatest>", repositoryRootPath);
		if (!repositoryRootPath.endsWith("/")) {
			repositoryRootPath = repositoryRootPath + File.separator+"src"+File.separator
					+"test"+File.separator+"resources"+File.separator;
		}
		
		String osName = System.getProperty("os.name");
		/**
		 * FOR CHROME BROWSER
		 */
		if(browserType.trim().equalsIgnoreCase(DRIVER.CHROME.name())){
			driverPath = repositoryRootPath +"driver"+ File.separator + "chromedriver" + File.separator + "2_15" + File.separator;
			
			if (osName.startsWith("Windows")) {
				driverPath += "win32" + File.separator + "chromedriver.exe";
			} else if (osName.startsWith("Linux")) {
				driverPath += "linux64" + File.separator + "chromedriver";
			} else if (osName.startsWith("Mac OS")) {
				driverPath += "mac32" + File.separator + "chromedriver";
			} else {
				String unsupportedOperatingSystem = "Unsupported operating system."+osName;
				Assert.fail(unsupportedOperatingSystem);
			}
			System.setProperty("webdriver.chrome.driver", driverPath);
		/**
		 * FOR FIREFOX BROWSER
		 */
		}else if(browserType.trim().equalsIgnoreCase(DRIVER.FIREFOX.name())){
			//firefox 36.01 vs firefox driver 2.45
			driverPath = repositoryRootPath +"driver"+ File.separator + "firefoxdriver" + File.separator + "2_45" + File.separator + "webdriver.xpi";
			fireBugPath = repositoryRootPath +"driver"+ File.separator + "firefoxdriver" + File.separator + "2_45" + File.separator + "firebug-2.0.7-fx.xpi";
			
		/**
		 * FOR IE BROWSER
		 */
		}else if(browserType.trim().equalsIgnoreCase(DRIVER.IE.name())){
			driverPath = repositoryRootPath +"driver"+ File.separator + "iedriver" + File.separator + "2_44" + File.separator;
			
			if (osName.startsWith("Windows")) {
				driverPath += "win64" + File.separator + "IEDriverServer.exe";
				System.setProperty("webdriver.ie.driver", new File(driverPath).getAbsolutePath());
			}else{
				String unsupportedOperatingSystem = "Unsupported operating system."+ osName;
				Assert.fail(unsupportedOperatingSystem);
			}
		}
		
	}
	

	/**
	 * Called after the method to quit all drivers.
	 */
	private void quitAllDrivers(){
		for(WebDriver driver:drivers){
			driver.quit();
		}
		this.users.clear();
	}
	
	/**
	 * Close current active driver.
	 */
	private void closeCurrentDriver(){
		if(driver!=null){
			driver.quit();
			drivers.pop();
			user = null;
			users.pop();
			try{
				this.driver = drivers.lastElement();
				this.user = users.lastElement();
				this.browserWindow = new BrowserWindowManager(driver).currentWindow();
			}catch(NoSuchElementException e){}
		}
	}
	
	/**
	 * 
	 * @return the current browserwindow
	 */
	private WebDriver openBrowser() {
		Proxy proxy = new Proxy();
		proxy.setProxyAutoconfigUrl("http://proxy:8083/");
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability("proxy", proxy);
		desiredCapabilities.setCapability("nativeEvents",true);
		desiredCapabilities.setCapability("unexpectedAlertBehaviour",true);
		WebDriver driver = null;
		if(browserType.trim().equalsIgnoreCase(DRIVER.CHROME.name())){
			ChromeOptions option = new ChromeOptions();
			option.addArguments("--test-type");
			System.setProperty("webdriver.chrome.silentOutput", "true");
			desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, option);
			driver = new ChromeDriver(desiredCapabilities);
		}else if(browserType.trim().equalsIgnoreCase(DRIVER.IE.name())){
			desiredCapabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION,true);
			desiredCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
			driver = new InternetExplorerDriver(desiredCapabilities);
		}else if(browserType.trim().equalsIgnoreCase(DRIVER.FIREFOX.name())){
			firefoxProfile = new FirefoxProfile();
			try {
				firefoxProfile.addExtension(new File(driverPath));
				firefoxProfile.addExtension(new File(fireBugPath));
				firefoxProfile.setPreference("nativeEvents", true);
				firefoxProfile.setPreference("unexpectedAlertBehaviour", true);
				firefoxProfile.setPreference("extensions.firebug.currentVersion", "2.0.7"); // Avoid startup screen
				driver = new FirefoxDriver(firefoxProfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		this.driver = driver;
		this.browserWindow = new BrowserWindowManager(this.driver).currentWindow();
		drivers.push(driver);
		return driver;
	}

	/**
	 * Take screen shot when failed or on demand.
	 * @param driver
	 * @param filename
	 */
	private void takeScreenshot(WebDriver driver, String filename) {
		if (driver != null) {
			File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			try {
				FileUtils.copyFile(file,
						new File("target" + File.separator + "screenshots" + File.separator + filename));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void configureLogging(String logfileName, org.apache.log4j.Level level) {
		// Configure logging
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getRootLogger();
		try {
			SimpleLayout layout = new SimpleLayout();
			ConsoleAppender consoleAppender = new ConsoleAppender(layout);
			logger.addAppender(consoleAppender);
			FileAppender fileAppender = new FileAppender(layout, logfileName, true);
			logger.addAppender(fileAppender);
			logger.setLevel(level);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	/**
	 * This login is for admin user login console.html
	 * @param username
	 * @param password
	 */
	public void loginConsole(String username, String password){
		openBrowser().get(baseUrl+"sysAdmin/console.html");
		nativeLogin(username,password);
		closeLimitationDialog();
		SapUi5Factory.findUI5ObjectByIdEndsWith(browserWindow, "-logoutBtn",500);
	}
	
	public void logoutConsole(){
		((com.sap.ui5.selenium.commons.Button)SapUi5Factory.findUI5ObjectByIdEndsWith(browserWindow, "-logoutBtn")).click();
		this.closeCurrentDriver();
	}
	
	/**
	 * @deprecated
	 * @param username
	 * @param password
	 */
	public void registerFreemiumUser(String username, String password){
		openBrowser().get(baseUrl+"public/index.html");
		((com.sap.ui5.selenium.sap.m.Button)SapUi5Factory.findUI5ObjectById(browserWindow, "submit_btn")).click();
		Dialog registerDlg = SapUi5Factory.findUI5ObjectByClass(browserWindow, "sapMDialog");
		((com.sap.ui5.selenium.sap.fpa.ui.control.commons.Input)registerDlg.getUI5ObjectByIdEndsWith("-registration-userId")).setValue(username);
		((com.sap.ui5.selenium.sap.fpa.ui.control.commons.Input)registerDlg.getUI5ObjectByIdEndsWith("-registration-passwd")).setValue(password);
		((com.sap.ui5.selenium.sap.fpa.ui.control.commons.Input)registerDlg.getUI5ObjectByIdEndsWith("-registration-repeatPasswd")).setValue(password);
		((com.sap.ui5.selenium.sap.fpa.ui.control.commons.CheckBox)registerDlg.getUI5ObjectByIdEndsWith("-registration-agreeTerms")).check();
		((com.sap.ui5.selenium.sap.fpa.ui.control.commons.CheckBox)registerDlg.getUI5ObjectByIdEndsWith("-registration-agreePrivacy")).check();
		
		registerDlg.ok();
		nativeLogin(username,password);
		settledownLogin();
	}
	/**
	 * Login the FPA application with normal password directly or reset password if new password is specified.
	 * @param username
	 * @param password
	 * @param newPassword
	 */
	public void login(String username, String password,String tenant) {
		if(tenant!=null){
			openBrowser().get(baseUrl+"tenant/"+tenant);
		}else{
			openBrowser().get(baseUrl);
		}

		nativeLogin(username,password);
		settledownLogin();
		SapUi5Factory.findWebElementByClass(browserWindow, "sapEpmDashboardMenuButton",300);
		
		closeToolTips();
		switchToLanguage(username,password,language);
	}

	public void login(String username, String password, String newPassword,String tenant) {
		if(tenant!=null){
			openBrowser().get(baseUrl+"tenant/"+tenant);
		}else{
			openBrowser().get(baseUrl);
		}

		nativeLogin(username,password);
		resetPassword(username,password,newPassword);
		settledownLogin();
		SapUi5Factory.findWebElementByClass(browserWindow, "sapEpmDashboardMenuButton",300);
		
		closeToolTips();
		switchToLanguage(username,newPassword,language);
		
	}
	
	private void resetPassword(String username, String password, String newPassword){
		try {
			Wait<WebDriver> wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("xs_oldpassword")));
			((com.sap.ui5.selenium.sap.m.Input)SapUi5Factory.findUI5ObjectById(browserWindow, "xs_oldpassword")).setValue(password);
			((com.sap.ui5.selenium.sap.m.Input)SapUi5Factory.findUI5ObjectById(browserWindow, "xs_newpassword")).setValue(newPassword);
			((com.sap.ui5.selenium.sap.m.Input)SapUi5Factory.findUI5ObjectById(browserWindow, "xs_confirmpassword")).setValue(newPassword);
			((com.sap.ui5.selenium.sap.m.Button)SapUi5Factory.findUI5ObjectByClass(browserWindow, "sapMBtn")).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void switchToLanguage(String username, String password,String language){
		try {
			HttpRequest request = new HttpRequest();
			String globalization = "{\"userName\":\""+ username + "\",\"parameters\""+
					":[{\"name\":\"LANGUAGE\",\"desc\":\"Language\",\"type\":\"language\",\"default\":\"E\",\"value\":\""+language+"\"}]}";
			String tenant = getTenantId();
			String userid = getUserId();

			
			String postUrl = "/sap/fpa/services/rest/epm/security/preference/"+username;
			postUrl=tenant!=null?(postUrl+ "?tenant="+tenant):postUrl;
			
			if(!getLanguage().equalsIgnoreCase(language)){
				request.sendPost(postUrl, globalization,userid,password);
				refreshBrowser();
				settledownLogin();
				SapUi5Factory.findWebElementByClass(browserWindow, "sapEpmDashboardMenuButton",300);
			}
			
		} catch (Exception e) {
			System.out.println(username+"||"+password);
			e.printStackTrace();
		}
	}
	

	
	public void refreshBrowser(){
		browserWindow.driver().navigate().refresh();
	}
	/**
	 * Login the FPA application with SMAL Authentication
	 * @param username   "fpatest"
	 * @param password	 "Abcd1234"
	 */
	public void loginAD(String username, String password){
		openBrowser().get(baseUrl);
		this.user = username;
		this.users.push(user);
		SapUi5Factory.findWebElementByIdEndsWith(browserWindow, "_UsernameTextBox").sendKeys(username);
		SapUi5Factory.findWebElementByIdEndsWith(browserWindow, "_PasswordTextBox").sendKeys(password);
		SapUi5Factory.findWebElementByIdEndsWith(browserWindow, "_SubmitButton").click();	
		settledownLogin();
		closeToolTips();
		//Enhance the logic to avoid unnecessary setting
		try {
			HttpRequest request = new HttpRequest();

			String globalization = "{\"userName\":\""+ username + "\",\"parameters\""+
					":[{\"name\":\"LANGUAGE\",\"desc\":\"Language\",\"type\":\"language\",\"default\":\"E\",\"value\":\""+language+"\"}]}";
			request.sendPost("/sap/fpa/services/rest/epm/security/preference/", globalization,null,null);
			browserWindow.driver().navigate().refresh();
			
		} catch (Exception e) {
			System.out.println(username+"||"+password);
			e.printStackTrace();
		}
		settledownLogin();
	}
	
	private void nativeLogin(String username, String password){
		this.user = username;
		this.users.push(user);
		try {
			((com.sap.ui5.selenium.sap.m.Input)SapUi5Factory.findUI5ObjectById(browserWindow, "xs_username")).setValue(username);
			((com.sap.ui5.selenium.sap.m.Input)SapUi5Factory.findUI5ObjectById(browserWindow, "xs_password")).setValue(password);
			((com.sap.ui5.selenium.sap.m.Button)SapUi5Factory.findUI5ObjectById(browserWindow, "logon_button")).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void closeLimitationDialog(){
		if (browserType.trim().equalsIgnoreCase(DRIVER.IE.name())||browserType.trim().equalsIgnoreCase(DRIVER.FIREFOX.name())) {
			((Dialog)SapUi5Factory.findUI5ObjectByClass(browserWindow, "sapMDialog")).ok();
		
		}
	}
	
	private void settledownLogin(){
		closeLimitationDialog();
		getShell();
		addDebugBanner();
	}
	/**
	 * logout the current logged in FPA application
	 */
	public void logout() {
		this.getShell().selectShellOption(Resource.getMessage("MAIN_LOGOUT"));
		//new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.id("logon_button")));
		this.closeCurrentDriver();
	}
	/**
	 * navigate to the expected functional area with specifying the menu item in the navigation
	 * @param menuItems
	 */
	public void navigateTo(String... menuItems){
		getShell().toggleNavigationPane();
		Navigation navigation = SapUi5Factory.findUI5ObjectById(browserWindow, "HomeNavigation");
		navigation.select(menuItems);
	}
	
	
	/**
	 * return current shell
	 * @return
	 */
	public Shell getShell(){
		//return (Shell) (shell==null?SapUi5Factory.findUI5ObjectByClass(browserWindow, "sapEpmUiShell"):shell);
		return (Shell)SapUi5Factory.findUI5ObjectByClass(browserWindow, "sapEpmUiShell",300);
	}
	
	/**
	 * return FPA message area with which you could check or assert FPA message content.
	 * @return
	 */
	public ShellInfoMessageArea getFPAMessageArea(){
		return ShellInfoMessageArea.findShellInfoMessageArea(browserWindow);
	}
	
	/**
	 * Leave page bypass prompt dialog.
	 */
	public void leaveWithoutSave(){
		Dialog leaveDlg = SapUi5Factory.findUI5ObjectByIdEndsWith(browserWindow, "sap-fpa-ui-main-save-prompt");
		leaveDlg.ok();
	}
	
	public String getResourceFolder(){
		
		String resourcesPath = System.getProperty("repoRootPath");
		if (!resourcesPath.endsWith("/")) {
			resourcesPath = resourcesPath + File.separator;
		}
		return resourcesPath +"src"+File.separator
				+"test"+File.separator+"resources"+File.separator;
	}
	public String runQuery(String sql) throws SQLException {
		String sqlUrl = "jdbc:sap://" + hanaHost + ":3" + hanaHTTPPort.substring(2, 4)+"15";
		hanaSystemUser = System.getProperty(HANA_SYSTEM_USER);
		if (hanaSystemUser == null) {
			hanaSystemUser = "SYSTEM";
		}
		hanaSystemUserPassword = System.getProperty(HANA_SYSTEM_USER_PASSWORD);
		if (hanaSystemUserPassword == null) {
			hanaSystemUserPassword = "manager";
		}
		Connection connection = DriverManager.getConnection(sqlUrl, hanaSystemUser, hanaSystemUserPassword);
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		
		String result = rs.getString(1);
		stmt.close();
		connection.close();
		return result;
	}
	
	
	//support insert update delete
	public void runSql(String sql) throws SQLException {
		String sqlUrl = "jdbc:sap://" + hanaHost + ":3" + hanaHTTPPort.substring(2, 4)+"15";
		hanaSystemUser = System.getProperty(HANA_SYSTEM_USER);
		if (hanaSystemUser == null) {
			hanaSystemUser = "SYSTEM";
		}
		hanaSystemUserPassword = System.getProperty(HANA_SYSTEM_USER_PASSWORD);
		if (hanaSystemUserPassword == null) {
			hanaSystemUserPassword = "manager";
		}
		Connection connection = DriverManager.getConnection(sqlUrl, hanaSystemUser, hanaSystemUserPassword);
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(sql);
		
		stmt.close();
		connection.close();
	}
	
	
	private String getLanguage(){
		String userString = "return sap.fpa.ui.infra.common.getContext().getLanguage()";
		return ((JavascriptExecutor) driver).executeScript(userString).toString();
		
	}
	public String getTenantId(){
		if(isMultiTenant()){
			String script = "return sap.fpa.ui.infra.common.getConfiguration()._aItem.CONTENT_PACKAGE";
			String tenant = ((JavascriptExecutor) driver).executeScript(script).toString();
			return tenant.split("\\.")[1];
		}else{
			return null;
		}
	}
	private boolean isMultiTenant(){
		String script = "return sap.fpa.ui.infra.common.getConfiguration()._aItem.MULTI_TENANT";
		return Boolean.parseBoolean(((JavascriptExecutor) driver).executeScript(script).toString());
	}
	
	private boolean isPageTipsOn(){
		String script = "return sap.fpa.ui.App.isPageTipsOn()";
		return Boolean.parseBoolean(((JavascriptExecutor) driver).executeScript(script).toString());
	}
	public void closeToolTips(){
		if(isPageTipsOn()){
			String script = "sap.fpa.ui.App.togglePageTips(false);sap.fpa.ui.control.tooltip.Tooltip.closeAll();";
			((JavascriptExecutor) driver).executeScript(script);
		}
	}
	
	private String getUserId(){
		
		String userString = "return sap.fpa.ui.infra.common.getContext().getUser().getHanaUserId()";
		return ((JavascriptExecutor) driver).executeScript(userString).toString();
	}

	/**
	 * Add debug banner to help analysis the testing result.
	 */
	private void addDebugBanner() {
		String script = "$('body').append('<div id=debugBanner style=\"pointer-events: none;"
				+ "position:absolute;top:5px;left:45%;background:#333300;"
				+ "color: #FF0033;font-weight: bold;padding: 3px;font-size: 15px;"
				+ "font-family: cursive;z-index:999\"padding: 5px\">Selenium test</div>');";
		try {
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			driver.findElement(By.id("debugBanner"));
		} catch (Exception e) {
			((JavascriptExecutor) driver).executeScript(script);
		}
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	}
	
	
	private void updateBanner(String exception){
		BufferedReader reader = new BufferedReader(new StringReader(exception));
		String error = null;
		try {
			error = reader.readLine();
			error = encodedStr(error);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		String bannerScript = "$('#debugBanner').offset({left:$(window).width()/2 - $('#debugBanner').html('Selenium test<br>Error:\""+error+"\"').width()/2});";
		
		((JavascriptExecutor) driver).executeScript(bannerScript);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}
	private String encodedStr(String rawStr){
		String value = rawStr.replaceAll("<", "&lt;");
		value = value.replaceAll(">", "&gt;");
		value = value.replaceAll("\'", "\\\\'");
		return value;
	}
	
	public String parseDecimal(String value,String language){
		try {
			Locale locale = Locale.getDefault();
			NumberFormat fmt = NumberFormat.getNumberInstance();
			float floatValue = fmt.parse(value).floatValue();
			if(language.equalsIgnoreCase("de")){
				locale = Locale.GERMANY;
			}
			fmt = NumberFormat.getNumberInstance(locale);
			((DecimalFormat)fmt).setDecimalSeparatorAlwaysShown(true);
			((DecimalFormat)fmt).setMinimumFractionDigits(2);
			return ((DecimalFormat)fmt).format(floatValue);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return value;
	}
}
