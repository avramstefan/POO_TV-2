package pages.unloggedhomepage;

import pages.Page;

import java.util.ArrayList;

public final class HomepageUnauthenticated extends Page {

    public HomepageUnauthenticated() {
        initializePossiblePages();
    }

    @Override
    protected void initializePossiblePages() {
        possiblePages = new ArrayList<>();
        possiblePages.add("login");
        possiblePages.add("register");
        possiblePages.add("homepage unauthenticated");
    }
}
