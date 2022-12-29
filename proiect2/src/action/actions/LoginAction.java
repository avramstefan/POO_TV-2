package action.actions;

import action.Action;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import platform.Platform;

import static action.Utils.actionResult;
import static action.Utils.getUserIdxByCredentials;
import static platform.Constants.*;
import static platform.Constants.SUCCESS;

public class LoginAction implements ActionStrategy {

    public LoginAction() {

    }

    /**
     * Function used for logging a user into the system.
     * @param inputData input data containing all the information needed
     * @param action current action
     * @return ObjectNode output
     */
    public ObjectNode executeAction(Input inputData, Action action) {
        Platform platform = Platform.getInstance();

        // Checks if the current page is "login" page.
        if (!platform.getCurrentPage().equals(LOGIN)) {
            return actionResult(ERROR);
        }

        /*
         * A helper function which returns -1 if the user does not appear to exist
         * in the system.
         */
        int userIdx = getUserIdxByCredentials(inputData.getUsers(), action.getCredentials());
        if (userIdx == -1) {
            return actionResult(ERROR);
        }

        // Sets the parameters
        platform.setLoggedUser(inputData.getUsers().get(userIdx));
        platform.setCurrentPage(HOMEPAGE_AUTHENTICATED);

        return actionResult(SUCCESS);
    }
}
