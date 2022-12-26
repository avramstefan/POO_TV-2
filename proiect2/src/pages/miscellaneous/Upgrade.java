package pages.miscellaneous;

import pages.Page;

import java.util.ArrayList;

public final class Upgrade extends Page {

    public Upgrade() {
        initializePossiblePages();
    }

    @Override
    protected void initializePossiblePages() {
        possiblePages = new ArrayList<>();
        possiblePages.add("homepage autentificat");
        possiblePages.add("movies");
        possiblePages.add("logout");
        possiblePages.add("upgrades");
    }
}
