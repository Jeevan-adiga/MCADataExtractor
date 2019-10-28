package com.web.addressbook.functions;

import com.web.addressbook.models.AddressModel;
import com.web.addressbook.models.Country;
import com.web.addressbook.pages.AddressListPage;
import com.web.addressbook.pages.AddAddressPage;

public class NewAddressFunction {

    public static void addNewAddress(AddressModel addressModel) {

        AddAddressPage.firstName.verifySendKeys(addressModel.getFirstName());
        AddAddressPage.lastName.verifySendKeys(addressModel.getLastName());

        AddAddressPage.address1.verifySendKeys(addressModel.getAddress1());
        AddAddressPage.address2.verifySendKeys(addressModel.getAddress2());
        AddAddressPage.city.verifySendKeys(addressModel.getCity());
        AddAddressPage.state.selectByValue(addressModel.getState().getCode());
        AddAddressPage.zipCode.sendKeys(addressModel.getZipCode());
        if(addressModel.getCountry().equals(Country.UNITED_STATES)) {
            AddAddressPage.countryUs.click();
        } else {
            AddAddressPage.countryCanada.click();
        }

        AddAddressPage.createAddress.verifyClick();

        AddressListPage.newAddress.waitForDisplay(true, 20);
    }
}
