package action.actions;

import action.Action;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import movie.Movie;
import platform.Platform;

import static action.Utils.actionResult;
import static platform.Constants.CHOSEN_MOVIE;
import static platform.Constants.DETAILS;
import static platform.Constants.ERROR;

public class SubscribeAction implements ActionStrategy {
    public SubscribeAction() {

    }

    /**
     * Method used for the process of subscribing a user to a given genre.
     * @param inputData
     * @param action
     * @return ObjectNode
     */
    public ObjectNode executeAction(final Input inputData, final Action action) {
        Platform platform = Platform.getInstance();
        String subscribedGenre = action.getSubscribedGenre();

        // Checks if the current page is "See Details".
        if (!platform.getCurrentPage().equals(DETAILS)) {
            return actionResult(ERROR);
        }

        Movie movie = platform.getAvailableMovies().get(CHOSEN_MOVIE);

        // Checks if the visualized movie contains the given genre.
        if (!movie.getGenres().contains(subscribedGenre)) {
            return actionResult(ERROR);
        }

        // Checks if the user already subscribed to the given genre.
        if (platform.getLoggedUser().getSubscribedGenres().contains(subscribedGenre)) {
            return actionResult(ERROR);
        }

        platform.getLoggedUser().getSubscribedGenres().add(subscribedGenre);
        return null;
    }
}
