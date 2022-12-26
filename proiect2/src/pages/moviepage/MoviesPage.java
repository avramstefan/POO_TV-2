package pages.moviepage;

import pages.Page;

import java.util.ArrayList;

public final class MoviesPage extends Page {

    public MoviesPage() {
        initializePossiblePages();
    }

    @Override
    protected void initializePossiblePages() {
        possiblePages = new ArrayList<>();
        possiblePages.add("homepage autentificat");
        possiblePages.add("see details");
        possiblePages.add("logout");
        possiblePages.add("movies");
    }
}
