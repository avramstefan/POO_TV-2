package action.actions;

import action.Action;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import platform.Platform;
import user.User;

import static action.Utils.actionResult;
import static platform.Constants.ERROR;
import static platform.Constants.UPGRADES;

public class BuyTokensAction implements ActionStrategy {

    public BuyTokensAction() {

    }

    /**
     * Function used for buying tokens, modifying user's credentials' balance
     * and tokens count.
     * @param action current action
     * @return ObjectNode output
     */
    public ObjectNode executeAction(final Input inputData, final Action action) {
        Platform platform = Platform.getInstance();

        // Checks if the current page is "upgrades" page.
        if (!platform.getCurrentPage().equals(UPGRADES)) {
            return actionResult(ERROR);
        }

        int count = action.getCount();
        User currUser = platform.getLoggedUser();

        // Checks if the user has enough balance to buy tokens.
        if (count > currUser.getCredentials().getBalance()) {
            return actionResult(ERROR);
        }

        // Setting the balance and tokens count.
        currUser.getCredentials().setBalance(currUser.getCredentials().getBalance() - count);
        currUser.setTokensCount(count + currUser.getTokensCount());

        return null;
    }
}
