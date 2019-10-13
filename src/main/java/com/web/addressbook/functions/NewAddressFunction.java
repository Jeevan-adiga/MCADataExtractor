package com.web.addressbook.functions;

import com.web.addressbook.pages.NewAddressPage;
import org.apache.commons.lang3.RandomUtils;

public class NewAddressFunction extends NewAddressPage {

    public NewAddressFunction(String name, String url) {
        super(name, url);
    }

    void addNewAddress(){
        address1.verifySendKeys("sjdhfjadf");
        address2.verifySendKeys("sjdhfjadf");
        city.verifySendKeys("sjdhfjadf");
        state.selectByIndex(5);
    }
}
