**Name: Avram Cristian-Stefan**\
**Group: 321 CA**

# <span style="color:#009999">POO-TV</span>

Implemented a simple backend for TV shows platform. The motivation behind this project
consists in correctly using **design patterns** and logical manufacturing of a real site's
fundamentals. Apps take in input from users and output information in various ways.
The project handler of IO consists in managing **JSON** files. The parameters resulted in
the output files should be taken in consideration for modifying the page's appearance
through user's commands and will.

In order to present the implementation, there is a need of summarizing the process.
The following picture shows off how the platform was deployed, by categorizing
the main parts of the implementation into different classes.

<img src=FlowChart.png middle alt="summary" width="704" height="837">

# <span style="color:#009999">Input</span>
The input is being parsed using **Jackson** as in the following example:
```java
    ObjectMapper objMapper = new ObjectMapper();
    Input inputData = objMapper.readValue(new File(inputPath), Input.class);
        ...
    ObjectWriter objectWriter = objMapper.writerWithDefaultPrettyPrinter();
    objectWriter.writeValue(new File(outputDirPath), output);
```
This way of transforming **JSON** into an integrated part of code is possible by naming
the class' variables the same way the fields from the input files are named. This process
is called *serializing* and uses different objects (*ArrayNode*, *ObjectNode*, *ObjectMapper* etc).

Talking about *Input* class, there are not so many things to say about, though it is
used by almost every command to obtain essential data. It contains three *ArrayList*,
one for movies, one for users and the last one for actions.

# <span style="color:#009999">Users</span>
This class materializes the concept of *user*, while its instances are individual representations
of each user in part. It contains information about its purchased, watched, liked and rated
movies, about the number of tokens and the number of free premium movies left (in case of premium
account). It also contains a reference to *Credentials* class, which is used to store
data about user's name, password, account type, country and balance.

There is only one user connected on the platform for the moment, and it can be found by using
the *ArrayList* that the *Input* provides.

# <span style="color:#009999">Movies</span>
*Movies* class represents the background of every existing movie in the system. It contains
information about the movie name, its year, duration, genres, actors, banned countries, ratings,
number of likes, number of ratings and the actual rating score. The *rating score* is calculated
by summing all the ratings and dividing the sum by the number of ratings.

As in the *user* case, a movie may be accessed by using *Input* *ArrayList* of movies.

# <span style="color:#009999">Pages</span>
There are more pages used by the platform. As the pages have a lot of things in common, they
should better be implemented as *children* of a *Page* parent class. Here is where design
patterns intervene under the representation of <span style="color:#999900">**FACTORY PATTERN**</span>.

The parent class, called *Page*, is actually **an abstract** class, and it has abstract
methods, that are actually implemented by its children and normal final methods that are
common for each *child* class in part.

There are more types of pages (Register, Login, Homepage un/authenticated, Upgrades, Details,
Movies and Logout), but, as a simple example, the *MoviesPage* was used in the following lines.

```java
/*
        FACTORY PATTERN
 */
public abstract class Page {
    protected ArrayList<String> possiblePages;
    ...

    protected abstract void initializePossiblePages();
    ...
}

public final class MoviesPage extends Page {

    public MoviesPage() {
        initializePossiblePages();
    }

    protected void initializePossiblePages() {
        possiblePages = new ArrayList<>();
        possiblePages.add("homepage autentificat");
        possiblePages.add("see details");
        possiblePages.add("logout");
        possiblePages.add("movies");
    }
}
```

# <span style="color:#009999">Platform</span>
The *Platform* class offers data about the current logged user, keeps track of the pages,
of the available movies, the current displayed page and has a reference to the *Input* data.
As there is a *unique* platform, the process will need just one instance of this class, so
<span style="color:#999900">**SINGLETON PATTERN**</span>. will be used. This allows the program to creat a single instance for
the entire process and every interaction with this class will come to be an interaction
with the instantiated *platform* object.
```java
/*
        SINGLETON PATTERN
 */
public static synchronized Platform getInstance(final Input inputData,
                                                final int created) {
        if (platform == null || created == 0) {
            platform = new Platform(inputData);
        }
        return platform;
    }
```
Also, there is a method,```public void runActions(final ArrayNode output)```, where every action
is processed and started, and every output is handled.

# <span style="color:#009999">Action</span>
This class is by far the most important one, as it weaves the parts of the process through
the platform and its parameters. Every action has its own characteristics, including its
type and other different things from case to case (feature, movie, page, filters etc.).

Depending on these characteristics, different functions will be triggered, functions 
from *Commands* class. But, before talking about what every function does, let's explain
how *Filter* class has been implemented.

## <span style="color:#009999">Filter</span>
The movies that appear on a page may be filtered using this class. This "filtering"
only consists of setting platform's available movies by multiple clauses. There are 2 types
of filters, *sorting filters* and *container filters*. As the filter class would not have
any sense without these two specific types of filters and vice-versa, I appealed to creating
<span style="color:#999900">**INNER CLASSES**</span>.
```java
public final class Filter {
    private Container contains;
    private SortClass sort;

    static class Container {
        private ArrayList<String> genres;
        private ArrayList<String> actors;
        ...
    }

    static class SortClass {
        private String rating;
        private String duration;
        ...
    }
    ...
}
```

## <span style="color:#009999">Commands</span>
* <span style="color:#ff704d">"*change page*"</span> -> ```public static ObjectNode changePage(final Input inputData, final Action action)```
    * checks if the platform may change from current page to the given page using
  *canChangePage(page)* method.
    * if the current page is "*details*", then the function checks if the movie exists
  in the currently available movies from *platform*. If the operation ends with
  success, then the *platform's* available movies will be an *ArrayList* with just
  one movie, the one whose details are presented.
    * if the given page is "*logout*", then the *platform's* logged user is set as null
  and the current page will become "*homepage unauthenticated*".
    * otherwise, the *platform's* current page becomes the given page and the *platform's*
  available movies are somehow filtered depending on the user's country and the movies'
  banned countries.
* <span style="color:#ff704d">"*login*""</span> -> ```public static ObjectNode login(final Input inputData, final Action action)```
  * checks if the current page is "*login*".
  * as the *Input* keeps track of every user in the system by using
  an *ArrayList* of users, the user with the given credentials will
  be searched through this array and, if found, its index position
  will be returned (**-1** for error).
  * if success, the "*platform's*" logged user will be the given user
  and the current page will become "*homepage authenticated*".
* <span style="color:#ff704d">"*register*"</span> -> ```public static ObjectNode register(final Input inputData, final Action action)```
  * works exactly like "*login*", but the user is now created and checked if
  already exists in the system. If not, then it is added into the *ArrayList*
  of users.
* <span style="color:#ff704d">"*search*"</span> -> ```public static ObjectNode search(final Action action)```
  * checks if the current page is "*movies".
  * iterates through every movie from "*platform's*" *ArrayList* of movies
  and keeps just the ones that start with the given string.
* <span style="color:#ff704d">"*filter*"</span> -> ```public static ObjectNode filter(final Action action)```
  * checks if the current page is "*movies*".
  * filters the movies from "*platform's*" available movies *ArrayList*.
  * uses a helper function from *Utils* -> ```public static void filterMovies(final ArrayList<Movie> movies, final Filter filters)```,
  where the movies are filtered by the given conditions through *Filter*.
  * the sorting process was not implemented with *Comparator* or *Comparable*,
  but the immanent sorting method of *ArrayLists* was used, given sorting
  functions found in the same class.
* <span style="color:#ff704d">"*buy tokens*"</span> -> ```public static ObjectNode buyTokens(final Action action)```
  * checks if the current page is "*upgrades*".
  * if the balance is big enough, that the number of tokens that is given
  through the *action* parameter will be bought.
* <span style="color:#ff704d">"*buy premium account*"</span> -> ```public static ObjectNode buyPremiumAccount()```
  * checks if the current page is "*upgrades*".
  * if the user has enough tokens (10 tokens), then the premium account
  will be bought.
  * if the user's account type was already premium, or he does not have
  enough tokens, then there will be an error.
* <span style="color:#ff704d">"*purchase*"</span> -> ```public static ObjectNode purchase()```
  * checks if the current page is "*details*".
  * depends on user's account type
    * if *standard* and has more than 2 tokens, then it will be a successful
    operation
    * if *premium* and has premium movies left to take, then the *numFreePremiumMovies*
    will get decremented, otherwise the user will need to pay tokens just like
    the standard user.
  * if everything is alright, then the purchased movie will be added to the user's
  purchased movies.
* <span style="color:#ff704d">"*watch*"</span> -> ```public static ObjectNode watch()```
  * checks if the current page is "*details*" and the movie has been purchased.
  * if everything is ok, then the movie will be added to the user's collection
  of watched movies.
* <span style="color:#ff704d">"*like*" or "*rate*"</span> -> ```public static ObjectNode likeOrRate(final Action action)```
  * checks if the current page is "*details*" and if the movie has been watched.
    * if "like" is demanded (```public static ObjectNode like(final User user, final Movie movie)```, then the movies is added to the user's liked movies
    and the number of likes that this movie has gets incremented.
    * if "rate" (```public static ObjectNode like(final User user, final Movie movie)```), then checks if the rate is *[ 0, 5 ]* interval and adds
    it into the user's rated movies and the movie's rating is recalculated.
  
# <span style="color:#009999">Feedback</span>
I enjoyed implementing this platform, while I've got a better understanding of how
*internal classes* work and why should I use *Singleton*. It's nice to see how
things behind our favorite TV Shows platform do work.

What I would've done different?
I could've use a HashMap for keeping < *name*, *password* > pairs for users, like
a small database.

Personally, looking over the comments from the project's forum, I was satisfied with
the answers and the project statements and requirements were explained very well. Good idea
with structuring the page hierarchy as a directory structure. I didn't like the fact that
I had to discover by myself that, when successfully finished "purchase", "watch", "like" and
"rate", then we have to give output. From this perspective, things could've gone better, but,
overall it was a nice task, which allows us to use our OOP knowledge in a creative way!

Kudos to the team!