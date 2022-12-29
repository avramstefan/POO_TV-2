package action.actions;

import action.Action;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import movie.Movie;
import platform.Platform;

import java.util.ArrayList;

import static action.Utils.actionResult;
import static platform.Constants.*;

public class SearchAction implements ActionStrategy {
    public SearchAction() {

    }

    /**
     * Function used for searching movies and displaying on the platform
     * just those whose name starts with a given string.
     * @param action current action
     * @return ObjectNode output
     */
    public ObjectNode executeAction(Input inputData, Action action) {
        Platform platform = Platform.getInstance();

        // Checks if the current page is "movies" page.
        if (!platform.getCurrentPage().equals(MOVIES)) {
            return actionResult(ERROR);
        }

        platform.setUserAvailableMovies();

        // Keeping the movies that start with the given string from action's startsWith variable.
        ArrayList<Movie> searchedMovies = new ArrayList<>();
        for (Movie movie : platform.getAvailableMovies()) {
            if (movie.getName().startsWith(action.getStartsWith())) {
                searchedMovies.add(movie);
            }
        }
        platform.setAvailableMovies(searchedMovies);

        return actionResult(SUCCESS);
    }
}
