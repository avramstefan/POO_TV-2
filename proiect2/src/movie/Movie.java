package movie;

import java.util.ArrayList;

public final class Movie {
    private String name;
    private int year;
    private int duration;
    private ArrayList<String> genres;
    private ArrayList<String> actors;
    private ArrayList<String> countriesBanned;
    private ArrayList<Integer> ratings;
    private int numLikes;
    private double rating;
    private int numRatings;

    public Movie() {

    }

    /**
     * Function used for initializing the extra data that is not given by Input.
     */
    public void initializeExtraData() {
        this.numLikes = 0;
        this.rating = 0;
        this.numRatings = 0;
        this.ratings = new ArrayList<>();
    }

    /**
     * Function used for updating the movie's rating by summing the ratings and dividing the
     * sum by the number of given ratings.
     */
    public void calculateRating() {
        if (numRatings == 0) {
            return;
        }

        int sumRatings = 0;
        for (Integer ratingX : ratings) {
            sumRatings += ratingX;
        }

        this.rating = (double) sumRatings / numRatings;
    }

    public ArrayList<Integer> getRatings() {
        return ratings;
    }

    public void setRatings(final ArrayList<Integer> ratings) {
        this.ratings = ratings;
    }

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(final int numLikes) {
        this.numLikes = numLikes;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(final double rating) {
        this.rating = rating;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(final int numRatings) {
        this.numRatings = numRatings;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(final int year) {
        this.year = year;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(final ArrayList<String> genres) {
        this.genres = genres;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public void setActors(final ArrayList<String> actors) {
        this.actors = actors;
    }

    public ArrayList<String> getCountriesBanned() {
        return countriesBanned;
    }

    public void setCountriesBanned(final ArrayList<String> countriesBanned) {
        this.countriesBanned = countriesBanned;
    }
}
