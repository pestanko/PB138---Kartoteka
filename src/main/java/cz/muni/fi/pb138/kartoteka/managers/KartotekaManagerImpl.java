package cz.muni.fi.pb138.kartoteka.managers;

import cz.muni.fi.pb138.kartoteka.entities.Category;
import cz.muni.fi.pb138.kartoteka.exceptions.CategoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Kartoteka Manager implementation
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 * @version 2015-06-06
 */
public class KartotekaManagerImpl implements KartotekaManager {

    /**
     * List of all categories
     */
    private final List<Category> categories = new ArrayList<>();

    /**
     * Class logger
     */
    private final static Logger logger = LoggerFactory.getLogger(KartotekaManagerImpl.class);

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

        logger.info("Added new category: " + category);
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
        logger.info("Deleting category with id: " + id);

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
                logger.info("Returning category: "+ category);
                return category;
            }
        }
        logger.info("No category for id: " + id);
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
                logger.info("Returning category: "+ category);
                return category;
            }
        }
        logger.info("No category for name: " + name);
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

    /**
     * Checks whether category with given name already exists
     * @param name name of category
     * @return true if category exists, false otherwise
     */
    @Override
    public boolean containsCategory(String name) {
        name = name.toLowerCase();
        for (Category category : categories) {
            if(category.getName().toLowerCase().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
