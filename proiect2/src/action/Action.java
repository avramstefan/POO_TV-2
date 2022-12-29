package action;

import action.actions.ActionStrategy;
import action.actions.BackAction;
import action.actions.BuyPremiumAccountAction;
import action.actions.BuyTokensAction;
import action.actions.ChangePageAction;
import action.actions.DatabaseAction;
import action.actions.FilterAction;
import action.actions.LikeRateAction;
import action.actions.LoginAction;
import action.actions.PurchaseAction;
import action.actions.RegisterAction;
import action.actions.SearchAction;
import action.actions.SubscribeAction;
import action.actions.WatchAction;
import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import movie.Movie;
import user.Credentials;

public final class Action {
    private String type;
    private String page;
    private String feature;
    private String movie;
    private String subscribedGenre;
    private Credentials credentials;
    private String startsWith;
    private int count;
    private Filter filters;
    private int rate;
    private Input inputData;
    private Movie addedMovie;
    private String deletedMovie;
    private ActionStrategy actionStrategy;

    public Action() {

    }

    /**
     * Function used for running the current action by triggering different functions.
     * @return ObjectNode output
     */
    public ObjectNode run() {
        setActionStrategy();
        return actionStrategy.executeAction(inputData, this);
    }

    /**
     * Setting the correspondent strategy for the action to run.
     */
    void setActionStrategy() {

        switch (type) {
            case "change page" -> actionStrategy = new ChangePageAction();
            case "subscribe" -> actionStrategy = new SubscribeAction();
            case "database" -> actionStrategy = new DatabaseAction();
            case "back" -> actionStrategy = new BackAction();
            default -> {
            }
        }

        if (feature == null) {
            return;
        }

        switch (feature) {
            case "login" -> actionStrategy = new LoginAction();
            case "register" -> actionStrategy = new RegisterAction();
            case "search" -> actionStrategy = new SearchAction();
            case "filter" -> actionStrategy = new FilterAction();
            case "buy tokens" -> actionStrategy = new BuyTokensAction();
            case "buy premium account" -> actionStrategy = new BuyPremiumAccountAction();
            case "purchase" -> actionStrategy = new PurchaseAction();
            case "watch" -> actionStrategy = new WatchAction();
            case "like", "rate" -> actionStrategy = new LikeRateAction();
            default -> {
            }
        }
    }

    public Movie getAddedMovie() {
        return addedMovie;
    }

    public void setAddedMovie(final Movie addedMovie) {
        this.addedMovie = addedMovie;
    }

    public String getSubscribedGenre() {
        return subscribedGenre;
    }

    public void setSubscribedGenre(final String subscribedGenre) {
        this.subscribedGenre = subscribedGenre;
    }

    public String getDeletedMovie() {
        return deletedMovie;
    }

    public void setDeletedMovie(final String deletedMovie) {
        this.deletedMovie = deletedMovie;
    }

    public Input getInputData() {
        return inputData;
    }

    public void setInputData(final Input inputData) {
        this.inputData = inputData;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(final int rate) {
        this.rate = rate;
    }

    public Filter getFilters() {
        return filters;
    }

    public void setFilters(final Filter filters) {
        this.filters = filters;
    }

    public int getCount() {
        return count;
    }

    public void setCount(final int count) {
        this.count = count;
    }

    public String getStartsWith() {
        return startsWith;
    }

    public void setStartsWith(final String startsWith) {
        this.startsWith = startsWith;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getPage() {
        return page;
    }

    public void setPage(final String page) {
        this.page = page;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(final String feature) {
        this.feature = feature;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(final Credentials credentials) {
        this.credentials = credentials;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(final String movie) {
        this.movie = movie;
    }
}
