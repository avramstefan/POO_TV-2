package action;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import movie.Movie;
import platform.Platform;
import user.User;

import java.util.ArrayList;

import static action.Utils.*;
import static platform.Constants.*;

public final class ActionExec {

    private static Platform platform;

    private ActionExec() {

    }

    /**
     * Setting the unique platform instance created as a Singleton.
     */
    public static void setPlatform() {
        ActionExec.platform = Platform.getInstance();
    }

    /**
     * Function used for logging a user into the system.
     * @param inputData input data containing all the information needed
     * @param action current action
     * @return ObjectNode output
     */
    public static ObjectNode login(final Input inputData, final Action action) {
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

    /**
     * Function used for adding a new user into the system, actually being added
     * at the end of the platform's ArrayList of users.
     * @param inputData input data containing all the information needed
     * @param action current action
     * @return ObjectNode output
     */
    public static ObjectNode register(final Input inputData, final Action action) {
        // Checks if the current page is "register" page.
        if (!platform.getCurrentPage().equals(REGISTER)) {
            return actionResult(ERROR);
        }

        /*
         * A helper function which returns -1 if the user does not appear to exist
         * in the system. In contrast to "login" action, if the index of the user differs
         * from -1, it means that there already exists a user with the same credentials,
         * so the registration is going to be invalid, triggering an error.
         */
        int userIdx = getUserIdxByCredentials(inputData.getUsers(), action.getCredentials());
        if (userIdx != -1) {
            return actionResult(ERROR);
        }

        // Creating a new user to be added in the system.
        User newUser = new User(action.getCredentials());
        inputData.getUsers().add(newUser);

        // Sets the parameters
        platform.setCurrentPage(HOMEPAGE_AUTHENTICATED);
        platform.setLoggedUser(newUser);

        return actionResult(SUCCESS);
    }

    /**
     * Function used for searching movies and displaying on the platform
     * just those whose name starts with a given string.
     * @param action current action
     * @return ObjectNode output
     */
    public static ObjectNode search(final Action action) {
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

    /**
     * Function used to filter movies by some given conditions.
     * The actual filtering process takes place in Utils class.
     * @param action current action
     * @return ObjectNode output
     */
    public static ObjectNode filter(final Action action) {
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

    /**
     * Function used for buying tokens, modifying user's credentials' balance
     * and tokens count.
     * @param action current action
     * @return ObjectNode output
     */
    public static ObjectNode buyTokens(final Action action) {
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
        currUser.setTokensCount(count);

        return null;
    }

    /**
     * Upgrades the user's account type by buying a premium account.
     * @return ObjectNode output
     */
    public static ObjectNode buyPremiumAccount() {
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

    /**
     * Function used for buying a movie and adding it to user's purchased movies ArrayList.
     * @return ObjectNode output
     */
    public static ObjectNode purchase() {
        // Checks if the current page is "see details" page.
        if (!platform.getCurrentPage().equals(DETAILS)) {
            return actionResult(ERROR);
        }

        User currUser = platform.getLoggedUser();
        Movie movie = platform.getAvailableMovies().get(CHOSEN_MOVIE);

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

    /**
     * Function used to quantify the fact that the user has watched a movie or not.
     * @return ObjectNode output
     */
    public static ObjectNode watch() {
        // Checks if the current page is "see details" page.
        if (!platform.getCurrentPage().equals(DETAILS)) {
            return actionResult(ERROR);
        }

        User currUser = platform.getLoggedUser();
        Movie movie = platform.getAvailableMovies().get(CHOSEN_MOVIE);

        // Checks if the user has purchased the movie before watching it.
        if (!currUser.getPurchasedMovies().contains(movie)) {
            return actionResult(ERROR);
        }

        currUser.getWatchedMovies().add(movie);

        return actionResult(SUCCESS);
    }

    /**
     * Function used for "like" and "rate" actions, as their processes
     * are quite similar.
     * @param action current action
     * @return ObjectNode output
     */
    public static ObjectNode likeOrRate(final Action action) {
        // Checks if the current page is "see details" page.
        if (!platform.getCurrentPage().equals(DETAILS)) {
            return actionResult(ERROR);
        }

        User currUser = platform.getLoggedUser();
        Movie movie = platform.getAvailableMovies().get(CHOSEN_MOVIE);

        // Checks if the current user has watched the movie first.
        if (!currUser.getWatchedMovies().contains(movie)) {
            return actionResult(ERROR);
        }

        // Redirects the function to "like" or "rate" functions
        if (action.getFeature().equals("like")) {
            return like(currUser, movie);
        } else {
            return rate(action, currUser, movie);
        }
    }

    /**
     * Function used for liking a movie. The movie is added to the user's
     * liked movies ArrayList and the number of movie's likes increases.
     * @param user current logged user
     * @param movie movie to be liked
     * @return ObjectNode output
     */
    public static ObjectNode like(final User user, final Movie movie) {
        user.getLikedMovies().add(movie);
        movie.setNumLikes(movie.getNumLikes() + 1);

        return actionResult(SUCCESS);
    }

    /**
     * Function used for rating a movie. The movie is added to user's
     * rated movies ArrayList and the movie's rating is updated.
     * @param action current action
     * @param user current logged user
     * @param movie movie to be liked
     * @return ObjectNode output
     */
    public static ObjectNode rate(final Action action, final User user, final Movie movie) {
        // Checks if the given rate is valid
        if (action.getRate() > MAX_RATE || action.getRate() < 0) {
            return actionResult(ERROR);
        }

        user.getRatedMovies().add(movie);
        movie.getRatings().add(action.getRate());
        movie.setNumRatings(movie.getNumRatings() + 1);
        movie.calculateRating();

        return actionResult(SUCCESS);
    }
}
