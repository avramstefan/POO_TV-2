package action.actions;

import action.Action;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import movie.MovieDatabase;

public class DatabaseAction implements ActionStrategy {
    public DatabaseAction() {

    }

    /**
     * Function that parse the action of adding or deleting a movie
     * from the MovieDatabase.
     * @param inputData
     * @param action
     * @return ObjectNode
     */
    public ObjectNode executeAction(final Input inputData, final Action action) {
        if (action.getFeature().equals("add")) {
            return MovieDatabase.getInstance().addMovie(action.getAddedMovie());
        } else {
            return MovieDatabase.getInstance().deleteMovie(action.getDeletedMovie());
        }
    }
}
