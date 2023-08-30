package com.cozyhome.onlineshop.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DataReader {
private final DataMapper mapper;
	
	@Value("${excel.file.path}")
	private String path;

	public String readFromExcel(int rowIndex, int columnIndex) {

		try (InputStream input = DataReader.class.getClassLoader().getResourceAsStream(path);
				Workbook workbook = WorkbookFactory.create(input)) {
			Sheet sheet = workbook.getSheetAt(0);
			Row row = sheet.getRow(rowIndex);
			Cell cell = row.getCell(columnIndex);
			if (cell != null) {
				CellType cellType = cell.getCellType();
				if (cellType == CellType.STRING) {
					return mapper.mapToString(cell.getStringCellValue());
				} else if (cellType == CellType.NUMERIC) {
					return mapper.mapToString(cell.getNumericCellValue());
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return new String();
	}
	
	public int findRowIndexByValue(String targetValue, int columnIndex) {
	    try (InputStream input = DataReader.class.getClassLoader().getResourceAsStream(path);
	            Workbook workbook = WorkbookFactory.create(input)) {
	        Sheet sheet = workbook.getSheetAt(0);
	        for (int rowIndex = sheet.getFirstRowNum(); rowIndex <= sheet.getLastRowNum(); rowIndex++) {
	            Row row = sheet.getRow(rowIndex);
	            if (row != null) {
	                Cell cell = row.getCell(columnIndex);
	                if (cell != null && cell.getCellType() == CellType.STRING) {
	                    String cellValue = cell.getStringCellValue();
	                    if (cellValue.equals(targetValue)) {
	                        return rowIndex;
	                    }
	                }
	            }
	        }
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }

	    return -1; 
	}
}
