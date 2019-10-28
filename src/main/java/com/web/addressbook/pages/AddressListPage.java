package com.web.addressbook.pages;

import auto.framework.web.Element;
import auto.framework.web.Link;
import auto.framework.web.Page;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

public class AddressListPage extends Page {

    public AddressListPage(String name, String url) {
        super(name, url);
    }

    public static AddressTable addressTable = new AddressTable("Address-Table", By.xpath("//table"));
    public static Link newAddress = new Link("New Address", By.xpath("//a[@data-test='create']"));

    public static class AddressTable extends Element {

        public AddressTable(final String name, final By by) {
            super(name, by);
        }

        public TableRow row(final int index) {
            return new TableRow("Row:" + index, By.xpath(".//tbody//tr[" + index + "]"), this);
        }

        public List<TableRow> getAllRows() {
            int i = 1;
            final List<TableRow> bulkOrderRows = new ArrayList<>();
            TableRow row = new TableRow("Row:" + i, By.xpath(".//tbody//tr[" + i + "]"), this);

            do {
                if (row.isDisplayed()) {
                    bulkOrderRows.add(row);
                }
                i++;
                row = new TableRow("Row:" + i, By.xpath(".//tbody//tr[" + i + "]"), this);
            } while (row.isDisplayed());

            return bulkOrderRows;
        }

        public TableHeader header() {
            return new TableHeader("Table-Header", By.xpath(".//thead"), this);
        }

        public static class TableRow extends Element {
            public Element firstName;
            public Element lastName;
            public Element city;
            public Element state;
            public Link show;
            public Link edit;
            public Link destroy;

            public TableRow(final String name, final By by, final Element parent) {
                super(name, by, parent);
                firstName = new Element("firstName", By.xpath(".//td[1]"), this);
                lastName = new Element("lastName", By.xpath(".//td[2]"), this);
                city = new Element("city", By.xpath(".//td[3]"), this);
                state = new Element("state", By.xpath(".//td[4]"), this);
                show = new Link("show", By.xpath(".//td[5]/a"), this);
                edit = new Link("edit", By.xpath(".//td[6]/a"), this);
                destroy = new Link("destroy", By.xpath(".//td[7]/a"), this);
            }
        }

        public static class TableHeader extends Element {
            public Element firstName;
            public Element lastName;
            public Element city;
            public Element state;

            public TableHeader(final String name, final By by, final Element parent) {
                super(name, by, parent);
                firstName = new Element("firstName", By.xpath(".//td[1]"), this);
                lastName = new Element("lastName", By.xpath(".//td[2]"), this);
                city = new Element("city", By.xpath(".//td[3]"), this);
                state = new Element("state", By.xpath(".//td[4]"), this);
            }
        }
    }
}
