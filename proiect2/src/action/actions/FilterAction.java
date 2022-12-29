package action.actions;

import action.Action;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import movie.Movie;
import platform.Platform;

import java.util.ArrayList;

import static action.Utils.actionResult;
import static action.Utils.filterMovies;
import static platform.Constants.ERROR;
import static platform.Constants.MOVIES;
import static platform.Constants.SUCCESS;

public class FilterAction implements ActionStrategy {
    public FilterAction() {

    }

    /**
     * Function used to filter movies by some given conditions.
     * The actual filtering process takes place in Utils class.
     * @param action current action
     * @return ObjectNode output
     */
    public ObjectNode executeAction(final Input inputData, final Action action) {
        Platform platform = Platform.getInstance();

        // Checks if the current page is "movies" page.
        if (!platform.getCurrentPage().equals(MOVIES)) {
            return actionResult(ERROR);
        }

        platform.setUserAvailableMovies();

        ArrayList<Movie> filteredMovies = new ArrayList<>(platform.getAvailableMovies());

        filterMovies(filteredMovies, action.getFilters());
        platform.setAvailableMovies(filteredMovies);

        return actionResult(SUCCESS);
    }
}
