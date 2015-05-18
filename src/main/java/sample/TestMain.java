package sample;

import cz.muni.fi.pb138.kartoteka.entities.Category;
import cz.muni.fi.pb138.kartoteka.exceptions.CategoryException;
import cz.muni.fi.pb138.kartoteka.loaders.FileManager;
import cz.muni.fi.pb138.kartoteka.loaders.FileManagerImpl;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManager;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManagerImpl;

/**
 * Creates empty test document
 *
 * @author Peter Stanko
 */
public class TestMain {

    public static void main(String[] args) {
        KartotekaManager manager = new KartotekaManagerImpl();

        try {
            manager.addCategory(new Category("Filmy"));
            manager.addCategory(new Category("Audio"));
            manager.addCategory(new Category("Super filmy"));

            FileManager fm = new FileManagerImpl();
            fm.save("dokument.ods", manager);

            KartotekaManager kart = fm.load("dokument.ods");

        } catch (CategoryException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
