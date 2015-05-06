package cz.muni.fi.pb138.kartoteka.loaders;

import cz.muni.fi.pb138.kartoteka.exceptions.FileManagerException;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManager;


/**
 * Created by Peter Stanko on 5/6/15.
 *
 * @author Peter Stanko
 */
public interface FileManager {
    KartotekaManager load(String path) throws Exception;
    void save(String path, KartotekaManager manager) throws Exception;

}
