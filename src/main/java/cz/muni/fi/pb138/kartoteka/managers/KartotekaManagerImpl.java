package cz.muni.fi.pb138.kartoteka.managers;

import cz.muni.fi.pb138.kartoteka.entities.Category;
import cz.muni.fi.pb138.kartoteka.exceptions.CategoryException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Peter Stanko on 5/6/15.
 *
 * @author Peter Stanko
 */
public class KartotekaManagerImpl implements KartotekaManager {

    List<Category> categories = new ArrayList<>();

    @Override
    public void addCategory(Category category) throws CategoryException
    {

        if(category == null)
        {
            throw new NullPointerException("Category is null");
        }
        if(category.getId() > 0)
        {
            throw new CategoryException("Trying to add category with id " + category.getId() + " again." );
        }
        if(getCategory(category.getName()) != null)
        {
            throw new CategoryException("Category with name  "+ category.getName() + " already exists.");
        }
        category.setId(categories.size() + 1);
        categories.add(category);
    }

    @Override
    public void deleteCategory(Category category) throws CategoryException
    {
        if(category == null)
        {
            throw new NullPointerException("Category is null");
        }
        deleteCategory(category.getId());
    }



    @Override
    public void deleteCategory(long id) throws CategoryException{
        if(id <= 0) throw new CategoryException("Id is negative or 0.");
        Category cat = getCategory(id);
        if(cat == null)
        {
            throw  new CategoryException("Category with id" + id + " not exists.");
        }

        if(!cat.isEmpty())
        {
            throw new CategoryException("Category with id "+ id + " is not empty.");
        }

        categories.remove(cat);
    }

    @Override
    public Category getCategory(long id)
    {
        if(id <= 0 )
        {
            throw new IndexOutOfBoundsException("id");
        }

        final Stream<Category> categoryStream = categories.stream().filter((cat) -> cat.getId() == id);
        if(categoryStream.count() == 0)
        {
            return null;
        }
        return categoryStream.collect(Collectors.toList()).get(0);
    }

    public Category getCategory(String name) {
        if(name == null )
        {
            throw new NullPointerException("name");
        }

        final Stream<Category> categoryStream = categories.stream().filter((cat) -> name.equals(cat.getName()));
        if(categoryStream.count() == 0)
        {
            return null;
        }
        return categoryStream.collect(Collectors.toList()).get(0);

    }

    @Override
    public List<Category> getCategories() {
        return categories;
    }
}
