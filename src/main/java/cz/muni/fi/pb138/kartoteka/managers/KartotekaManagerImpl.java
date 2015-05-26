package cz.muni.fi.pb138.kartoteka.managers;

import cz.muni.fi.pb138.kartoteka.entities.Category;
import cz.muni.fi.pb138.kartoteka.exceptions.CategoryException;

import java.util.ArrayList;
import java.util.List;

/**
 * Kartoteka Manager implementation
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 * @version 2015-05-18
 */
public class KartotekaManagerImpl implements KartotekaManager {

    /**
     * List of all categories in
     */
    List<Category> categories = new ArrayList<>();

    /**
     * Adds {@link Category} to document
     * @param category category to be added
     * @throws CategoryException when category has wrong attributes
     */
    @Override
    public void addCategory(Category category) throws CategoryException {
        if(category == null) {
            throw new NullPointerException("Category is null");
        }
        if(category.getId() > 0) {
            throw new CategoryException("Trying to add category with id " + category.getId() + " again." );
        }
        if(getCategory(category.getName()) != null) {
            throw new CategoryException("Category with name  "+ category.getName() + " already exists.");
        }
        category.setId(categories.size() + 1);
        categories.add(category);
    }

    /**
     * Deletes the {@link Category} from document
     * @param category category to be deleted
     * @throws CategoryException when category has wrong attributes
     */
    @Override
    public void deleteCategory(Category category) throws CategoryException {
        if(category == null) {
            throw new NullPointerException("Category is null");
        }
        deleteCategory(category.getId());
    }

    /**
     * Deletes the {@link Category} from document
     * @param id category id
     * @throws CategoryException when category has wrong attributes
     */
    @Override
    public void deleteCategory(long id) throws CategoryException {
        if (id <= 0) {
            throw new CategoryException("Id is negative or 0.");
        }
        Category cat = getCategory(id);
        if (cat == null) {
            throw  new CategoryException("Category with id" + id + " not exists.");
        }

        if (!cat.isEmpty()) {
            throw new CategoryException("Category with id "+ id + " is not empty.");
        }

        categories.remove(cat);
    }

    /**
     * Getter for {@link Category}
     * @param id category id
     * @return category
     */
    @Override
    public Category getCategory(long id) {
        if (id <= 0) {
            throw new IndexOutOfBoundsException("id");
        }

        for(Category category : categories) {
            if (id == category.getId()) {
                return category;
            }
        }
        return null;
    }

    /**
     * Getter for {@link Category}
     * @param name category name
     * @return category
     */
    public Category getCategory(String name) {
        if(name == null) {
            throw new NullPointerException("name");
        }

        for(Category category : categories) {
            if (name.equals(category.getName())) {
                return category;
            }
        }
        return null;
    }

    /**
     * Getter for {@link KartotekaManagerImpl#categories}
     * @return list of categories
     */
    @Override
    public List<Category> getCategories() {
        return categories;
    }
}
