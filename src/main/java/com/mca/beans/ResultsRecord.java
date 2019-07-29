package com.mca.beans;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class ResultsRecord {

	@CsvBindByPosition(position = 0)
	String din;
	
	@CsvBindByPosition(position = 1)
	String name;
	
	@CsvBindByPosition(position = 2)
	String category;
	
	@CsvBindByPosition(position = 3)
	String cin;
	
	@CsvBindByPosition(position = 4)
	String companyName;
	
	@CsvBindByPosition(position = 5)
	String beginDate;

	@CsvBindByPosition(position = 6)
	String endDate;
	
	@CsvBindByPosition(position = 7)
	String activeCompliance;
	
	public String getDin() {
		return din;
	}

	public void setDin(String din) {
		this.din = din;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCin() {
		return cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getActiveCompliance() {
		return activeCompliance;
	}

	public void setActiveCompliance(String activeCompliance) {
		this.activeCompliance = activeCompliance;
	}

	@Override
	public String toString() {
		return "CompanyDetails [din=" + din + ", name=" + name + ", category=" + category + ", cin=" + cin
				+ ", companyName=" + companyName + ", beginDate=" + beginDate + ", endDate=" + endDate
				+ ", activeCompliance=" + activeCompliance + "]";
	}
	
}
