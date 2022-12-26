package pages.miscellaneous;

import pages.Page;

import java.util.ArrayList;

public final class Details extends Page {

    public Details() {
        initializePossiblePages();
    }

    @Override
    protected void initializePossiblePages() {
        possiblePages = new ArrayList<>();
        possiblePages.add("homepage autentificat");
        possiblePages.add("movies");
        possiblePages.add("logout");
        possiblePages.add("upgrades");
        possiblePages.add("details");
    }
}
