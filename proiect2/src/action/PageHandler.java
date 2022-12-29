package action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import movie.Movie;
import movie.MovieDatabase;
import platform.Platform;
import java.util.LinkedList;

import static action.Utils.actionResult;
import static platform.Constants.DETAILS;
import static platform.Constants.ERROR;
import static platform.Constants.HOMEPAGE_AUTHENTICATED;
import static platform.Constants.HOMEPAGE_UNAUTHENTICATED;
import static platform.Constants.LOGIN;
import static platform.Constants.LOGOUT;
import static platform.Constants.MOVIES;
import static platform.Constants.REGISTER;
import static platform.Constants.SUCCESS;

public final class PageHandler implements Command {
    private static PageHandler pageHandler = null;
    private final Platform platform;
    private Action action;
    private ObjectNode actionNodeResult;
    private final LinkedList<Action> changePageHistory;
    private boolean undoAction = false;

    public PageHandler() {
        this.platform = Platform.getInstance();
        this.action = null;
        this.actionNodeResult = null;
        this.changePageHistory = new LinkedList<>();
    }

    /**
     * Singleton method for accessing a unique PageHandler.
     * @return instance of PageHandler
     */
    public static synchronized PageHandler getInstance() {
        if (pageHandler == null) {
            pageHandler = new PageHandler();
        }
        return pageHandler;
    }

    /**
     * The main function that represents the "change page" action.
     * It is treating different cases and sets the movies and
     * the current page that may be displayed on the platform.
     */
    public void execute() {
        // Checks if it is possible to change the page from the current page.
        if (!platform.getPages().get(platform.getCurrentPage()).canChangePage(action.getPage())
            && !undoAction) {
            actionNodeResult = actionResult(ERROR);
            return;
        }

        undoAction = false;

        /*
         * If the page is going to change in "see details", then there will be
         * just one movie displayed on the screen.
         */
        if (action.getMovie() != null && action.getPage().equals(DETAILS)) {
            Movie movie = platform.getMovieByName(action.getMovie());

            if (movie == null) {
                actionNodeResult = actionResult(ERROR);
                return;
            }

            if (movie.getCountriesBanned().contains(platform.getLoggedUser().
                    getCredentials().getCountry())) {
                actionNodeResult = actionResult(ERROR);
                return;
            }

            if (!platform.getAvailableMovies().contains(movie)) {
                actionNodeResult = actionResult(ERROR);
                return;
            }

            platform.setAvailableMovies(action.getMovie());
            platform.setCurrentPage(action.getPage());
            actionNodeResult = actionResult(SUCCESS);
            changePageHistory.addLast(this.action);
            return;
        }

        platform.setAvailableMovies(MovieDatabase.getInstance().getMovies());

        /*
         * Removing movies that are currently banned in the logged user's country
         * as soon as it connects on the platform.
         */
        if (!action.getPage().equals(LOGIN) && !action.getPage().equals(REGISTER)
                && !action.getPage().equals(HOMEPAGE_UNAUTHENTICATED)) {
            platform.removeBannedMovies();
            changePageHistory.addLast(this.action);
        }

        platform.setCurrentPage(action.getPage());

        // Disconnects a user and resets the parameters.
        if (action.getPage().equals(LOGOUT)) {
            platform.setLoggedUser(null);
            platform.setCurrentPage(HOMEPAGE_UNAUTHENTICATED);
            platform.setAvailableMovies(MovieDatabase.getInstance().getMovies());
            this.changePageHistory.clear();
            actionNodeResult = null;
            return;
        }

        if (action.getPage().equals(MOVIES)) {
            actionNodeResult = actionResult(SUCCESS);
            return;
        }
        actionNodeResult = null;
    }


    /**
     * Main function used for going backwards in the chain of changing page actions.
     * It removes the last action from the linked list, as it is the action that
     * has set the platform's current page. After that, it takes the second
     * last action and sets it as the current action and then triggers
     * execute() function.
     */
    public void undo() {
        if (changePageHistory.size() == 0) {
            actionNodeResult = actionResult(ERROR);
            return;
        }

        changePageHistory.removeLast();

        /*
         Check if there are any more actions in the linked list
         or if the second last action is redirecting the platform
         to the HOMEPAGE_AUTHENTICATED.
        */
        if (changePageHistory.size() == 0
                || changePageHistory.getLast().getPage().
                equals(HOMEPAGE_AUTHENTICATED)) {
            platform.setCurrentPage(HOMEPAGE_AUTHENTICATED);
            actionNodeResult = null;
            return;
        }

        this.setCurrentAction(changePageHistory.removeLast());
        undoAction = true;
        execute();
    }

    public static void setPageHandler(final PageHandler pageHandler) {
        PageHandler.pageHandler = pageHandler;
    }

    public void setCurrentAction(final Action givenAction) {
        this.action = givenAction;
    }

    public ObjectNode getActionResult() {
        return actionNodeResult;
    }

    public void setActionResult(final ObjectNode actionNode) {
        this.actionNodeResult = actionNode;
    }
}
