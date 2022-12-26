package pages.loggedhomepage;

import pages.Page;

import java.util.ArrayList;

public final class HomepageAuthenticated extends Page {

    public HomepageAuthenticated() {
        initializePossiblePages();
    }

    @Override
    protected void initializePossiblePages() {
        possiblePages = new ArrayList<>();
        possiblePages.add("movies");
        possiblePages.add("upgrades");
        possiblePages.add("logout");
        possiblePages.add("homepage authenticated");
    }
}
