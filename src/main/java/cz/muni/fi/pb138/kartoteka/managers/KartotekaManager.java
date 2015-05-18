package cz.muni.fi.pb138.kartoteka.managers;

import cz.muni.fi.pb138.kartoteka.entities.Category;
import cz.muni.fi.pb138.kartoteka.exceptions.CategoryException;

import java.util.List;

/**
 * Kartoteka Manager interface
 *
 * @author Peter Stanko
 */
public interface KartotekaManager {

    /**
     * Adds category to document
     * @param category category to be added
     * @throws CategoryException
     */
    void addCategory(Category category) throws CategoryException;

    /**
     * Deletes the category from document
     * @param category category to be deleted
     * @throws CategoryException
     */
    void deleteCategory(Category category) throws CategoryException;

    /**
     * Deletes the category from document
     * @param id category id
     * @throws CategoryException
     */
    void deleteCategory(long id) throws CategoryException;

    /**
     * Getter for category
     * @param id category id
     * @return category
     */
    Category getCategory(long id);

    /**
     * Getter for category
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
