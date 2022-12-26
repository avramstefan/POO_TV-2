package action;

import java.util.ArrayList;

public final class Filter {
    private Container contains;
    private SortClass sort;

    static class Container {
        private ArrayList<String> genres;
        private ArrayList<String> actors;

        Container() {

        }

        public ArrayList<String> getGenres() {
            return genres;
        }

        public void setGenre(final ArrayList<String> genre) {
            this.genres = genre;
        }

        public ArrayList<String> getActors() {
            return actors;
        }

        public void setActors(final ArrayList<String> actors) {
            this.actors = actors;
        }
    }

    static class SortClass {
        private String rating;
        private String duration;

        SortClass() {

        }

        public String getRating() {
            return rating;
        }

        public void setRating(final String rating) {
            this.rating = rating;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(final String duration) {
            this.duration = duration;
        }
    }


    public Filter() {

    }

    public Container getContains() {
        return contains;
    }

    public void setContains(final Container contains) {
        this.contains = contains;
    }

    public SortClass getSort() {
        return sort;
    }

    public void setSort(final SortClass sort) {
        this.sort = sort;
    }
}
