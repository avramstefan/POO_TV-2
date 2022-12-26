package user;

import movie.Movie;

import java.util.ArrayList;

import static platform.Constants.START_NUM_FREE_PREMIUM_MOVIES;

public final class User {
    private Credentials credentials;
    private int tokensCount;
    private int numFreePremiumMovies;
    private ArrayList<Movie> purchasedMovies;
    private ArrayList<Movie> watchedMovies;
    private ArrayList<Movie> likedMovies;
    private ArrayList<Movie> ratedMovies;
    private ArrayList<Movie> notifications;

    public User() {

    }

    public User(final Credentials credentials) {
        this.credentials = credentials;
        initializeExtraData();
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
    }

    public ArrayList<Movie> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Movie> notifications) {
        this.notifications = notifications;
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
