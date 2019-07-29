package com.mca.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.mca.beans.ErrorDetails;
import com.mca.beans.ResultsRecord;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class CsvWriterUtils {

	public static void writeResultsDataLineByLine(String filePath, ResultsRecord result) {
//		// first create file object for file placed at location
//		// specified by filepath
//		File file = new File(filePath);
//		try {
//			// create FileWriter object with file as parameter
//			FileWriter outputfile = new FileWriter(file, true);
//
//			// create CSVWriter object filewriter object as parameter
//			CSVWriter writer = new CSVWriter(outputfile);
//
//			// adding header to csv
//			String[] header = { "DIN", "Name", "Category", "CIN/LLPIN", "Name", "Begin Date", "End Date", "Active Compliance" };
//			writer.writeNext(header);
//			// add data to csv
//			String[] data1 = { result.getDin(), result.getName(), result.getCategory(), result.getCin(), result.getCompanyName(),
//					result.getBeginDate(), result.getEndDate(), result.getActiveCompliance()};
//			writer.writeNext(data1);
//			// closing writer connection
//			writer.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		// List<MyBean> beans comes from somewhere earlier in your code.
		try {
	     Writer writer = new FileWriter("target/result.csv", true);
	     StatefulBeanToCsv<ResultsRecord> beanToCsv = new StatefulBeanToCsvBuilder<ResultsRecord>(writer).build();
	     beanToCsv.write(result);
	     writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CsvDataTypeMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvRequiredFieldEmptyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeErrorDataLineByLine(String filePath, ErrorDetails error) {
		// first create file object for file placed at location
		// specified by filepath
		File file = new File(filePath);
		try {
			// create FileWriter object with file as parameter
			FileWriter outputfile = new FileWriter(file, true);

			// create CSVWriter object filewriter object as parameter
			CSVWriter writer = new CSVWriter(outputfile);

			// adding header to csv
			String[] header = { "DIN", "Error Msg" };
			writer.writeNext(header);
			// add data to csv
			String[] data1 = { error.getDin(), error.getErrorMsg() };
			writer.writeNext(data1);
			// closing writer connection
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
