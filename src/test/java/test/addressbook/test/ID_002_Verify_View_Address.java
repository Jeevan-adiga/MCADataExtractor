package test.addressbook.test;

import auto.framework.ReportLog;
import auto.framework.TestBase;
import auto.framework.web.WebControl;
import com.Constants;
import com.web.addressbook.functions.SignInFunction;
import com.web.addressbook.pages.AddressListPage;
import com.web.addressbook.pages.HomePage;
import org.testng.annotations.Test;

import java.util.stream.Collectors;

public class ID_002_Verify_View_Address extends TestBase {

    @Test
    public void testCase() {

        ReportLog.setTestCase("ID_002_Verify_View_Address");

        ReportLog.setTestStep("launch Application");
	        WebControl.open(Constants.WEB_BASE_URL);

        ReportLog.setTestStep("Login to application");
	        SignInFunction.login("test@ahem.email", "test");

        ReportLog.setTestStep("Navigate to address list, fetch count and details of address");
			HomePage.navbar.addressesLink.verifyClick();
			AddressListPage.addressTable.waitForDisplay(true, 10);
			ReportLog.assertTrue(AddressListPage.addressTable.getAllRows().size() == 15,
					"address Record count is matched");
			for (AddressListPage.AddressTable.AddressTableRow addressTableRow : AddressListPage.addressTable.getAllRows()) {
				ReportLog.addInfo("Record:" + addressTableRow.toString());
			}
			ReportLog.assertTrue(AddressListPage.addressTable.getAllRows().stream()
					.filter(addr -> addr.state.getText().equals("AL")).collect(Collectors.toList()).size() == 1,
					"Only one record for state AL is present");

    }
}