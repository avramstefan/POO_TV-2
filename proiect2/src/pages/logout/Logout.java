package pages.logout;

import pages.Page;

import java.util.ArrayList;

public final class Logout extends Page {

    public Logout() {
        initializePossiblePages();
    }

    @Override
    protected void initializePossiblePages() {
        possiblePages = new ArrayList<>();
        possiblePages.add("homepage neautentificat");
        possiblePages.add("logout");
    }
}
