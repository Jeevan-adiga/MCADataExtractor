package test.addressbook.test;

import auto.framework.ReportLog;
import auto.framework.TestBase;
import auto.framework.web.WebControl;
import com.Constants;
import com.web.addressbook.functions.AddAddressFunction;
import com.web.addressbook.functions.SignInFunction;
import com.web.addressbook.functions.ViewAddressFunction;
import com.web.addressbook.models.AddressModel;
import com.web.addressbook.pages.AddressListPage;
import com.web.addressbook.pages.HomePage;
import com.web.addressbook.pages.SignInPage;
import com.web.addressbook.pages.ViewAddressPage;
import org.testng.annotations.Test;

public class ID_001_Verify_Add_New_Address extends TestBase {

    @Test
	public void testCase() {
		
		ReportLog.setTestCase("ID_001_Verify_Add_New_Address");
		
		ReportLog.setTestStep("1 - Navigate to application Url");
			WebControl.open(Constants.WEB_BASE_URL);
			HomePage.welcomeText.assertDisplayed(true);

		ReportLog.setTestStep("2 - Login to application");
			SignInFunction.login("adiga.jeevan@gmail.com", "Automate@123");

		ReportLog.setTestStep("3 - Add new Address");
			HomePage.navbar.addressesLink.verifyClick();
			AddressListPage.newAddress.verifyClick();

			AddressModel addressModel = new AddressModel();
			AddAddressFunction.addNewAddress(addressModel);

			ViewAddressPage.list.waitForDisplay(true, 20);
			ViewAddressFunction.verifyAddressAdded(addressModel);
			ViewAddressPage.list.assertClick();
			AddressListPage.addressTable.waitForDisplay(true, 10);
	}
}