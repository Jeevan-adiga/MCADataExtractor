package test.extract;

import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.mca.beans.ErrorDetails;
import com.mca.beans.ProvideNextDin;
import com.mca.beans.ResultsRecord;
import com.mca.pages.ViewDirectorMasterDataResultPage;
import com.mca.pages.ViewDirectorMasterDataResultPage.ResultsTable.ResultsTableColumn.ResultsTableRow;
import com.mca.pages.ViewDirectorMasterDataSearchPage;
import com.mca.utils.CsvWriterUtils;

import auto.framework.TestBase;
import auto.framework.web.WebControl;

@Test(threadPoolSize = 2, invocationCount = 3)
public class ExtractDataFromMCA extends TestBase {
	
	int waitTime = 30;
//	String dIn = "00870832";
	String errorPath = "./target/errorLog.log";
	String resultsPath = "./target/result.log";
	
	
//	@Test(dataProvider = "provideDin")
	public void extractDataFromMCA() throws InterruptedException {
		
		String dIn = ProvideNextDin.getInstance();
		System.out.println(dIn);		
		
		WebControl.open("http://www.mca.gov.in/mcafoportal/showdirectorMasterData.do");
		ErrorDetails error = new ErrorDetails();
		
		Thread.sleep(5000);
		// click on close button if popup msg is displayed
		if(ViewDirectorMasterDataSearchPage.errorMsg.waitForDisplay(true, 10)) {
			ViewDirectorMasterDataSearchPage.errorCloseBtn.click();
		}
		
		ViewDirectorMasterDataSearchPage.dinInput.waitForDisplay(true, waitTime);
		ViewDirectorMasterDataSearchPage.dinInput.sendKeys(dIn);
		ViewDirectorMasterDataSearchPage.submitButton.click();

		if(ViewDirectorMasterDataSearchPage.errorMsg.waitForDisplay(true, 10)) {
			error.setDin(dIn);
			error.setErrorMsg(ViewDirectorMasterDataSearchPage.errorMsg.getText());
			CsvWriterUtils.writeErrorDataLineByLine(errorPath, error);
			System.out.println(error.toString());
		} else if(!ViewDirectorMasterDataResultPage.companyTable.getAttribute("class").contains("dataTable")
				&& !ViewDirectorMasterDataResultPage.llpTable.getAttribute("class").contains("dataTable")) { 
			error.setDin(dIn);
			error.setErrorMsg("Data not present in both companyTable and llpTable");
			CsvWriterUtils.writeErrorDataLineByLine(errorPath, error);
			System.out.println(error.toString());
		} else { 
			String din = ViewDirectorMasterDataResultPage.dinTextBox.getText();
			String name = ViewDirectorMasterDataResultPage.nameTextBox.getText();
			
			if(ViewDirectorMasterDataResultPage.companyTable.getAttribute("class").contains("dataTable")) {
				List<ResultsTableRow> rows = ViewDirectorMasterDataResultPage.companyTable.companyColumn().getAllRows();
				for(ResultsTableRow row : rows) {			
					ResultsRecord record = new ResultsRecord();
					record.setDin(din);
					record.setName(name);
					record.setCategory("Company");
					record.setCin(row.cin.getText());
					record.setCompanyName(row.companyName.getText());
					record.setBeginDate(row.beginDate.getText());
					record.setEndDate(row.endDate.getText());
					record.setActiveCompliance(row.activeCompliance.getText());
					CsvWriterUtils.writeResultsDataLineByLine(resultsPath, record);
					System.out.println(record.toString());
				}				
			}
			
			if(ViewDirectorMasterDataResultPage.llpTable.getAttribute("class").contains("dataTable")) {
				List<ResultsTableRow> rows = ViewDirectorMasterDataResultPage.llpTable.companyColumn().getAllRows();
				for(ResultsTableRow row : rows) {			
					ResultsRecord record = new ResultsRecord();
					record.setDin(din);
					record.setName(name);
					record.setCategory("LLP");
					record.setCin(row.cin.getText());
					record.setCompanyName(row.companyName.getText());
					record.setBeginDate(row.beginDate.getText());
					record.setEndDate(row.endDate.getText());
					record.setActiveCompliance(row.activeCompliance.getText());
					CsvWriterUtils.writeResultsDataLineByLine(resultsPath, record);
					System.out.println(record.toString());
				}				
			}

		} 
	}
	

	@DataProvider(name="provideDin")
    public Object[][] provideDin(){
		String din = "00870";
		int initialValue = 801;
		int count = 20;
		
		Object[][] dataProvider = new Object[count][1];
		for(int i=0; i<count; i++) {
			String dinValue = din+String.valueOf(++initialValue);
			dataProvider[i][0] = dinValue;
		}
//		dataProvider[0][0] = "00870832";
//		dataProvider[1][0] = "00870832";
		System.out.println("dataProvider:"+dataProvider);
		return dataProvider;
    }
}
