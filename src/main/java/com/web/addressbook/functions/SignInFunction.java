package com.web.addressbook.functions;

import auto.framework.ReportLog;
import com.web.addressbook.pages.HomePage;
import com.web.addressbook.pages.SignInPage;

public class SignInFunction {

    public static void login(String userName, String password) {
        HomePage.navbar.signInLink.verifyClick();
        SignInPage.signInSection.emailTextBox.verifySendKeys(userName);
        SignInPage.signInSection.passwordtextBox.verifySendKeys(password);
        SignInPage.signInSection.signInButton.verifyClick();
        ReportLog.assertTrue(HomePage.navbar.currentUser.getText().equals(userName),
                "Successfully logged into application");
    }
}
