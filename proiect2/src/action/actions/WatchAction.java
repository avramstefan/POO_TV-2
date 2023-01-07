package action.actions;

import action.Action;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import movie.Movie;
import platform.Platform;
import user.User;

import static action.Utils.actionResult;
import static platform.Constants.CHOSEN_MOVIE;
import static platform.Constants.DETAILS;
import static platform.Constants.ERROR;
import static platform.Constants.SUCCESS;

public class WatchAction implements ActionStrategy {
    public WatchAction() {

    }

    /**
     * Function used to quantify the fact that the user has watched a movie or not.
     * @return ObjectNode output
     */
    public ObjectNode executeAction(final Input inputData, final Action action) {
        Platform platform = Platform.getInstance();

        // Checks if the current page is "see details" page.
        if (!platform.getCurrentPage().equals(DETAILS)) {
            return actionResult(ERROR);
        }

        User currUser = platform.getLoggedUser();
        Movie movie = platform.getAvailableMovies().get(CHOSEN_MOVIE);

        // Checks if the user has purchased the movie before watching it.
        if (!currUser.getPurchasedMovies().contains(movie)) {
            return actionResult(ERROR);
        }

        if (!currUser.getWatchedMovies().contains(movie)) {
            currUser.getWatchedMovies().add(movie);
        }

        return actionResult(SUCCESS);
    }
}
