package pages.unloggedhomepage;

import pages.Page;

import java.util.ArrayList;

public final class Login extends Page {

    public Login() {
        initializePossiblePages();
    }

    @Override
    protected void initializePossiblePages() {
        possiblePages = new ArrayList<>();
        possiblePages.add("login");
    }
}
