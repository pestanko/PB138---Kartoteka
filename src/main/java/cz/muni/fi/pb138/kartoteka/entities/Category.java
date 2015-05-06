package cz.muni.fi.pb138.kartoteka.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Stanko on 5/6/15.
 *
 * @author Peter Stanko
 */
public class Category {


    String name;
    List<Film> film = new ArrayList<>();

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Film> getFilm()
    {
        return film;
    }

    public void setFilm(List<Film> film) {
        this.film = film;
    }


    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", film=" + film.toString() +
                '}';
    }
}
