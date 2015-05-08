package cz.muni.fi.pb138.kartoteka.entities;

import cz.muni.fi.pb138.kartoteka.exceptions.FilmException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Peter Stanko on 5/6/15.
 *
 * @author Peter Stanko
 */
public class Category {

    private String name;
    private List<Film> films = new ArrayList<>();
    private long id;

    public Category(String name) {
        this.name = name;
        this.id = 0;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Film> getFilms()
    {
        return films;
    }

    public void setFilms(List<Film> film) {
        this.films = film;
    }

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

    public void deleteFilm(long id) throws FilmException
    {
        Film film = getFilm(id);
        if(film == null) {
            throw new FilmException("Film does not exist in database.");
        }
        films.remove(film);
    }



    public Film getFilm(long id) throws FilmException {
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


    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", film=" + films.toString() +
                '}';
    }

    public boolean isEmpty() {
        return films.isEmpty();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
