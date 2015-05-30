package cz.muni.fi.pb138.kartoteka.entities;

import cz.muni.fi.pb138.kartoteka.exceptions.FilmException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Category representing class
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 * @version 2015-05-18
 */
public class Category {

    /**
     * Category name
     */
    private String name;

    /**
     * Films belonging to category
     */
    private List<Film> films = new ArrayList<>();

    /**
     * Category id
     */
    private long id;

    /**
     * Non-parametric constructor
     */
    public Category() {
        this.name = "";
        this.id = 0;
    }

    /**
     * Constructor with name
     * @param name category name
     */
    public Category(String name) {
        this.name = name;
        this.id = 0;
    }

    /**
     * Getter for {@link Category#name}
     * @return category name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for {@link Category#name}
     * @param name category name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for {@link Category#films}
     * @return List of category films
     */
    public List<Film> getFilms() {
        return films;
    }

    /**
     * Setter for {@link Category#films}
     * @param films List of films
     */
    public void setFilms(List<Film> films) {
        this.films = films;
    }

    /**
     * Adds film into {@link Category#films}
     * @param film film to be added
     * @throws FilmException when film has not all the required attributes
     */
    public void addFilm(Film film) throws FilmException {
        if(film == null) // req
        {
            throw new NullPointerException("film is null");
        }
        if(film.getId() > 0)  // req
        {
            throw new FilmException("Film with id "+ film.getId()+" already exists in database.");
        }

        if(getFilm(film.getName()) !=  null) // opt ? - Neviem -> toto robi to ze film je unikatny ? zmenime list na set ?
        {
            throw new FilmException("Film with name " + film.getName() + " already exists in database.");
        }

        film.setId(films.size() + 1);
        this.films.add(film);
    }

    /**
     * Getter for {@link Film} by name
     * @param name film name
     * @return {@link Film} if found, null otherwise
     */
    private Film getFilm(String name) {
        if(name == null) {
            throw new NullPointerException("Name is null.");
        }

        final List<Film> collect = films.stream()
                                        .filter((elem) -> name.equals(elem.getName()))
                                        .collect(Collectors.toList());
        if(collect.size() == 0) {
            return null;
        }
        return collect.get(0);

    }

    /**
     * Deletes the {@link Film} from {@link Category#films}
     * @param id film id
     * @throws FilmException when {@link Film} is not found in database
     */
    public void deleteFilm(long id) throws FilmException {
        Film film = getFilm(id);
        if(film == null) {
            throw new FilmException("Film does not exist in database.");
        }
        films.remove(film);
        film.setId(-1);
    }

    /**
     * Getter for {@link Film} by id
     * @param id film id
     * @return {@link Film} if found, null otherwise
     */
    public Film getFilm(long id) {
        if(id <= 0) {
            throw new IndexOutOfBoundsException("Id is negative or zero.");
        }

        final List<Film> collect = films.stream()
                                        .filter((elem) -> elem.getId() == id)
                                        .collect(Collectors.toList());
        if(collect.size() == 0) {
            return null;
        }
        return collect.get(0);
    }

    /**
     * Writes category into {@link String}
     * @return formatted string
     */
    @Override
    public String toString() {
        return "Category {" +
                "name ='" + name + '\'' +
                ", films =" + films.toString() +
                '}';
    }

    /**
     * Checks whether the category has no films
     * @return true if category has no films, false otherwise
     */
    public boolean isEmpty() {
        return films.isEmpty();
    }

    /**
     * Getter for {@link Category#id}
     * @return category id
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for {@link Category#id}
     * @param id category id
     */
    public void setId(long id) {
        this.id = id;
    }
}
