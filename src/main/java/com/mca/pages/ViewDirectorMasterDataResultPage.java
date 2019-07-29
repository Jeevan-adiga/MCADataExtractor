package com.mca.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;

import auto.framework.web.Element;


public class ViewDirectorMasterDataResultPage {
	
	public static Element dinTextBox = new  Element("dinTextBox", By.xpath("//td[contains(text(),'DIN')]/following-sibling::td"));
	public static Element nameTextBox = new  Element("nameTextBox", By.xpath("//td[contains(text(),'Name')]/following-sibling::td"));
	
	public static ResultsTable companyTable = new ResultsTable("CompanyTable",
			By.xpath("//table[@id='companyData']"));
	public static ResultsTable llpTable = new ResultsTable("LlpTable",
			By.xpath("//table[@id='llpData']"));
	
	public static class ResultsTable extends Element {

		public ResultsTable(final String name, final By by) {
			super(name, by);
		}

		public ResultsTableColumn companyColumn() {
			return new ResultsTableColumn("Table Column", By.xpath(".//tbody"), this);
		}

		public static class ResultsTableColumn extends Element {

			public ResultsTableColumn(final String name, final By by, final Element parent) {
				super(name, by, parent);
			}

			public static class ResultsTableRow extends Element {
				public Element cin;
				public Element companyName;
				public Element beginDate;
				public Element endDate;
				public Element activeCompliance;

				public ResultsTableRow(final String name, final By by, final Element parent) {
					super(name, by, parent);
					cin = new Element("cin", By.xpath(".//td[1]"), this);
					companyName = new Element("companyName", By.xpath(".//td[2]"), this);
					beginDate = new Element("beginDate", By.xpath(".//td[3]"), this);
					endDate = new Element("endDate", By.xpath(".//td[4]"), this);
					activeCompliance = new Element("activeCompliance", By.xpath(".//td[5]"), this);
				}
			}
			
			public final List<ResultsTableRow> getAllRows() {
				int i = 1;
				final List<ResultsTableRow> allTimes = new ArrayList<ResultsTableRow>();
				ResultsTableRow row = new ResultsTableRow("Row:"+i, By.xpath(".//tr["+i+"]"), this);

				do {
					allTimes.add(row);
					i++;
					row = new ResultsTableRow("Row:"+i, By.xpath(".//tr["+i+"]"), this);
				} while (row.isDisplayed());

				return allTimes;
			}
		}
	}


}
