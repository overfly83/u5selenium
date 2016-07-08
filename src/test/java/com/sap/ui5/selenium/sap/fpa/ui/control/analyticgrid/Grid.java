package com.sap.ui5.selenium.sap.fpa.ui.control.analyticgrid;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.ui5.selenium.core.BrowserWindow;
import com.sap.ui5.selenium.core.Frame;
import com.sap.ui5.selenium.sap.fpa.ui.common.CompoundControl;
import com.sap.ui5.selenium.utilities.SapUi5Factory;

/**
 * This control wraps the analytical grid in FPA.
 */
public class Grid extends CompoundControl {
	
	public Grid(String id, Frame frame) {
		super(id, frame);
	}
	public Grid getThisGrid(){
		return this;
	}
	public String getCellString(String cellCoords) {
		WebElement element = this.getCellElement(cellCoords);
		return element.getText();
	}
	
	private WebElement getCellElement(String cellCoords) {
		By cellLocator = getCellLocator(cellCoords);
		return this.findRootWebElement().findElements(cellLocator).get(0);
	}

	/**
	 * return the xpath locator of the cell in current grid
	 * @param cellCoords
	 * @return
	 */
	private By getCellLocator(String cellCoords) {
		int col = cellCoords.charAt(0) - 'A';
		int row = Integer.parseInt(cellCoords.substring(1)) - 1;
		return By.xpath(".//table[@class='sapEpmUiControlAnalyticgridGridTableInner']//td[@x='" + col + "' and @y='"
				+ row + "']");
	}
	
	/**
	 * Wait until the report grid is completely open
	 */
	public void waitUntilGridInitialized(){
		Wait<WebDriver> wait = new WebDriverWait(driver, 30);
		wait.until(new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver webdriver) {
				try{
					return getThisGrid().findRootWebElement().findElements(By.className("focused")).size()!=0;
				}catch(Exception e){
					return false;
				}
			}
				
		});
	}



	/**
	 * This function is designed to expect a given value in a grid cell
	 * 
	 * @param cellCoords
	 *            Coordinates as a string of Excel type format, e.g. B5
	 * @param timeout
	 *            Timeout is in seconds
	 * @param expectedValue
	 *            The value that is expected to be found in the cell
	 */

	public void waitUntilHasValue(final String cellCoords, final String expectedValue, long timeout) {
		try {
			new WebDriverWait(driver, timeout).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver webDriver) {
					try{
						WebElement node = getCellElement(cellCoords);
						return node.getText().equals(expectedValue);
					}catch(Exception e){
						return false;
					}
				}
			});
		} catch (Exception e1) {
			throw new TimeoutException("Cell " + cellCoords + " has no value " + expectedValue + " after " + timeout
					+ "s", e1);
		}
	}


	private By getCellByText(String text, String type) {
		By cell = By.xpath("//table[@class='sapEpmUiControlAnalyticgridGridTableInner']//td[contains(text(), '" + text
				+ "') and contains(@class, '" + type + "')]");
		return cell;
	}

	public WebElement getCellElementByText(String text, String type) {
		By cell = getCellByText(text, type);
		WebElement element = this.getThisGrid().findRootWebElement().findElements(cell).get(0);
		return element;
	}



	public void changeCellValue(String cellCoords, final String newValue) {
		final WebElement node = getCellElement(cellCoords);
		new WebDriverWait(driver, 5).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver webDriver) {
				
				try{
					node.click();
					node.sendKeys(Keys.ENTER);
					if(!node.getAttribute("class").contains("editing")){
						return false;
					}
					node.sendKeys(newValue);
					if(!node.getText().equals(newValue)){
						return false;
					}
					return true;
					
				}catch(Exception e){
					return false;
				}
			}
		});
		node.sendKeys(Keys.TAB);
	}


	/**
	 * @param index
	 *            like: A, B, C
	 */
	public void selectColumn(String index){
		int col = index.charAt(0) - 'A';
		WebElement oIndexCol = this.findRootWebElement().findElements(By.xpath("//td[@x='"+col+"' and @y='-2']")).get(0);
		oIndexCol.click();
	}
	
	/**
	 * @param index
	 *            like: 1, 2, 3
	 */
	public void selectRow(String index){
		int row = Integer.parseInt(index) - 1;;
		WebElement oIndexRow = this.findRootWebElement().findElements(By.xpath("//td[@y='"+row+"' and @x='-2']")).get(0);
		oIndexRow.click();
	}


	public void selectRange(String... ranges) {
		boolean first = true;
		Actions builder = new Actions(driver);
		for (String range : ranges) {
			String from,to;
			if(range.split(":").length==1){
				from = range.split(":")[0];
				to = from;
			}else{
				from = range.split(":")[0];
				to = range.split(":")[1];
			}

			WebElement fromCell = this.getCellElement(from);
			WebElement toCell = this.getCellElement(to);
			if (first) {
				builder.moveToElement(fromCell).clickAndHold()
				.moveToElement(toCell).release().build().perform();	
			} else {
				builder.keyDown(Keys.CONTROL).moveToElement(fromCell).clickAndHold()
				.moveToElement(toCell).release().keyUp(Keys.CONTROL).build().perform();
				builder.keyUp(Keys.CONTROL).build().perform();
			}
			first = false;
		}

	}
	

	public void selectCell(String cellCoords) {
		WebElement node = getCellElement(cellCoords);
		node.click();
	}

	
	
	public void collapseAllNodes(){
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		final WebElement grid=this.findRootWebElement();
		while(grid.findElements(By.xpath(".//td[contains(@class,'dimMember expanded')]")).size()>0){
			final WebElement opencell = grid.findElements(By.xpath(".//td[contains(@class,'dimMember expanded')]")).get(0);
			final String locateX = opencell.getAttribute("x");
			final String locateY = opencell.getAttribute("y");
			opencell.findElements(By.xpath(".//span")).get(0).click();
			new WebDriverWait(driver,3).until(new ExpectedCondition<Boolean>(){
				public Boolean apply(WebDriver webdriver) {
					return grid.findElements(By.xpath(".//td[@x='"+ locateX + "' and @y='"+ locateY + "']")).get(0)
							.getAttribute("class").contains("dimMember expansible");
				}
			});
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	public void myDrillRow(final BrowserWindow bw, int x, int y ) {
 		final By byclickMem = this.getCellSpanByXandY(x,y);
		WebElement clickMem =  SapUi5Factory.findWebElementByLocator(bw, byclickMem, 60);
		clickMem.click();
		int attempts = 0;
		boolean successful = false;
		while(attempts < 2) {
			try {
				new WebDriverWait(bw.driver(), 5).until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver webDriver) {
						WebElement element =  SapUi5Factory.findWebElementByLocator(bw, byclickMem, 60); // member exists
						String className = element.getAttribute("class");
						if (className.contains("dimMemberExpanded")) {
							return true;
						} else {
							return false;
						}
					}
				});
				successful = true;
				break;
			} catch (StaleElementReferenceException e1) {
				System.out.println("stale element exception, attempt is " + attempts);
			}
			attempts++;
		}
		Assert.assertTrue(successful);
	}

	public By getCellSpanByXandY(int x, int y) {
		By cell = By.xpath("//table[@class='sapEpmUiControlAnalyticgridGridTableInner']//td[@y='" + y + "' and @x='"
				+ x + "']//span");
		return cell;
	}

	

}
