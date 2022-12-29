package action.actions;

import action.Action;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import movie.MovieDatabase;
import platform.Constants;
import platform.Platform;
import user.User;

import static action.Utils.actionResult;
import static action.Utils.getUserIdxByCredentials;
import static platform.Constants.SUCCESS;

public class RegisterAction implements ActionStrategy {
    public RegisterAction() {

    }

    /**
     * Function used for adding a new user into the system, actually being added
     * at the end of the platform's ArrayList of users.
     * @param inputData input data containing all the information needed
     * @param action current action
     * @return ObjectNode output
     */
    public ObjectNode executeAction(final Input inputData, final Action action) {
        Platform platform = Platform.getInstance();

        // Checks if the current page is "register" page.
        if (!platform.getCurrentPage().equals(Constants.REGISTER)) {
            return actionResult(Constants.ERROR);
        }

        /*
         * A helper function which returns -1 if the user does not appear to exist
         * in the system. In contrast to "login" action, if the index of the user differs
         * from -1, it means that there already exists a user with the same credentials,
         * so the registration is going to be invalid, triggering an error.
         */
        int userIdx = getUserIdxByCredentials(inputData.getUsers(), action.getCredentials());
        if (userIdx != -1) {
            return actionResult(Constants.ERROR);
        }

        // Creating a new user to be added in the system.
        User newUser = new User(action.getCredentials());
        inputData.getUsers().add(newUser);

        // Sets the parameters
        platform.setCurrentPage(Constants.HOMEPAGE_AUTHENTICATED);
        platform.setLoggedUser(newUser);

        MovieDatabase.getInstance().getObservers().add(newUser);
        return actionResult(SUCCESS);
    }
}
