package com.web.addressbook.functions;

import auto.framework.web.Element;
import auto.framework.web.Link;
import com.web.addressbook.models.AddressModel;
import com.web.addressbook.models.Country;
import com.web.addressbook.pages.BasePage;
import com.web.addressbook.pages.ViewAddressPage;
import org.openqa.selenium.By;

/**
 * Page-Object representation of Welcome page
 */
public class ViewAddressFunction {

    public static void verifyAddressAdded(AddressModel addressModel) {
        ViewAddressPage.firstName.verifyText(addressModel.getFirstName());
        ViewAddressPage.lastName.verifyText(addressModel.getLastName());
        ViewAddressPage.address1.verifyText(addressModel.getAddress1());
        ViewAddressPage.address2.verifyText(addressModel.getAddress2());
        ViewAddressPage.city.verifyText(addressModel.getCity());
        ViewAddressPage.state.verifyText(addressModel.getState().getCode());
        ViewAddressPage.zipCode.verifyText(addressModel.getZipCode());
        if(addressModel.getCountry().equals(Country.UNITED_STATES)){
            ViewAddressPage.country.verifyText("us");
        } else {
            ViewAddressPage.country.verifyText("canada");
        }
    }
}
