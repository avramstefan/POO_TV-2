package platform;

import action.Action;
import action.ActionExec;
import action.PageHandler;
import action.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import movie.Movie;
import movie.MovieDatabase;
import movie.MovieObserver;
import pages.Page;
import pages.loggedhomepage.HomepageAuthenticated;
import pages.logout.Logout;
import pages.miscellaneous.Details;
import pages.miscellaneous.Upgrade;
import pages.moviepage.MoviesPage;
import pages.unloggedhomepage.HomepageUnauthenticated;
import pages.unloggedhomepage.Login;
import pages.unloggedhomepage.Register;
import user.User;

import java.util.ArrayList;
import java.util.HashMap;

import static action.ActionExec.recommend;
import static platform.Constants.*;

public final class Platform implements MovieObserver {
    private User loggedUser;
    private HashMap<String, Page> pages;
    private ArrayList<Movie> availableMovies;
    private String currentPage;
    private Input inputData;
    private static Platform platform = null;

    public Platform(final Input inputData) {
        this.loggedUser = null;
        this.currentPage = HOMEPAGE_UNAUTHENTICATED;
        this.inputData = inputData;
        this.availableMovies = inputData.getMovies();
    }

    /**
     * SINGLETON method to assure the creation of a unique platform, used in Main function
     * with Input parameter for initializing data in constructor.
     * @param inputData variable containing a reference to Input class
     */
    public static synchronized Platform getInstance(final Input inputData) {
        if (platform == null) {
            platform = new Platform(inputData);
            MovieDatabase movieDatabase = MovieDatabase.getInstance(inputData);
            PageHandler pageHandler = PageHandler.getInstance(inputData);
        }
        return platform;
    }

    /**
     * SINGLETON method to assure the creation of a unique platform.
     */
    public static synchronized Platform getInstance() {
        return platform;
    }

    /**
     * Function used for running each action in part and adding their results into the ArrayNode.
     * @param output ArrayNode of ObjectNodes which will be displayed in the JSON output.
     */
    public void runActions(final ArrayNode output) {
        initializePages();
        initializeUsers();
        initializeMovies();

        ActionExec.setPlatform();
        Utils.setPlatform();

        for (Action action : inputData.getActions()) {
            action.setInputData(inputData);

            ObjectNode actionObj = action.run();

//            if (actionObj == null) {
//                ObjectNode actionObj2 = (new ObjectMapper()).createObjectNode();
//                actionObj2.put("type", action.getType());
//                actionObj2.put("feature", action.getFeature());
//                output.add(actionObj2);
//            }

            if (actionObj != null) {
//                if (action.getType().equals("back")) {
//                    actionObj.put("currentPage", platform.getCurrentPage());
//                }
//                actionObj.put("type", action.getType());
//                actionObj.put("feature", action.getFeature());
//                actionObj.put("page", action.getPage());
//                actionObj.put("movie", action.getMovie());
                output.add(actionObj);
            }
        }

        if (platform.getLoggedUser() != null
                && platform.getLoggedUser().getCredentials().getAccountType().equals("premium")) {
            output.add(recommend());
        }
    }

    @Override
    public void update(Movie movie) {
        setUserAvailableMovies();
    }

    /**
     * Function used for initializing pages' instances.
     */
    private void initializePages() {
        pages = new HashMap<>();
        pages.put(HOMEPAGE_UNAUTHENTICATED, new HomepageUnauthenticated());
        pages.put(HOMEPAGE_AUTHENTICATED, new HomepageAuthenticated());
        pages.put(LOGIN, new Login());
        pages.put(REGISTER, new Register());
        pages.put(MOVIES, new MoviesPage());
        pages.put(DETAILS, new Details());
        pages.put(UPGRADES, new Upgrade());
        pages.put(LOGOUT, new Logout());
    }

    /**
     * Initializes users' extra data.
     */
    private void initializeUsers() {
        for (User user : inputData.getUsers()) {
            user.initializeExtraData();
        }
    }

    /**
     * Initializes movies' extra data.
     */
    private void initializeMovies() {
        for (Movie movie : MovieDatabase.getInstance().getMovies()) {
            movie.initializeExtraData();
        }
    }

    /**
     * Removes the movies that are banned in the current user's country.
     */
    public void removeBannedMovies() {
        availableMovies.removeIf(movie -> movie.getCountriesBanned().
                contains(loggedUser.getCredentials().getCountry()));
    }

    /**
     * Setting the user's available movies.
     */
    public void setUserAvailableMovies() {
        setAvailableMovies(MovieDatabase.getInstance().getMovies());
        removeBannedMovies();
    }

    public static void setPlatform(final Platform platform) {
        Platform.platform = platform;
    }

    public ArrayList<Movie> getAvailableMovies() {
        return availableMovies;
    }

    public void setAvailableMovies(final ArrayList<Movie> availableMovies) {
        this.availableMovies = new ArrayList<>(availableMovies);
    }

    /**
     * Setting the available movies variables to be an ArrayList of just one movie.
     * Usually used on changing the page to "see details".
     * @param movieName a string containing the movie name
     */
    public void setAvailableMovies(final String movieName) {
        ArrayList<Movie> singleMovieArray = new ArrayList<>();
        singleMovieArray.add(getMovieByName(movieName));
        this.availableMovies = singleMovieArray;
    }

    /**
     * @param movieName a string containing the movie name
     * @return the movie instance with the given name
     */
    public Movie getMovieByName(final String movieName) {
        Movie retMovie = null;

        for (Movie movie : platform.getAvailableMovies()) {
            if (movie.getName().equals(movieName)) {
                retMovie = movie;
            }
        }

        return  retMovie;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(final User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(final String currentPage) {
        this.currentPage = currentPage;
    }

    public HashMap<String, Page> getPages() {
        return pages;
    }

    public void setPages(final HashMap<String, Page> pages) {
        this.pages = pages;
    }

    public Input getInputData() {
        return inputData;
    }

    public void setInputData(final Input inputData) {
        this.inputData = inputData;
    }
}
