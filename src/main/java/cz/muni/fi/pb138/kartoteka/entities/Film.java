package cz.muni.fi.pb138.kartoteka.entities;

import java.math.BigDecimal;

/**
 * Created by Peter Stanko on 5/5/15.
 *
 * @author Peter Stanko
 */
public class Film {

    private String name;
    private String director;
    private int year;
    private String description;
    private byte hodnotenie;

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte getHodnotenie() {
        return hodnotenie;
    }

    public void setHodnotenie(byte hodnotenie) {
        this.hodnotenie = hodnotenie;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
