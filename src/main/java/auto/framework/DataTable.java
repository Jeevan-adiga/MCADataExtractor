package auto.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class DataTable {

	private static ThreadLocal<DataHolder> dataHolder = new InheritableThreadLocal<DataHolder>(){
		@Override
		protected DataHolder initialValue() { return new DataHolder(); }
	};

	public static DataUpdate setRowData(final String sheet, final String where){
		return new DataUpdate(sheet, where);
	}

	/**
	 * method to add a new row to excel sheet.
	 * @param sheet - sheet to which row to be added
	 * @return insert object
	 */
	public static DataInsert addRowData(final String sheet){
		return new DataInsert(sheet);
	}

	public static void setDataTable(final String path) {
		System.setProperty("data", path);

	}

	public static Recordset getRowData(final String sheet, final String... where){
		return dataHolder.get().executeQuery(sheet, where);
	}

	public static Recordset executeQuery(final String query){
		return dataHolder.get().executeQuery(query);
	}

	public static DataTableInstance Load(final String table){
		final String resource = Resources.findResource(table);
		//System.err.println("load: "+resource);
		return new DataTableInstance(resource);
	}

	@Deprecated public static void loadTable(final String table){ //experimental
		System.err.println("load: "+table);
		dataHolder.get().table = table;
	}

	public static class  DataUpdate {
		//ADDED FOR DATA UPDATE
		private final Fillo fillo = new Fillo();
		private final String SHEET;
		private final String WHERE;

		public DataUpdate(final String sheet, final String where){
			SHEET = sheet;
			WHERE = where;
		}

		public DataUpdate update(final String column, final String value) throws IOException, InterruptedException {
			Connection connection = null;
			try {
				String dataTable = "./src/test/resources/data/DataTable.xls";

				final String dataPath = TestManager.Preferences.getPreference("data","DataTable.xls");
				if(dataPath!=null){
					if(new File("./src/test/resources/data/"+dataPath).exists()){
						dataTable = "./src/test/resources/data/"+dataPath;
					} else {
						dataTable = dataPath;
					}
				}

				connection = fillo.getConnection(new File(dataTable).getAbsolutePath());
				final String query = "Update "+SHEET+" Set "+column+"='"+value+"' where "+WHERE; //"Select * from " + sheet;
				connection.executeUpdate(query);
				Thread.sleep(1000);

			} catch (final FilloException e) {
				e.printStackTrace();
				ReportLog.failed("Error Updating Spreadsheet: ");

			} finally {
				if(connection!=null) {
					connection.close();
				}
			}
			return null;
		}
	}

	/**
	 *  class to be used to add an row to the excel sheet
	 * @author jeevan.adiga
	 */
	public static class  DataInsert {
		//ADDED FOR DATA INSERT
		private final Fillo fillo = new Fillo();
		private final String SHEET;

		public DataInsert(final String sheet){
			SHEET = sheet;
		}

		public DataInsert insert(final Map<String,String> columnValues) throws IOException, InterruptedException {
			Connection connection = null;
			try {
				String dataTable = "./src/test/resources/data/DataTable.xls";

				final String dataPath = TestManager.Preferences.getPreference("data","DataTable.xls");
				if(dataPath!=null){
					if(new File("./src/test/resources/data/"+dataPath).exists()){
						dataTable = "./src/test/resources/data/"+dataPath;
					} else {
						dataTable = dataPath;
					}
				}

				connection = fillo.getConnection(new File(dataTable).getAbsolutePath());
				final String query = "Insert into "+SHEET
						+"("+StringUtils.join(columnValues.keySet(), ",")+")"
						+ " values ('"+StringUtils.join(columnValues.values(), "','")+"')"; //Insert into SHEET(col1,col2) values ('value1', 'value2')
				System.out.println(query);
				connection.executeUpdate(query);
				Thread.sleep(1000);

			} catch (final FilloException e) {
				e.printStackTrace();
				ReportLog.failed("Error Inserting Spreadsheet: ");

			} finally {
				if(connection!=null) {
					connection.close();
				}
			}
			return null;
		}
	}

	public static ArrayList<String> getPreference(final String field, final String name, final String tab) throws Exception{
		ArrayList<String> columndata = null;
		boolean found = false;
		try {
			String dataTable = "./src/test/resources/data/DataTable.xls";

			final String dataPath = TestManager.Preferences.getPreference("data","DataTable.xls");
			if(dataPath!=null){
				if(new File("./src/test/resources/data/"+dataPath).exists()){
					dataTable = "./src/test/resources/data/"+dataPath;
				} else {
					dataTable = dataPath;
				}
			}

			final POIFSFileSystem file = new POIFSFileSystem(new FileInputStream(dataTable));
			final FileOutputStream fileo = new FileOutputStream(new File(dataTable));

			//Create Workbook instance holding reference to .xls file
			final HSSFWorkbook workbook = new HSSFWorkbook(file);

			//Get first/desired sheet from the workbook
			final HSSFSheet sheet = workbook.getSheet(tab);

			//Iterate through each rows one by one
			final Iterator<Row> rowIterator = sheet.iterator();
			columndata = new ArrayList<>();
			Row row = rowIterator.next();

			final int colNum = getColNumber(row, field);

			try{
				while (rowIterator.hasNext()){
					row = rowIterator.next();
					final String value = row.getCell(colNum).toString();
					if(name.equalsIgnoreCase(value)) {
						found = true;
					} else if(found == true && value.equalsIgnoreCase("End of Line")) {
						break;
					} else if(found == true) {
						columndata.add(value);
					} else {

					}
				}
			}catch(final NullPointerException e) {
				ReportLog.addInfo(name + " Preference Not Found in Data Table");
			}

			workbook.write(fileo);
			fileo.flush();
			fileo.close();

		} catch (final Exception e) {
			e.printStackTrace();
		}
		return columndata;
	}

	private static int getColNumber(final Row row, final String field) {
		int colNumber = 0;
		final Iterator<Cell> cellIterator = row.cellIterator();
		while (cellIterator.hasNext()) {
			final Cell cell = cellIterator.next();
			if(cell.getStringCellValue().equalsIgnoreCase(field)) {
				colNumber = cell.getColumnIndex();
				break;
			}
		}

		return colNumber;
	}

	public static class DataTableInstance {

		private final DataHolder dataHolder = new DataHolder();

		public DataTableInstance(final String table){
			dataHolder.table = table;
		}

		public Recordset getRowData(final String sheet, final String... where){
			return dataHolder.executeQuery(sheet, where);
		}

	}

}

class DataHolder {

	private final Fillo fillo = new Fillo();

	protected String table=null; //"DataTable.xls";

	protected Recordset executeQuery(final String sheet, final String... where) {
		Connection connection = null;
		Recordset recordset = null;
		try {

			String dataTable = "./src/test/resources/data/DataTable.xls";

			final String dataPath = table!=null? table : TestManager.Preferences.getPreference("data","DataTable.xls");
			if(dataPath!=null){
				if(new File("./src/test/resources/data/"+dataPath).exists()){
					dataTable = "./src/test/resources/data/"+dataPath;
				} else {
					dataTable = dataPath;
				}
			}

			connection = fillo.getConnection(new File(dataTable).getAbsolutePath());
			final String query = "Select * from " + sheet;
			recordset = connection.executeQuery(query);
			for(int i=0; i<where.length; i++){
				recordset = recordset.where(where[i]);
			}
			recordset.next();
			return recordset;
		} catch (final FilloException e) {
			//e.printStackTrace();;
			//		} catch (IOException e) {
			//			//e.printStackTrace();
		} finally {
			if(connection!=null) {
				connection.close();
			}
		}
		return null;
	}

	protected Recordset executeQuery(final String query) {
		Connection connection = null;
		Recordset recordset = null;
		try {

			String dataTable = "./src/test/resources/data/DataTable.xls";

			final String dataPath = table!=null? table : TestManager.Preferences.getPreference("data","DataTable.xls");
			if(dataPath!=null){
				if(new File("./src/test/resources/data/"+dataPath).exists()){
					dataTable = "./src/test/resources/data/"+dataPath;
				} else {
					dataTable = dataPath;
				}
			}

			connection = fillo.getConnection(new File(dataTable).getAbsolutePath());
			recordset = connection.executeQuery(query);
			return recordset;
		} catch (final FilloException e) {
			// e.printStackTrace();
		} finally {
			if(connection!=null) {
				connection.close();
			}
		}
		return null;
	}
}
