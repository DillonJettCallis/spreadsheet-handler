package com.redgear.spreadsheet.impl;

import com.redgear.spreadsheet.ErrorHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class ExcelReaderImpl<T> extends AbstractReader<T>{

	private final Iterator<Row> rows;

	public ExcelReaderImpl(HandlerMetaData<T> data, Workbook workbook, int sheetIndex, ErrorHandler errorHandler) throws IOException, InvalidFormatException {
		super(data, errorHandler);
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		this.rows = sheet.rowIterator();
	}

	@Override
	public List<String> readRawLine(){
		if(rows.hasNext())
			return readRow(rows.next());
		else
			return null;
	}

	private List<String> readRow(Row row){
		List<String> cells = new ArrayList<>();

		for(int i = 0; i < row.getLastCellNum(); i++) {
			Cell c = row.getCell(i);

			if(c == null)
				cells.add(null);
			else
				switch (c.getCellType()) {
					case Cell.CELL_TYPE_BLANK:
						cells.add(null);
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						cells.add(String.valueOf(c.getBooleanCellValue()));
						break;
					case Cell.CELL_TYPE_ERROR:
						cells.add(String.valueOf(c.getErrorCellValue()));
						break;
					case Cell.CELL_TYPE_FORMULA:
						cells.add(c.getCellFormula());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						double value = c.getNumericCellValue();

						if(value % 1 == 0)
							cells.add(String.valueOf((long) value));
						else
							cells.add(String.valueOf(value));
						break;
					case Cell.CELL_TYPE_STRING:
					default:
						cells.add(StringUtils.trimToNull(c.getStringCellValue()));
				}
		}

		return cells;
	}

	@Override
	protected Stream<List<String>> readRawRemaining() {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(rows, 0), false)
					.map(this::readRow);
	}

	@Override
	public void close() throws Exception {

	}
}
