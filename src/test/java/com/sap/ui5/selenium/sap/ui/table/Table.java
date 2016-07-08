package com.sap.ui5.selenium.sap.ui.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.commons.CheckBox;
import com.sap.ui5.selenium.commons.TriStateCheckBox;
import com.sap.ui5.selenium.commons.TriStateCheckBoxState;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.core.UI5By;
import com.sap.ui5.selenium.core.UI5Object;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;
import com.sap.ui5.selenium.sap.ui.core.ScrollBar;
import com.sap.ui5.selenium.utilities.SapUi5Utilities;


public class Table extends CompoundControl{

	protected Table(String id, Frame frame) {
		super(id, frame);
	}
	
	
	

	
	/**
	 * return the column index by the column name
	 * @param colName
	 * @return the column index of a column named colName or -1 
	 */
	protected int getColumnIndex(String colName) {
		try{
			List<WebElement> cols = this.findRootWebElement().findElements(By.className("sapUiTableCol"));
			for(WebElement col:cols){
				if(col.getText().trim().equalsIgnoreCase(colName.trim()) || col.getAttribute("innerText").trim().equalsIgnoreCase(colName.trim())){
					int colIndex = Integer.parseInt(col.getAttribute("data-sap-ui-colindex"));
					return this.hasValidRowHdr()?colIndex+1:colIndex;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Get the exact column cell by index start from 0
	 * @param index
	 * @return
	 */
	private WebElement getColumnHeadByIndex(int index) {
		try{
			List<WebElement> cols = this.findRootWebElement().findElements(By.className("sapUiTableCol"));
			for(WebElement col:cols){
				if(Integer.parseInt(col.getAttribute("data-sap-ui-colindex")) == index){
					return col;
				}
			}
		}catch(Exception e){	
			System.out.println(e.getCause());
		}
		return null;
	}
	
	/**
	 * 
	 * @param index indicate which one is supposed, start from 1
	 * @return the column index which contains the checkbox
	 */
	private int getColumnContainsCheckbox(int index){
		try{
			List<WebElement> cols = this.findRootWebElement().findElements(By.className("sapUiTableCol"));
			int count = 0;
			for(WebElement col:cols){
				if(SapUi5Utilities.getCheckBoxfrom(driver,col)!=null){
					count++;
					if(count==index){
						return Integer.parseInt(col.getAttribute("data-sap-ui-colindex"));
					}
				}
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 
	 * @return boolean to check if the table has the valid row header
	 */
	protected boolean hasValidRowHdr(){
		boolean hasrowhdr = false;
		driver.manage().timeouts().implicitlyWait((long) 0.5, TimeUnit.SECONDS);
		try{
			String ariaselected = this.getRowHeaders().get(0).getAttribute("aria-selected");
			if(!ariaselected.equals("")){
				hasrowhdr = true;
			}
		}catch(Exception e){
			hasrowhdr = false;
		}
		driver.manage().timeouts().implicitlyWait((long) 10, TimeUnit.SECONDS);
		return hasrowhdr;
	}

	/**
	 * 
	 * Collapse all the tree node one by one
	 */
	public void collapseAllNodes(){
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		while(this.findRootWebElement().findElements(By.className("sapUiTableTreeIconNodeOpen")).size()>0){
			final WebElement opennode = driver.findElement(By.className("sapUiTableTreeIconNodeOpen"));
			opennode.click();
			new WebDriverWait(driver,3).until(new ExpectedCondition<Boolean>(){
				public Boolean apply(WebDriver webdriver) {
					return opennode.getAttribute("class").contains("sapUiTableTreeIconNodeClosed");
				}
			});
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	/**
	 * 
	 * Expand all the tree node one by one
	 */
	public void expandAllNodes(){
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		while(this.findRootWebElement().findElements(By.className("sapUiTableTreeIconNodeClosed")).size()>0){
			final WebElement opennode = driver.findElement(By.className("sapUiTableTreeIconNodeClosed"));
			opennode.click();
			new WebDriverWait(driver,3).until(new ExpectedCondition<Boolean>(){
				public Boolean apply(WebDriver webdriver) {
					return opennode.getAttribute("class").contains("sapUiTableTreeIconNodeOpen");
				}
			});
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	/**
	 * 
	 * Wait until the table initialization is finished.
	 */
	public void waitUntilTableInitialized(){
		Wait<WebDriver> wait = new WebDriverWait(driver, 30);
		wait.until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					webdriver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
					List<WebElement> trlist = getTableRows();
					if(trlist.size()==0)return true;
					List<WebElement> celllist = trlist.get(trlist.size()-1).findElements(By.className("sapUiTableCell"));
					webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					return celllist.size()>0;
				}catch(Exception e){
					return false;
				}
			}
		});
	}
	
	/**
	 * 
	 * @param cb
	 */
	protected void tickCheckbox(WebElement cb){
		if(this.find(UI5By.id(cb.getAttribute("id"))).getClass().equals(com.sap.ui5.selenium.commons.TriStateCheckBox.class)){
			TriStateCheckBox chb = this.find(UI5By.id(cb.getAttribute("id")));
			if(chb.getSelectionState().equals(TriStateCheckBoxState.Unchecked)){
				cb.click();
			}
		}else if(this.find(UI5By.id(cb.getAttribute("id"))).getClass().equals(com.sap.ui5.selenium.commons.CheckBox.class)){
			CheckBox chb = this.find(UI5By.id(cb.getAttribute("id")));
			if(!chb.isChecked()) chb.check();
		}else if(this.find(UI5By.id(cb.getAttribute("id"))).getClass().equals(com.sap.ui5.selenium.sap.m.CheckBox.class)){
			com.sap.ui5.selenium.sap.m.CheckBox chb = this.find(UI5By.id(cb.getAttribute("id")));
			if(!chb.isChecked()){
				chb.check();
			}
		}else{
			System.out.println(this.find(UI5By.id(cb.getAttribute("id"))).getClass().toString());
		}
		
	}

	/**
	 * 
	 * @param cb
	 */
	protected void unTickCheckbox(WebElement cb){
		if(this.find(UI5By.id(cb.getAttribute("id"))).getClass().equals(com.sap.ui5.selenium.commons.TriStateCheckBox.class)){
			TriStateCheckBox chb = this.find(UI5By.id(cb.getAttribute("id")));
			if(chb.getSelectionState().equals(TriStateCheckBoxState.Checked)){
				cb.click();
			}
		}else if(this.find(UI5By.id(cb.getAttribute("id"))).getClass().equals(com.sap.ui5.selenium.commons.CheckBox.class)){
			CheckBox chb = this.find(UI5By.id(cb.getAttribute("id")));
			if(chb.isChecked()) chb.uncheck();
		}else if(this.find(UI5By.id(cb.getAttribute("id"))).getClass().equals(com.sap.ui5.selenium.sap.m.CheckBox.class)){
			com.sap.ui5.selenium.sap.m.CheckBox chb = this.find(UI5By.id(cb.getAttribute("id")));
			if(chb.isChecked()){
				chb.uncheck();
			}
		}else{
			System.out.println(this.find(UI5By.id(cb.getAttribute("id"))).getClass().toString());
		}
	}

	/**
	 * 
	 * @param index should start from 1
	 * @throws InterruptedException 
	 */
	protected void selectSingleRow(int index){
		boolean hashdr = hasValidRowHdr();
		if(hashdr){
			List<WebElement> rowhdrs = this.getRowHeaders();
			rowhdrs.get(index).click();
	
		}else{
			
			int colindex = this.getColumnContainsCheckbox(1);
			colindex = colindex==-1 ? 0 : colindex;
			WebElement td = this.getCellElement(index, colindex);
			WebElement checkbox = SapUi5Utilities.getCheckBoxfrom(driver,td);
			this.tickCheckbox(checkbox);
		}
	}
	/**
	 * 
	 * @param columnname
	 * @param value
	 */
	public void selectRowByColumn(String columnname, String value){
		waitUntilTableInitialized();
		int rowindex = getRowIndex(columnname, value);
		this.selectSingleRow(rowindex);

	}
	
	/**
	 * 
	 * @param index
	 */
	private void tickColumnByIndex(int index){
		WebElement col = this.getColumnHeadByIndex(index);
		WebElement checkbox = SapUi5Utilities.getCheckBoxfrom(driver, col);
		this.tickCheckbox(checkbox);
	}
	/**
	 * 
	 * @param column
	 */
	public void tickColumn(String column){
		waitUntilTableInitialized();
		int colIndex = this.getColumnIndex(column);
		tickColumnByIndex(colIndex);
		
	}
	
	/**
	 * column: the target column you expect to tick the cell
	 * KeyColumn: the column name of the key
	 * key: the key of the item 
	 * user need keyColumn and key to locate the target row in the table and locate the new cell together with column 
	 * @param column
	 */
	public void tickCell(String column, String keyColumn, String key){
		waitUntilTableInitialized();
		int rowindex = this.getRowIndex(keyColumn, key);
		int columnindex = this.getColumnIndex(column);
		WebElement cell = this.getCellElement(rowindex, columnindex);
		this.tickCheckbox(SapUi5Utilities.getCheckBoxfrom(driver, cell));
	}
	/**
	 * 
	 * @param column
	 * @param KeyColumn
	 * @param key
	 */
	public void unTickCell(String column, String KeyColumn, String key){
		waitUntilTableInitialized();
		int rowindex = this.getRowIndex(KeyColumn, key);
		int columnindex = this.getColumnIndex(column);
		WebElement cell = this.getCellElement(rowindex, columnindex);
		this.unTickCheckbox(SapUi5Utilities.getCheckBoxfrom(driver, cell));
	}

	/**
	 * 
	 * @param index
	 */
	private void unTickColumnByIndex(int index){	
		WebElement col = this.getColumnHeadByIndex(index);
		WebElement checkbox = SapUi5Utilities.getCheckBoxfrom(driver, col);
		this.unTickCheckbox(checkbox);
	}
	/**
	 * 
	 * @param column
	 */
	public void unTickColumn(String column){
		waitUntilTableInitialized();
		int colIndex = this.getColumnIndex(column);
		unTickColumnByIndex(colIndex);
	}
	
	public boolean multiSelectByColumn(String columnname, String... value) {
		waitUntilTableInitialized();
		int pageSize = this.getTableRows().size();
		int colindex = this.getColumnIndex(columnname);
		int scrollflag = 1;
		boolean hasRowhdr = hasValidRowHdr();
		ScrollBar scrollBar = this.getScrollBar();
		Map<String, Integer> valueMap = new HashMap<String, Integer>(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 6019787720475484167L;};
		for(String item : value) {
			valueMap.put(item.toLowerCase(), 0);
		}

		while(scrollflag>=0 && valueMap.containsValue(0)) {
			List<WebElement> rows = this.getTableRows();
			List<Integer> indexs = new ArrayList<Integer>();
			for(int i=0;i<rows.size();i++) {
				WebElement td = rows.get(i).findElements(By.tagName("td")).get(colindex);
				String cellvalue = this.getCellValue(td).toLowerCase();
				cellvalue = cellvalue.split("\n")[0];
				if(valueMap.containsKey(cellvalue) && valueMap.get(cellvalue)==0) {
					indexs.add(i);
					valueMap.put(cellvalue, 1);
				}
			}
			this.multiselect(hasRowhdr, indexs);
			scrollflag = scrollBar==null?-1:scrollBar.scrollToNextPage(pageSize);
		}
		return valueMap.containsValue(1);
	}
	/**
	 * 
	 * @param columnname
	 * @param value
	 * @return 
	 * @throws InterruptedException 
	 */
/*	public boolean multiSelectByColumn(String columnname, String... value){	
		waitUntilTableInitialized();
		
		int rowsWithData = this.getRowIndex(columnname, "");
		int colindex = this.getColumnIndex(columnname);
		List<Integer> indexs = new ArrayList<Integer>();
		List<WebElement> rows = this.getTableRows();
		rowsWithData = rowsWithData ==-1?rows.size():rowsWithData;
		boolean hasRowhdr = hasValidRowHdr();
		for(int i=0;i<rowsWithData;i++){
			WebElement td = rows.get(i).findElements(By.tagName("td")).get(colindex);
			String cellvalue = this.getCellValue(td);
			for(int count = 0;count < value.length;count++) {
				if(cellvalue.trim().equalsIgnoreCase(value[count])){
					indexs.add(i);
					break;
				}
			}
			if(indexs.size()==value.length)break;
		}
		if(indexs.size() != 0) {
			this.multiselect(hasRowhdr,indexs);
			return true;
		}
		return false;
	}*/
	/**
	 * 
	 * @param hasRowHeader
	 * @param indexs
	 */
	private void multiselect(boolean hasRowHeader,List<Integer> indexs){
		if(hasRowHeader) {
			List<WebElement> rowhdrs = this.getRowHeaders();
			for (int index : indexs) {
				Actions action = new Actions(driver);
				action.keyDown(Keys.CONTROL).perform();
				rowhdrs.get(index).click();
				action.keyUp(Keys.CONTROL).perform();
				action.release();	
			}
		}else{
			for(int index : indexs) {

				int colindex = getColumnContainsCheckbox(1);
				colindex = colindex==-1?0:colindex;
				WebElement td = this.getCellElement(index, colindex);
				WebElement checkbox = SapUi5Utilities.getCheckBoxfrom(driver, td);
				this.tickCheckbox(checkbox);
			}	
		}
	}
	

    public void selectAll(){
    	driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    	if(hasValidRowHdr()){
    		final WebElement select = this.findRootWebElement().findElements(By.className("sapUiTableColRowHdr")).get(0);
    		new WebDriverWait(driver, 30).until(new ExpectedCondition<Boolean>(){
    			public Boolean apply(WebDriver webdriver) {
    				try{
    					if(select.getAttribute("class").contains("sapUiTableSelAllEnabled sapUiTableSelAll")){
    						select.click();
    						return false;
    					}else{
    						return true;
    					}
    				}catch(Exception e){
    					return false;
    				}
    			}
    		});
    	}else{ 
    		tickColumnByIndex(0); 
    	}
    	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }


    public void deSelectAll(){
    	driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
    	if(hasValidRowHdr()){
    		final WebElement table = this.findRootWebElement();
    		new WebDriverWait(driver, 30).until(new ExpectedCondition<Boolean>(){
    			public Boolean apply(WebDriver webdriver) {
    				try{
    					WebElement select = table.findElements(By.className("sapUiTableColRowHdr")).get(0);
    		    		if(!select.getAttribute("class").contains("sapUiTableSelAllEnabled sapUiTableSelAll")){
    						select.click();
    						return false;
    					}else{
    						return true;
    					}
    				}catch(Exception e){
    					return false;
    				}
    			}
    		});
    		
    	}else{ 
    		unTickColumnByIndex(0); 
    	}
    	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
    
    protected WebElement getCellElement(int row, int col) {
        return getTableRows().get(row).findElements(By.tagName("td")).get(col);
    }
    private List<WebElement> getRowHeaders(){
    	return this.findRootWebElement().findElements(By.className("sapUiTableRowHdr"));
    }
    protected List<WebElement> getTableRows(){
    	return this.findRootWebElement().findElements(By.xpath(".//tbody/tr"));
    }
    
    /**
     * 
     * @param row : row index begin from 0
     * @param col : column index begin from 0
     * @param content :
     */
    private void inputCell(int row, int col, String content) {
        WebElement cell = getCellElement(row, col);
        this.inputCell(cell, content);
    }
    
    private void inputCell(WebElement cell,String content){
    	driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
    	try{
    		WebElement input = cell.findElement(By.className("sapUiTf"));
    		((com.sap.ui5.selenium.commons.TextField)this.getUI5Object(By.id(input.getAttribute("id")))).setValue(content,false);
    	}catch(Exception e){
    		WebElement input = cell.findElement(By.className("sapMInput"));
			((com.sap.ui5.selenium.sap.m.Input)this.getUI5Object(By.id(input.getAttribute("id")))).setValue(content,false);
			
    	}
    	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    /**
     * example: enterInputCell("USER ID", "ABC","LAST NAME" ,"BUSH")
     * use columnname , value to determine the row index
     * @param columnname
     * @param value
     * @param enterColName
     * @param content
     */
    public void inputCell(String columnname, String value,String enterColName ,String content) {
    	waitUntilTableInitialized();
    	int rowIndex = this.getRowIndex(columnname, value);
        int colIndex = getColumnIndex(enterColName);
        inputCell(rowIndex, colIndex, content);
    }

    /**
     * 
     * @param cell : webelement
     * @return
     */
    protected String getCellValue(WebElement cell) {
        WebElement input = null;
        String cellValue = null;
        try {
            driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
            input = cell.findElement(By.tagName("input"));
        } catch (Exception e) {
        }
        
        if(input != null) {
        	cellValue = input.getAttribute("value");
        }
        else {
        	try {
        		input = cell.findElement(By.tagName("button"));
        	} catch(Exception e) {
        		
        	}
        	cellValue = input == null?cell.getText():input.getAttribute("title");
        }
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return cellValue;
    }

    /**
     * get rowindex by columnname and the value in the column
     * @param columnname
     * @param value
     * @return the rowindex
     */
	protected int getRowIndex(String columnname, String value) {
		int colindex = this.getColumnIndex(columnname);
		int scrollflag = 1;
		ScrollBar scrollBar = this.getScrollBar();
		int pageSize = this.getTableRows().size();
		while(scrollflag>=0) {
			List<WebElement> rows = this.getTableRows();
			WebElement td = null;
			for (int i = 0; i < rows.size(); i++) {
				td = rows.get(i).findElements(By.tagName("td")).get(colindex);
				String cellvalue = this.getCellValue(td).split("\n")[0];
				if (cellvalue.trim().equalsIgnoreCase(value)) {
					return i;
				}
			}
			scrollflag = scrollBar==null?-1:scrollBar.scrollToNextPage(pageSize);
		}

		return -1;
	}
	
    
    /**
     * input a row of data by rowindex
     * @param rowIdex: row index ,begin from 1...
     * @param args: data string
     */
	private void inputDataLine(int rowIdex, String... args) {
		List<WebElement> tdlist = this.getTableRows().get(rowIdex).findElements(By.tagName("td"));
		int firstvalidcol = getFirstEditableColumnIndex();
		for(int i=0; i<args.length; i++){
			this.inputCell(tdlist.get(firstvalidcol), args[i]);
			firstvalidcol++;
		}
	}
	
	private int getFirstEditableColumnIndex(){
		List<WebElement> cols = this.findRootWebElement().findElements(By.className("sapUiTableCol"));
		int validColumn = -2;
		for(int i=0;i<cols.size();i++){
			if(!cols.get(i).getText().trim().equalsIgnoreCase("")){
				validColumn = i;
				break;
			}
		}
		return this.hasValidRowHdr()==true?validColumn+1:validColumn;
		
	}
	/**
	 * input a row of data by the name of column and the value in column
	 * example:inputDataLine("Matt_HOPPER", "Viewer", "DISPLAYNAME" , "FIRST NAME" , "EMAIL", "MANAGER", "ROLE")
	 * input data at the row that the "ROLES" contains the value of "Viewer"
	 * @param columnname
	 * @param value
	 * @param args
	 */
	public void inputDataLine(String columnname, String value, String... args) {
		waitUntilTableInitialized();
		int rowindex = getRowIndex(columnname, value);
		this.inputDataLine(rowindex, args);
	}
	
	protected ScrollBar getScrollBar() {
		ScrollBar scrollBar;
		try {
			scrollBar = ScrollBar.findScrollBarByIdEndsWith(frame.getWindow(), this.getId()+"-vsb", 1);
			scrollBar.setScrollPosition(0);
		} catch (TimeoutException e) {
			scrollBar = null;
		}
		return scrollBar;
	}
	/**
	 * check whether the item exist in the column by columnindex
	 * @param col: columnindex, begin from 0
	 * @param item
	 * @return whether the item exist
	 */
	private boolean checkItemExist(int col, String item) {
		int scrollflag = 1;
		int pageSize = this.getTableRows().size();
		ScrollBar scrollBar = this.getScrollBar();
		while (scrollflag >= 0) {
			List<WebElement> rows = this.getTableRows();
			for (int i = 0; i < rows.size(); i++) {
				WebElement td = rows.get(i).findElements(By.tagName("td"))
						.get(col);
				if (item.trim().equalsIgnoreCase(this.getCellValue(td).trim())) {
					return true;
				}
			}
			scrollflag = scrollBar==null?-1:scrollBar.scrollToNextPage(pageSize);
		}
		return false;
	}

	/**
	 * check whether the item exist in the column by columnname
	 * @param colname
	 * @param item
	 * @return whether the item exist
	 */
	public boolean checkItemExist(String colname, String item) {
		SapUi5Utilities.waitWhenPageBusy(driver);
		boolean isExist = false;
		int colindex = this.getColumnIndex(colname);
		isExist = this.checkItemExist(colindex, item);
		return isExist;
	}
	
	public boolean checkItemsExist_MultiMatch(String colname[], String item[]){
		List<WebElement> rows = this.getTableRows();
		int len = colname.length;
		int colindex[] = new int[len];
		int count = 0;
		for (int i = 0; i < len; i++){
			colindex[i] = this.getColumnIndex(colname[i]);
		}
		
		for (int i = 0; i < rows.size(); i++){
			count = 0;
			for (int j = 0; j < len; j++){
				WebElement td = rows.get(i).findElements(By.tagName("td")).get(colindex[j]);
				if (item[j].trim().equalsIgnoreCase(this.getCellValue(td).trim())){
					count++;
				}
				else break;
			}
			if (count == len) return true;
		}
		return false;
	}
	
	public void assertMultiItemsExist(String colname[], String item[], Boolean expected){
		Boolean actual = checkItemsExist_MultiMatch(colname, item);
		Assert.assertEquals("Assert whether Item: " + Arrays.toString(item) + " exist in " + "column " + Arrays.toString(colname) , expected, actual);
	}

	/**
	 * check whether distinct(items) equals distinct(td) in colname
	 * @param colname
	 * @param items
	 * @return whether the items equals
	 */
	public boolean checkItemsExistsOnly(String colname, String... items) {
		int colindex = 0;
		int i =0;
		String itemValue;
		Map<String, Integer> expectedItems = new HashMap<String, Integer>();
		List<WebElement> rows = this.getTableRows();
		
		colindex = this.getColumnIndex(colname);
		for(i=0; i<items.length; i++) {   //put the given items into a map
			expectedItems.put(items[i].toLowerCase(), 1);
		}
		
		for(i = 0; i < rows.size(); i++) {   //change value of map, if table contains items not given, return false
			WebElement td = rows.get(i).findElements(By.tagName("td")).get(colindex);
			itemValue = this.getCellValue(td).trim().toLowerCase();
			if(expectedItems.containsKey(itemValue)) {
				expectedItems.put(itemValue, expectedItems.get(itemValue)-1);
			}
			else {
				return false;
			}
		}
		return !expectedItems.containsValue(1);
	}
	/**
	 * assert whether item exist by columnname
	 * @param colname
	 * @param item
	 * @param excepted
	 */
	public void assertItemExist(String colname, String item, boolean expected) {
		waitUntilTableInitialized();
		boolean isExist = this.checkItemExist(colname, item);
		Assert.assertEquals("Checking whether item: "+item+" exist in the list", expected, isExist);
	}
	
	
	@SuppressWarnings("unused")
	private int getRowIndexByKeys(String[] columnnames, String[] values) throws Exception{
		//waitUntilTableInitialized(true);
		if(columnnames.length != values.length) {
			throw new Exception("columnnames.length != values.length!");
		}
		List<WebElement> rows = this.getTableRows();
		for(WebElement row:rows){
			int foundflag = 0;
			for(int i=0;i<columnnames.length;i++){
				int colindex = this.getColumnIndex(columnnames[i]);
				WebElement td = row.findElements(By.tagName("td")).get(colindex);
				String cellvalue = this.getCellValue(td);
				if(cellvalue.trim().equalsIgnoreCase(values[i])){
					foundflag++;
					if(foundflag==columnnames.length){
						return rows.indexOf(row)+1;
					}
				}
			}
		}
		return -1;
	}		
	
	/**
	 * 
	 * @param columnname
	 * @param value
	 * @return
	 */
	public void clickCell(String columnname, String value){
		this.waitUntilTableInitialized();
		int rowindex = getRowIndex( columnname,  value);
		int colindex = getColumnIndex(columnname);
		getCellElement(rowindex,colindex).click();

	}
	
	/**
	 * 
	 * @param columnname
	 * @param value
	 * @param whichColumntoTick
	 */
	public void tickCheckBoxInLine(String columnname, String value, String... whichColumntoTick){
		int rowIndex = this.getRowIndex(columnname, value);

		for(String column:whichColumntoTick){
			int columnIdx = getColumnIndex(column)-1;
			String cbStatus = "$(\"input[id*='col"+ columnIdx + "-row"+ rowIndex + "-CB']\").attr('checked')";
			String cbxString = "$(\"input[id*='col"+ columnIdx + "-row"+ rowIndex + "-CB']\").trigger(\"click\")";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			String checked = (String) js.executeScript(cbStatus);
			if(checked==null){
				js.executeScript(cbxString);
			}
		}
	}
	
	public <T extends UI5Object> T getUi5ObjectFromCell(String columnname, String value,String enterColName, String className,int...timeout){
		waitUntilTableInitialized();
    	int rowIndex = this.getRowIndex(columnname, value);
        int colIndex = getColumnIndex(enterColName);
        WebElement cell = this.getCellElement(rowIndex, colIndex);
        
		long start = System.currentTimeMillis();
		int defaultTimeout = timeout!=null && timeout.length==1?timeout[0]:30;
		while (defaultTimeout * 1000 > System.currentTimeMillis() - start) {	
	    	try{
	    		java.util.List<WebElement> elementList = cell.findElements(By.className(className));
	    		for(WebElement el:elementList){
	    			if(el.isDisplayed()){
	    				return this.find(UI5By.id(el.getAttribute("id")));
	    			}
	    		}
	    	}catch(Exception e){
	    		
	    	}
		} 	
		throw new TimeoutException("Could not find element within timeout 30 seconds: " + className.toString());
	}
	
}



