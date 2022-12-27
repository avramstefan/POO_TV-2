package movie;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import platform.Platform;

import java.util.ArrayList;

import static action.Utils.actionResult;
import static platform.Constants.ERROR;

public class MovieDatabase {
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

    private void notify(Movie addedMovie) {
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

    private boolean movieExistInDatabase(Movie x) {
        for (Movie movie: movies) {
            if (movie.getName().equals(x.getName())) {
                return true;
            }
        }
        return false;
    }

    public ObjectNode addMovie(Movie addedMovie) {
        if (movieExistInDatabase(addedMovie))
            return actionResult(ERROR);

        movies.add(addedMovie);
        notify(addedMovie);
        return null;
    }

    public static void setMovieDatabase(MovieDatabase movieDatabase) {
        MovieDatabase.movieDatabase = movieDatabase;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public ArrayList<MovieObserver> getObservers() {
        return observers;
    }
}
