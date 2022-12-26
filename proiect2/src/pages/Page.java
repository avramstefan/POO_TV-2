package pages;

import java.util.ArrayList;

public abstract class Page {
    protected ArrayList<String> possiblePages;

    public Page() {
    }

    /**
     * Function used for initializing the pages where the platform may
     * be redirected from the current page.
     */
    protected abstract void initializePossiblePages();

    /**
     * Function used for signaling if the changing page process is possible by analyzing
     * if the current page's "possible pages" contain this given page.
     * @param pageToChange name of the page that the user wants to go at.
     * @return True if the platform may change its current page to the given page, False otherwise
     */
    public final boolean canChangePage(final String pageToChange) {
        return possiblePages.contains(pageToChange);
    }

    public final ArrayList<String> getPossiblePages() {
        return possiblePages;
    }

    public final void setPossiblePages(final ArrayList<String> possiblePages) {
        this.possiblePages = possiblePages;
    }
}
