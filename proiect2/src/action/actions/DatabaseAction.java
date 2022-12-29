package action.actions;

import action.Action;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import movie.MovieDatabase;

public class DatabaseAction implements ActionStrategy {
    public DatabaseAction() {

    }

    public ObjectNode executeAction(Input inputData, Action action) {
        if (action.getFeature().equals("add")) {
            return MovieDatabase.getInstance().addMovie(action.getAddedMovie());
        } else {
            return MovieDatabase.getInstance().deleteMovie(action.getDeletedMovie());
        }
    }
}
