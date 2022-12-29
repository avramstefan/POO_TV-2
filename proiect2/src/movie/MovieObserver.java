package movie;

public interface MovieObserver {
    /**
     * Updating the observer's characteristics according to
     * the subject's transformation by the latest event.
     * @param movie
     */
    void update(Movie movie);
}
