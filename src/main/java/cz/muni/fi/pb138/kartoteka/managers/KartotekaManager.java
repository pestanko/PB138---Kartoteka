package cz.muni.fi.pb138.kartoteka.managers;

import cz.muni.fi.pb138.kartoteka.entities.Category;
import cz.muni.fi.pb138.kartoteka.exceptions.CategoryException;

import java.util.List;

/**
 * Kartoteka Manager interface
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 * @version 2015-05-18
 */
public interface KartotekaManager {

    /**
     * Adds {@link Category} to document
     * @param category category to be added
     * @throws CategoryException when category has wrong attributes
     */
    void addCategory(Category category) throws CategoryException;

    /**
     * Deletes the {@link Category} from document
     * @param category category to be deleted
     * @throws CategoryException when category has wrong attributes
     */
    void deleteCategory(Category category) throws CategoryException;

    /**
     * Deletes the {@link Category} from document
     * @param id category id
     * @throws CategoryException when category has wrong attributes
     */
    void deleteCategory(long id) throws CategoryException;

    /**
     * Getter for {@link Category}
     * @param id category id
     * @return category
     */
    Category getCategory(long id);

    /**
     * Getter for {@link Category}
     * @param name category name
     * @return category
     */
    Category getCategory(String name);

    /**
     * Getter for all categories
     * @return list of categories
     */
    List<Category> getCategories();
}
