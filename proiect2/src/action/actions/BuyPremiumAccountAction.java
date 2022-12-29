package action.actions;

import action.Action;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import platform.Platform;
import user.User;

import static action.Utils.actionResult;
import static platform.Constants.ERROR;
import static platform.Constants.PREMIUM_ACCOUNT_TOKENS_PRICE;
import static platform.Constants.UPGRADES;

public class BuyPremiumAccountAction implements ActionStrategy {

    public BuyPremiumAccountAction() {

    }

    /**
     * Upgrades the user's account type by buying a premium account.
     * @return ObjectNode output
     */
    public ObjectNode executeAction(final Input inputData, final Action action) {
        Platform platform = Platform.getInstance();
        // Checks if the current page is "upgrades" page.
        if (!platform.getCurrentPage().equals(UPGRADES)) {
            return actionResult(ERROR);
        }

        User currUser = platform.getLoggedUser();

        // Checks if the user has enough tokens and if its account type is not already premium.
        if (!currUser.getCredentials().getAccountType().equals("standard")
                && currUser.getTokensCount() < PREMIUM_ACCOUNT_TOKENS_PRICE) {
            return actionResult(ERROR);
        }

        // Setting the tokens count and account type.
        currUser.setTokensCount(currUser.getTokensCount() - PREMIUM_ACCOUNT_TOKENS_PRICE);
        currUser.getCredentials().setAccountType("premium");

        return null;
    }
}
