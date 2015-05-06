package cz.muni.fi.pb138.kartoteka.entities;

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
    private byte rating;
    private long id;

    public Film(String name, String director, int year, String description, byte rating) {
        this.name = name;
        this.director = director;
        this.year = year;
        this.description = description;
        this.rating = rating;
    }

    public Film(String name) {
        this.name = name;
    }

    public Film() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public byte getRating() {
        return rating;
    }

    public void setRating(byte rating) {
        this.rating = rating;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Film{" +
                "name='" + name + '\'' +
                ", director='" + director + '\'' +
                ", year=" + year +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                ", id=" + id +
                '}';
    }
}
