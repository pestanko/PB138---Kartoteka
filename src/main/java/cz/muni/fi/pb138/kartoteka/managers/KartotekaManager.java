package cz.muni.fi.pb138.kartoteka.managers;

import cz.muni.fi.pb138.kartoteka.entities.Category;
import cz.muni.fi.pb138.kartoteka.exceptions.CategoryException;

import java.util.List;

/**
 * Created by Peter Stanko on 5/6/15.
 *
 * @author Peter Stanko
 */
public interface KartotekaManager {


    void addCategory(Category category) throws CategoryException;
    void deleteCategory(Category category) throws CategoryException;
    void deleteCategory(long id) throws CategoryException;

    Category getCategory(long id);
    Category getCategory(String name);
    List<Category> getCategories();






}
