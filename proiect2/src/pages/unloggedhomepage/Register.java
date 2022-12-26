package pages.unloggedhomepage;

import pages.Page;

import java.util.ArrayList;

public final class Register extends Page {

    public Register() {
        initializePossiblePages();
    }

    @Override
    protected void initializePossiblePages() {
        possiblePages = new ArrayList<>();
        possiblePages.add("register");
    }
}
