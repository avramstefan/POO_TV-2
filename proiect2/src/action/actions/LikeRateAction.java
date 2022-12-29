package action.actions;

import action.Action;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import movie.Movie;
import platform.Platform;
import user.User;

import static action.Utils.actionResult;
import static platform.Constants.CHOSEN_MOVIE;
import static platform.Constants.DETAILS;
import static platform.Constants.ERROR;
import static platform.Constants.MAX_RATE;
import static platform.Constants.SUCCESS;

public class LikeRateAction implements ActionStrategy {
    public LikeRateAction() {

    }

    /**
     * Function used for "like" and "rate" actions, as their processes
     * are quite similar.
     * @param action current action
     * @return ObjectNode output
     */
    public ObjectNode executeAction(final Input inputData, final Action action) {
        Platform platform = Platform.getInstance();

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

        if (!user.getRatedMovies().contains(movie)) {
            user.getRatedMovies().add(movie);
        }

        if (!movie.getRatingOwners().contains(user)) {
            movie.setNumRatings(movie.getNumRatings() + 1);
            movie.getRatings().add(action.getRate());
            movie.getRatingOwners().add(user);
        } else {
            int idx = 0;
            for (int i = 0; i < movie.getRatingOwners().size(); i++) {
                if (movie.getRatingOwners().get(i) == user) {
                    idx = i;
                    break;
                }
            }
            movie.getRatings().set(idx, action.getRate());
        }

        movie.calculateRating();

        return actionResult(SUCCESS);
    }
}

