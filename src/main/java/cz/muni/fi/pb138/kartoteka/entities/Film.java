package cz.muni.fi.pb138.kartoteka.entities;

/**
 * Film representing class
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 * @version 2015-05-18
 */
public class Film {

    /**
     * Film name
     */
    private String name;

    /**
     * Film director
     */
    private String director;

    /**
     * Film year
     */
    private String year;

    /**
     * Film description
     */
    private String description;

    /**
     * Film rating
     */
    private String rating;

    /**
     * Film id
     */
    private long id;

    /**
     * Constructor with required fields
     * @param name film name
     * @param director film director
     * @param year film year
     * @param description film description
     * @param rating film rating
     */
    public Film(String name, String director, String year, String description, String rating) {
        this.name = name;
        this.director = director;
        this.year = year;
        this.description = description;
        this.rating = rating;
    }

    /**
     * Constructor with name
     * @param name film name
     */
    public Film(String name) {
        this.name = name;
    }

    /**
     * Non-parametric constructor
     */
    public Film() {
    }

    /**
     * Getter for {@link Film#id}
     * @return film id
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for {@link Film#id}
     * @param id film id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for {@link Film#director}
     * @return film director
     */
    public String getDirector() {
        return director;
    }

    /**
     * Setter for {@link Film#director}
     * @param director film director
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * Getter for {@link Film#year}
     * @return film year
     */
    public String getYear() {
        return year;
    }

    /**
     * Setter for {@link Film#year}
     * @param year film year
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * Getter for {@link Film#description}
     * @return film description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for {@link Film#description}
     * @param description film description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for {@link Film#rating}
     * @return film rating
     */
    public String getRating() {
        return rating;
    }

    /**
     * Setter for {@link Film#rating}
     * @param rating film rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * Getter for {@link Film#name}
     * @return film name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for {@link Film#name}
     * @param name film name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Writes info about {@link Film} into String
     * @return formatted string
     */
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
