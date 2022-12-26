package action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import input.Input;
import user.Credentials;

public final class Action {
    private String type;
    private String page;
    private String feature;
    private String movie;
    private Credentials credentials;
    private String startsWith;
    private int count;
    private Filter filters;
    private int rate;
    private Input inputData;

    public Action() {

    }

    /**
     * Function used for running the current action by triggering different functions.
     * @return ObjectNode output
     */
    public ObjectNode run() {
        if (type.equals("change page")) {
            return Commands.changePage(inputData, this);
        }

        return switch (feature) {
            case "login" -> Commands.login(inputData, this);
            case "register" -> Commands.register(inputData, this);
            case "search" -> Commands.search(this);
            case "filter" -> Commands.filter(this);
            case "buy tokens" -> Commands.buyTokens(this);
            case "buy premium account" -> Commands.buyPremiumAccount();
            case "purchase" -> Commands.purchase();
            case "watch" -> Commands.watch();
            case "like", "rate" -> Commands.likeOrRate(this);
            default -> null;
        };
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
