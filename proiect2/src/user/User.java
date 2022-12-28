package user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import movie.Movie;
import movie.MovieDatabase;
import movie.MovieObserver;

import java.util.AbstractMap;
import java.util.ArrayList;

import static action.Utils.actionResult;
import static platform.Constants.ERROR;
import static platform.Constants.START_NUM_FREE_PREMIUM_MOVIES;

public final class User implements MovieObserver {
    private Credentials credentials;
    private int tokensCount;
    private int numFreePremiumMovies;
    private ArrayList<Movie> purchasedMovies;
    private ArrayList<Movie> watchedMovies;
    private ArrayList<Movie> likedMovies;
    private ArrayList<Movie> ratedMovies;
    private ArrayList<Notification> notifications;
    private ArrayList<String> subscribedGenres;

    public static class Notification {
        private String movieName;
        private String message;

        public Notification(String movieName, String message) {
            this.movieName = movieName;
            this.message = message;
        }

        public String getMovieName() {
            return movieName;
        }

        public void setMovieName(String movieName) {
            this.movieName = movieName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public User() {

    }

    public User(final Credentials credentials) {
        this.credentials = credentials;
        initializeExtraData();
    }

    private void addNotify(Movie movie) {
        boolean shouldBeNotified = false;

        for (String genre: movie.getGenres()) {
            for (String subscribedGenre: subscribedGenres) {
                if (genre.equals(subscribedGenre)) {
                    shouldBeNotified = true;
                    break;
                }
            }
        }

        if (!shouldBeNotified) {
            return;
        }

        notifications.add(new Notification(movie.getName(), "ADD"));
    }

    private void deleteNotify(Movie deletedMovie) {
        if (!purchasedMovies.contains(deletedMovie)) {
            return;
        }

        purchasedMovies.remove(deletedMovie);
        watchedMovies.remove(deletedMovie);
        likedMovies.remove(deletedMovie);
        ratedMovies.remove(deletedMovie);

        if (credentials.getAccountType().equals("premium")) {
            numFreePremiumMovies++;
        } else {
            tokensCount += 2;
        }

        notifications.add(new Notification(deletedMovie.getName(), "DELETE"));
    }

    @Override
    public void update(Movie movie) {
        if (MovieDatabase.getInstance().getMovies().contains(movie)) {
            addNotify(movie);
        } else {
            deleteNotify(movie);
        }
    }

    /**
     * Function used for initializing the user's extra data that is not given by Input.
     */
    public void initializeExtraData() {
        this.tokensCount = 0;
        this.numFreePremiumMovies = START_NUM_FREE_PREMIUM_MOVIES;
        purchasedMovies = new ArrayList<>();
        watchedMovies = new ArrayList<>();
        likedMovies = new ArrayList<>();
        ratedMovies = new ArrayList<>();
        notifications = new ArrayList<>();
        subscribedGenres = new ArrayList<>();
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    public ArrayList<String> getSubscribedGenres() {
        return subscribedGenres;
    }

    public void setSubscribedGenres(ArrayList<String> subscribedGenres) {
        this.subscribedGenres = subscribedGenres;
    }

    public int getTokensCount() {
        return tokensCount;
    }

    public void setTokensCount(final int tokensCount) {
        this.tokensCount = tokensCount;
    }

    public int getNumFreePremiumMovies() {
        return numFreePremiumMovies;
    }

    public void setNumFreePremiumMovies(final int numFreePremiumMovies) {
        this.numFreePremiumMovies = numFreePremiumMovies;
    }

    public ArrayList<Movie> getPurchasedMovies() {
        return purchasedMovies;
    }

    public void setPurchasedMovies(final ArrayList<Movie> purchasedMovies) {
        this.purchasedMovies = purchasedMovies;
    }

    public ArrayList<Movie> getWatchedMovies() {
        return watchedMovies;
    }

    public void setWatchedMovies(final ArrayList<Movie> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }

    public ArrayList<Movie> getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(final ArrayList<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }

    public ArrayList<Movie> getRatedMovies() {
        return ratedMovies;
    }

    public void setRatedMovies(final ArrayList<Movie> ratedMovies) {
        this.ratedMovies = ratedMovies;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(final Credentials credentials) {
        this.credentials = credentials;
    }
}
