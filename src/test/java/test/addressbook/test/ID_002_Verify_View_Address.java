package test.addressbook.test;

import auto.framework.ReportLog;
import auto.framework.TestBase;
import auto.framework.web.WebControl;
import com.Constants;
import com.web.addressbook.functions.AddAddressFunction;
import com.web.addressbook.functions.ViewAddressFunction;
import com.web.addressbook.models.AddressModel;
import com.web.addressbook.pages.AddressListPage;
import com.web.addressbook.pages.HomePage;
import com.web.addressbook.pages.SignInPage;
import com.web.addressbook.pages.ViewAddressPage;
import org.testng.annotations.Test;

public class ID_002_Verify_View_Address extends TestBase {

    @Test
	public void testCase() {
		
		ReportLog.setTestCase("ID_002_Verify_View_Address");
		
		ReportLog.setTestStep("Get List Of movies via API");
			WebControl.open(Constants.WEB_BASE_URL);

		ReportLog.setTestStep("Get 1st movie Cast details for 1st movie from above list");
		HomePage.welcomeText.assertDisplayed(true);

		HomePage.navbar.signInLink.verifyClick();
		SignInPage.signInSection.emailTextBox.verifySendKeys("test@ahem.email");
		SignInPage.signInSection.passwordtextBox.verifySendKeys("test");
		SignInPage.signInSection.signInButton.verifyClick();
		ReportLog.assertTrue(HomePage.navbar.currentUser.getText().equals("test@ahem.email"),
				"Successfully logged into application");

		HomePage.navbar.addressesLink.verifyClick();
		System.out.println("address size:" + AddressListPage.addressTable.getAllRows().size());

		AddressListPage.newAddress.verifyClick();

		AddressModel addressModel = new AddressModel();
		AddAddressFunction.addNewAddress(addressModel);

		ViewAddressPage.list.waitForDisplay(true, 20);
		ViewAddressFunction.verifyAddressAdded(addressModel);
		ViewAddressPage.list.assertClick();
		AddressListPage.addressTable.waitForDisplay(true, 10);
	}

}