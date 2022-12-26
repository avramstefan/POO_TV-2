package action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import movie.Movie;
import pages.Page;
import platform.Platform;

import java.util.ArrayList;
import java.util.LinkedList;

import static action.Utils.actionResult;
import static platform.Constants.*;
import static platform.Constants.SUCCESS;

public class PageHandler implements Command {
    private static PageHandler pageHandler = null;
    private Platform platform;
    private Input inputData;
    private Action action;
    private ObjectNode actionNodeResult;
    private LinkedList<Action> changePageHistory;

    public PageHandler(Input inputData) {
        this.platform = Platform.getInstance();
        this.inputData = inputData;
        this.action = null;
        this.actionNodeResult = null;
        this.changePageHistory = new LinkedList<>();
    }

    public static synchronized PageHandler getInstance(final Input inputData) {
        if (pageHandler == null) {
            pageHandler = new PageHandler(inputData);
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
        if (!platform.getPages().get(platform.getCurrentPage()).canChangePage(action.getPage())) {
            actionNodeResult = actionResult(ERROR);
            return;
        }

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

        platform.setAvailableMovies(inputData.getMovies());

        /*
         * Removing movies that are currently banned in the logged user's country
         * as soon as it connects on the platform.
         */
        if (!action.getPage().equals(LOGIN) && !action.getPage().equals(REGISTER)
                && !action.getPage().equals(HOMEPAGE_UNAUTHENTICATED)) {
            platform.removeBannedMovies();
        }

        platform.setCurrentPage(action.getPage());

        // Disconnects a user and resets the parameters.
        if (action.getPage().equals(LOGOUT)) {
            platform.setLoggedUser(null);
            platform.setCurrentPage(HOMEPAGE_UNAUTHENTICATED);
            platform.setAvailableMovies(inputData.getMovies());
            this.changePageHistory.clear();
        }

        changePageHistory.addLast(this.action);
        if (action.getPage().equals(MOVIES)) {
            actionNodeResult = actionResult(SUCCESS);
            return;
        }
        actionNodeResult = null;
    }


    public void undo() {
        if (changePageHistory.size() == 0) {
            actionNodeResult = actionResult(ERROR);
            return;
        }

        Action previousAction = changePageHistory.removeLast();

        if (previousAction.getPage().equals(LOGIN)
            || previousAction.getPage().equals(REGISTER)) {
            actionNodeResult = actionResult(ERROR);
            return;
        }

        this.setCurrentAction(previousAction);
        execute();
    }

    public Action getCurrentAction() {
        return action;
    }

    public void setCurrentAction(Action action) {
        this.action = action;
    }

    public ObjectNode getActionResult() {
        return actionNodeResult;
    }

    public void setActionResult(ObjectNode actionNodeResult) {
        this.actionNodeResult = actionNodeResult;
    }
}
