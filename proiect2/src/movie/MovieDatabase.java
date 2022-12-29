package movie;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import platform.Platform;

import java.util.ArrayList;

import static action.Utils.actionResult;
import static platform.Constants.ERROR;

public final class MovieDatabase {
    private ArrayList<Movie> movies;
    private static MovieDatabase movieDatabase = null;
    private final ArrayList<MovieObserver> observers;

    public MovieDatabase(final Input inputData) {
        this.movies = inputData.getMovies();

        observers = new ArrayList<>();
        observers.add(Platform.getInstance());
        observers.addAll(inputData.getUsers());
    }

    /**
     * SINGLETON method to assure the creation of a unique platform, used in Main function
     * with Input parameter for initializing data in constructor.
     * @param inputData variable containing a reference to Input class
     */
    public static synchronized MovieDatabase getInstance(final Input inputData) {
        if (movieDatabase == null) {
            movieDatabase = new MovieDatabase(inputData);
        }
        return movieDatabase;
    }

    /**
     * Function used for notifying the observers about the current event.
     * @param addedMovie
     */
    private void notify(final Movie addedMovie) {
        for (MovieObserver observer: observers) {
            observer.update(addedMovie);
        }
    }

    /**
     * SINGLETON method to assure the creation of a unique platform.
     */
    public static synchronized MovieDatabase getInstance() {
        return movieDatabase;
    }

    /**
     * Method used for checking if the given movie exists in the movie database.
     * @param movieName
     * @return true/false
     */
    private boolean movieExistInDatabase(final String movieName) {
        for (Movie movie: movies) {
            if (movie.getName().equals(movieName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method used for adding a movie into the database and notifying
     * the observers about this event.
     * @param addedMovie
     * @return ObjectNode
     */
    public ObjectNode addMovie(final Movie addedMovie) {
        if (movieExistInDatabase(addedMovie.getName())) {
            return actionResult(ERROR);
        }

        movies.add(addedMovie);
        notify(addedMovie);
        return null;
    }

    /**
     * Method used for deleting a movie from the database and notifying
     * the observers about this event.
     * @param movieName
     * @return ObjectNode
     */
    public ObjectNode deleteMovie(final String movieName) {
        if (!movieExistInDatabase(movieName)) {
            return actionResult(ERROR);
        }

        Movie movie = null;

        for (Movie databaseMovie: movies) {
            if (databaseMovie.getName().equals(movieName)) {
                movie = databaseMovie;
                break;
            }
        }

        movies.remove(movie);
        notify(movie);
        return null;
    }

    public static void setMovieDatabase(final MovieDatabase movieDatabase) {
        MovieDatabase.movieDatabase = movieDatabase;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(final ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public ArrayList<MovieObserver> getObservers() {
        return observers;
    }
}
