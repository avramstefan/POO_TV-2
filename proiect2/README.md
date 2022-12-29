**Name: Avram Cristian-Stefan**\
**Group: 321 CA**

# <span style="color:#009999">POO-TV_2</span>

The basic functionalities that this platform is displaying
are presented in *old / README.md*. As this project's stage
is focused on correctly implementing **design patterns**, by virtue
of *Object-Oriented Programming*, I will present the steps that
were taken in consideration in the process of development. For
the beginning, I will present every design pattern that was
used.

## <span style="color:#009999">Observer Pattern</span>
**Observer** is a behavioral design pattern that allows notifying
multiple objects about an event that happen to the subject.
Using this definition, I implemented the *subscribing / notifying* system in an
efficient way. <img src=Observer.png align ="right" alt="Observer" width="300" height="320">
When triggering an action like *database add* or *database delete*, the
**Movie Database**, which happen to be our subject, suffers modifications.
So, the observers, *users* and the *platform*, are going to be notified
about this change that happened in the **Movie Database**. The users are notified
if the added movie contains a genre to which the user is subscribed or if
the deleted movie is one of the user's purchased movies. When talking about
the movie platform, its available movies are updated, movies that may be
displayed at output or may be seen by the user. This fact is helpful because
the **Movie Database** contains all the movies, while the platform's *available
movies* contain only the logged user's valid movies.

## <span style="color:#009999">Command Pattern</span>
**Command** is a behavioral design pattern that contains all information about
the request and supports undoable operations. With that being said, our platform
allows navigating through its pages forward and backward.
<img src=Command.png align ="left" alt="Observer" width="310" height="350">
If there is an action of changing page, the **PageHandler** is triggering *execute()*.
The execution modifies the current page that our platform is displaying, so the
receiver of the operation is going to be the platform. Every changing page action
is stored in a **linked list** in *PageHandler*. The last action from the linked list
is actually storing the action that brought us on the current page.
So, when the *back* action comes into consideration, the platform will change its 
current page into the page of the second last action from the linked list.
The *undo()* operation is quite simple, as the linked list stores objects of class
*Action*. So, going backward to a previous page is equivalent to changing the page again.
So, from the *undo()* function, the *execute()* function will be called, but with the
previous action as a parameter.

## <span style="color:#009999">Strategy Pattern</span>
**Strategy** is a behavioral design pattern which define a family of algorithms and put each
other into separate classes, called strategies. I implemented the action parser using this design
<img src=Strategy.png align ="right" alt="Observer" width="310" height="350">
pattern, with every action that is possible representing a different strategy.
So, when an action is given and triggered using *action.run()*, the *Action* object does two
essential things. Firstly, it creates and sets the action strategy that is needed, depending
on the type of the action and, eventually, on its feature. After this step, the action executes
its chosen strategy. So, for example, if the action is of type *login*, the program will create
a new object of class *LoginAction*, which overrides the *executeAction(input, action)* function.
After that, it is logging the user on the platform and returns the output. This process is
available for every instance of action strategy.

## <span style="color:#009999">Factory Pattern</span>
There are more pages used by the platform. As the pages have a lot of things in common, they
should better be implemented as *children* of a *Page* parent class. Here is where design
patterns intervene under the representation of **Factory Pattern**.

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


## <span style="color:#009999">Singleton Pattern</span>
The *Platform* class offers data about the current logged user, keeps track of the pages,
of the available movies, the current displayed page and has a reference to the *Input* data.
As there is a *unique* platform, the process will need just one instance of this class, so
**Singleton Pattern**. will be used. This allows the program to creat a single instance for
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
The **Movie Database** was also implemented as a *Singleton*, because the entire platform
has just one database of movies, and it is easier to handle it this way.

### <span style="color:#009999">Recommendation concept</span>
This **recommendation** algorithm works by sorting the user's favourite genres and movies. The 
*recommend()* function is triggered at the end of all actions, in the platform class.

For the genres sorting, there is used a *HashMap* that stores and updates the number of likes
for each genre. Lately, the *key* values will become the *ArrayList* of the best genres and the
*values* of the dictionary will come to be an *ArrayList* representing the number of likes
for each genre. So, on the position *x*, there will be the genre in the *ArrayList* of genres
and its correspondent number of likes in the *ArrayList* of *numLikes*.

As for the movies sorting, they are stored in an *ArrayList*, being sorted in a descending
way by their total number of likes.

## <span style="color:#009999">Feedback</span>
It was nice to work on this project overall. I feel like it was not a burden, but a challenge to
put our minds to work. The concept of a TV-Show Platform is very permissive, and it allows us
to think and create the best way of implementing the requirements.

I've used a total of 5 design patterns and this homework was probably the best way to learn
about their capabilities and importance. I like how we had the chance of implementing
these design patterns very easily according to the requirements.

What I did not like was the wavering state of this project's requirements, as there were
some difficulties in understanding the demanding. I've come to the need of changing some
functions after they were done, because some changes intervened on the forum and on the tests.

But the negative part is not so important as the fact that I've enjoyed working on this project.
So, Kudos to the team! You did a great job.
