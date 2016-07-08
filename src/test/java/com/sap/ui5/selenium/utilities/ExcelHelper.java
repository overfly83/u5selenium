package com.sap.ui5.selenium.utilities;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelHelper {

	private Workbook wb;

	public ExcelHelper(InputStream is) throws InvalidFormatException, IOException {
		wb = WorkbookFactory.create(is);
	}

	public String copyFrom(String sheetName, String range) {
		return copyFrom(wb.getSheet(sheetName), range);
	}
	
	public static String copyFrom(Sheet sheet, String range) {
		final StringBuilder sb = new StringBuilder();
		final CellRangeAddress rangeAddress = CellRangeAddress.valueOf(range);

		for (int row = rangeAddress.getFirstRow(); row <= rangeAddress.getLastRow(); row++) {
			for (int col = rangeAddress.getFirstColumn(); col <= rangeAddress.getLastColumn(); col++) {
				Row row2 = sheet.getRow(row);
				if(row2==null){
					throw new RuntimeException("Invalid range for sheet "+sheet.getSheetName()+": "+range);
				}
				Cell cell = row2.getCell(col);
				if (col != rangeAddress.getFirstColumn()) {
					sb.append('\t');
				}
				if (cell != null) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BLANK:
						sb.append("");
						break;
					case Cell.CELL_TYPE_STRING:
						sb.append(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						sb.append(cell.getNumericCellValue());
						break;
					default:
						sb.append("CELL TYPE NOT SUPPORTED IN TEST");
						break;
					}
				}
				if (col == rangeAddress.getLastColumn()) {
					sb.append('\n');
				}
			}
		}
		return sb.toString();
	}

}
