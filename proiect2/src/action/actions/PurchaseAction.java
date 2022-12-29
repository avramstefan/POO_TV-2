package action.actions;

import action.Action;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import movie.Movie;
import platform.Platform;
import user.User;

import static action.Utils.actionResult;
import static platform.Constants.*;

public class PurchaseAction implements ActionStrategy {
    public PurchaseAction() {

    }

    /**
     * Function used for buying a movie and adding it to user's purchased movies ArrayList.
     * @return ObjectNode output
     */
    public ObjectNode executeAction(Input inputData, Action action) {
        Platform platform = Platform.getInstance();

        // Checks if the current page is "see details" page.
        if (!platform.getCurrentPage().equals(DETAILS)) {
            return actionResult(ERROR);
        }

        User currUser = platform.getLoggedUser();
        Movie movie = platform.getAvailableMovies().get(CHOSEN_MOVIE);

        if (currUser.getPurchasedMovies().contains(movie)) {
            return actionResult(ERROR);
        }

        /*
         * If the user has a standard account type, then it has to pay tokens for buying the movie.
         * If the user has a premium account type and there are free premium movies left to buy,
         * then the user won't pay any tokens.
         */
        if (currUser.getCredentials().getAccountType().equals("standard")) {
            // Checks if the user has enough tokens to buy a movie.
            if (currUser.getTokensCount() < MOVIE_TOKENS_PRICE) {
                return actionResult(ERROR);
            }

            // Setting the tokens count and user's purchased movies ArrayList.
            currUser.setTokensCount(currUser.getTokensCount() - MOVIE_TOKENS_PRICE);
            currUser.getPurchasedMovies().add(movie);
        } else {
            /*
             * Checks if there are free premium movies left or the user has enough tokens left
             * in case of zero free premium movies left.
             */
            if (currUser.getNumFreePremiumMovies() <= 0
                    && currUser.getTokensCount() < MOVIE_TOKENS_PRICE) {
                return actionResult(ERROR);
            }

            // Setting the tokens count and user's purchased movies ArrayList.
            if (currUser.getNumFreePremiumMovies() <= 0) {
                currUser.setTokensCount(currUser.getTokensCount() - MOVIE_TOKENS_PRICE);
            } else {
                currUser.setNumFreePremiumMovies(currUser.getNumFreePremiumMovies() - 1);
            }

            currUser.getPurchasedMovies().add(movie);
        }

        return actionResult(SUCCESS);
    }
}
