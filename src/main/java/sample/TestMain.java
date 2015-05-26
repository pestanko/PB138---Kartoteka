package sample;

import cz.muni.fi.pb138.kartoteka.entities.Category;
import cz.muni.fi.pb138.kartoteka.exceptions.CategoryException;
import cz.muni.fi.pb138.kartoteka.loaders.FileManager;
import cz.muni.fi.pb138.kartoteka.loaders.FileManagerImpl;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManager;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates empty test document
 *
 * @author Peter Stanko
 */
@Deprecated
public class TestMain {

    final static Logger logger = LoggerFactory.getLogger(TestMain.class);

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
            logger.error("Category exception.",e);

        } catch (Exception e) {
            logger.error("Exception",e);
        }
    }
}
