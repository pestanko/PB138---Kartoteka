package cz.muni.fi.pb138.kartoteka.loaders;

import cz.muni.fi.pb138.kartoteka.exceptions.FileManagerException;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManager;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManagerImpl;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Table;

import java.io.File;

/**
 * Created by Peter Stanko on 5/6/15.
 *
 * @author Peter Stanko
 */
public class FileManagerImpl implements FileManager {


    @Override
    public KartotekaManager load(String path) throws Exception {
        KartotekaManager kartotekaManager = new KartotekaManagerImpl();


        SpreadsheetDocument document = SpreadsheetDocument.loadDocument(path);

        document.getTableList().forEach((table -> {

            System.out.println(table.getTableName());


        }));



        return kartotekaManager;
    }

    @Override
    public void save(String path, KartotekaManager manager) throws Exception {
        SpreadsheetDocument document = SpreadsheetDocument.newSpreadsheetDocument();
        document.removeSheet(0);
        manager.getCategories().forEach((category ->
        {
            document.appendSheet(category.getName());

        }));



        document.save(new File(path));

    }
}
