package action.actions;

import action.Action;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import movie.Movie;
import platform.Platform;

import static action.Utils.actionResult;
import static platform.Constants.*;
import static platform.Constants.ERROR;

public class SubscribeAction implements ActionStrategy {
    public SubscribeAction() {

    }

    public ObjectNode executeAction(Input inputData, Action action) {
        Platform platform = Platform.getInstance();
        String subscribedGenre = action.getSubscribedGenre();

        if (!platform.getCurrentPage().equals(DETAILS)) {
            return actionResult(ERROR);
        }

        Movie movie = platform.getAvailableMovies().get(CHOSEN_MOVIE);

        if (!movie.getGenres().contains(subscribedGenre)) {
            return actionResult(ERROR);
        }

        if (platform.getLoggedUser().getSubscribedGenres().contains(subscribedGenre)) {
            return actionResult(ERROR);
        }

        platform.getLoggedUser().getSubscribedGenres().add(subscribedGenre);
        return null;
    }
}
