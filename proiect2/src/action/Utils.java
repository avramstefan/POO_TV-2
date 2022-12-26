package action;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import movie.Movie;
import platform.Platform;
import user.Credentials;
import user.User;

import java.util.ArrayList;

import static platform.Constants.*;

public final class Utils {

    private static Platform platform;

    private Utils() {

    }

    public static void setPlatform() {
        Utils.platform = Platform.getInstance();
    }

    /**
     * Setting the ObjectNode output, treating the case when
     * an error occurred or not.
     * @param isNotError true if there is not an error case
     * @return ObjectNode output
     */
    public static ObjectNode actionResult(final boolean isNotError) {
        ObjectNode node = (new ObjectMapper()).createObjectNode();

        if (!isNotError) {
            node.put("error", "Error");

            if (platform.getCurrentPage().equals(LOGIN)
                    || platform.getCurrentPage().equals(REGISTER)) {
                platform.setCurrentPage(HOMEPAGE_UNAUTHENTICATED);
            }
        } else {
            node.put("error", (JsonNode) null);
        }

        ArrayNode moviesObj = node.putArray("currentMoviesList");

        if (isNotError && (platform.getCurrentPage().equals(MOVIES)
                || platform.getCurrentPage().equals(DETAILS))) {
            serializeMovies(platform.getAvailableMovies(), moviesObj);
        }

        if (platform.getLoggedUser() != null && isNotError) {
            node.set("currentUser", serializeUserCredentials(platform.getLoggedUser()));
        } else {
            node.set("currentUser", null);
        }

        return node;
    }

    /**
     * Helper function used to give the index of the user's position in the ArrayList.
     * @param users ArrayList of existing users in the system
     * @param actCredentials given credentials for the current action
     * @return -1 if there is no user with the given credentials, otherwise
     * returns the index at which the user may be found in the ArrayList of users
     */
    public static int getUserIdxByCredentials(final ArrayList<User> users,
                                              final Credentials actCredentials) {
        for (int i = 0; i < users.size(); i++) {
            Credentials userCredentials = users.get(i).getCredentials();

            if (userCredentials.getName().equals(actCredentials.getName())
                && userCredentials.getPassword().equals(actCredentials.getPassword())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Function used for serializing a movie and transforming its information into an ObjectNode.
     * @param movieNode ObjectNode which will keep the serialized information about the given movie
     * @param movie to be serialized
     */
    public static void serializeSingleMovie(final ObjectNode movieNode, final Movie movie) {
        movieNode.put("name", movie.getName());
        movieNode.put("year", movie.getYear());
        movieNode.put("duration", movie.getDuration());
        movieNode.put("numLikes", movie.getNumLikes());
        movieNode.put("rating", movie.getRating());
        movieNode.put("numRatings", movie.getNumRatings());

        ArrayNode genresNode = movieNode.putArray("genres");
        ArrayNode actorsNode = movieNode.putArray("actors");
        ArrayNode countriesBanned = movieNode.putArray("countriesBanned");

        for (String genre : movie.getGenres()) {
            genresNode.add(genre);
        }

        for (String actor : movie.getActors()) {
            actorsNode.add(actor);
        }

        for (String country : movie.getCountriesBanned()) {
            countriesBanned.add(country);
        }
    }

    /**
     * Function used for serializing a user and transforming its information into an ObjectNode.
     * @param user to be serialized
     * @return serialized user's information in form of ObjectNode
     */
    public static ObjectNode serializeUserCredentials(final User user) {
        ObjectNode objectNode = (new ObjectMapper()).createObjectNode();
        ObjectNode credentialsNode = (new ObjectMapper()).createObjectNode();

        Credentials credentials = user.getCredentials();
        credentialsNode.put("name", credentials.getName());
        credentialsNode.put("password", credentials.getPassword());
        credentialsNode.put("accountType", credentials.getAccountType());
        credentialsNode.put("country", credentials.getCountry());
        credentialsNode.put("balance", String.valueOf(credentials.getBalance()));
        objectNode.set("credentials", credentialsNode);

        objectNode.put("tokensCount", user.getTokensCount());
        objectNode.put("numFreePremiumMovies", user.getNumFreePremiumMovies());

        ArrayNode purchasedMoviesNode = objectNode.putArray("purchasedMovies");
        ArrayNode watchedMoviesNode = objectNode.putArray("watchedMovies");
        ArrayNode likedMoviesNode = objectNode.putArray("likedMovies");
        ArrayNode ratedMoviesNode = objectNode.putArray("ratedMovies");
        // TODO vezi ca notificarile nu sunt bune prostule
        ArrayNode notificationsNode = objectNode.putArray("notifications");

        for (Movie movie : user.getNotifications()){
            ObjectNode movieNode = (new ObjectMapper()).createObjectNode();
            serializeSingleMovie(movieNode, movie);
            purchasedMoviesNode.add(movieNode);
        }

        for (Movie movie : user.getPurchasedMovies()) {
            ObjectNode movieNode = (new ObjectMapper()).createObjectNode();
            serializeSingleMovie(movieNode, movie);
            purchasedMoviesNode.add(movieNode);
        }

        for (Movie movie : user.getWatchedMovies()) {
            ObjectNode movieNode = (new ObjectMapper()).createObjectNode();
            serializeSingleMovie(movieNode, movie);
            watchedMoviesNode.add(movieNode);
        }

        for (Movie movie : user.getLikedMovies()) {
            ObjectNode movieNode = (new ObjectMapper()).createObjectNode();
            serializeSingleMovie(movieNode, movie);
            likedMoviesNode.add(movieNode);
        }

        for (Movie movie : user.getRatedMovies()) {
            ObjectNode movieNode = (new ObjectMapper()).createObjectNode();
            serializeSingleMovie(movieNode, movie);
            ratedMoviesNode.add(movieNode);
        }

        return objectNode;
    }

    /**
     * Function used to serialize movies.
     * @param movies ArrayList of movies to be serialized
     * @param moviesArrayObj ArrayNode which keeps the ObjectNodes referenced by each movie
     */
    public static void serializeMovies(final ArrayList<Movie> movies,
                                       final ArrayNode moviesArrayObj) {
        for (Movie movie : movies) {
            ObjectNode movieNode = (new ObjectMapper()).createObjectNode();
            serializeSingleMovie(movieNode, movie);
            moviesArrayObj.add(movieNode);
        }
    }

    /**
     * Function used for sorting an ArrayList of movies by rating.
     * @param m1 movie 1
     * @param m2 movie 2
     * @param order "increasing" or "decreasing"
     * @return -1, 0 or 1
     */
    public static int compareByRating(final Movie m1, final Movie m2, final String order) {
        int normalOrderRetValue = 1;

        if (order.equals("decreasing")) {
            normalOrderRetValue = -1;
        }

        if (m1.getRating() > m2.getRating()) {
            return normalOrderRetValue;
        } else if (m1.getRating() < m2.getRating()) {
            return (-1) * normalOrderRetValue;
        }
        return 0;
    }

    /**
     * Function used for sorting an ArrayList of movies by duration. If the option of sorting
     * the movies by ratings exists and the durations of two movies are equal, then the
     * function will proceed with calling "compareByRating()" function.
     * @param m1 movie 1
     * @param m2 movie 2
     * @param filters Filter object which keeps information about the filtering options
     * @return -1, 0 or 1
     */
    public static int compareByDuration(final Movie m1, final Movie m2, final Filter filters) {
        int normalOrderRetValue = 1;

        if (filters.getSort().getDuration().equals("decreasing")) {
            normalOrderRetValue = -1;
        }

        if (m1.getDuration() > m2.getDuration()) {
            return normalOrderRetValue;
        } else if (m1.getDuration() < m2.getDuration()) {
            return (-1) * normalOrderRetValue;
        }

        // Checks if movies should be sorted by rating in case of duration equity
        if (filters.getSort().getRating() != null) {
            return compareByRating(m1, m2, filters.getSort().getRating());
        }
        return 0;
    }

    /**
     * Function used for filtering movies.
     * @param movies ArrayList of movies to be filtered
     * @param filters Filter object which keeps information about the filtering options
     */
    public static void filterMovies(final ArrayList<Movie> movies, final Filter filters) {

        Filter.SortClass sortObj = filters.getSort();

        // Checks if the movies should be sorted just by rating.
        if (sortObj != null && sortObj.getDuration() == null && sortObj.getRating() != null) {
            movies.sort((m1, m2) -> compareByRating(m1, m2, filters.getSort().getRating()));
        }

        // Checks if the movies should be sorted by duration.
        if (sortObj != null && sortObj.getDuration() != null) {
            movies.sort((m1, m2) -> compareByDuration(m1, m2, filters));
        }

        Filter.Container containerObj = filters.getContains();

        // Keeping the movies that contain the given actors.
        if (containerObj != null && containerObj.getActors() != null) {
            ArrayList<Movie> copyMovieArray = new ArrayList<>(movies);
            for (Movie movie : copyMovieArray) {
                boolean hasActors = true;
                for (String actor : containerObj.getActors()) {
                    if (!movie.getActors().contains(actor)) {
                        hasActors = false;
                        break;
                    }
                }

                if (!hasActors) {
                    movies.remove(movie);
                }
            }
        }

        // Keeping the movies that contain the given genres.
        if (containerObj != null && containerObj.getGenres() != null) {
            ArrayList<Movie> copyMovieArray = new ArrayList<>(movies);
            for (Movie movie : copyMovieArray) {
                boolean hasGenres = true;
                for (String genre : containerObj.getGenres()) {
                    if (!movie.getGenres().contains(genre)) {
                        hasGenres = false;
                        break;
                    }
                }

                if (!hasGenres) {
                    movies.remove(movie);
                }
            }
        }
    }
}
