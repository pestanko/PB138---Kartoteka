package cz.muni.fi.pb138.kartoteka.loaders;

import cz.muni.fi.pb138.kartoteka.managers.KartotekaManager;

/**
 * File manager interface
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 */
public interface FileManager {
    /**
     * Loads the document from file into {@link KartotekaManager}
     * @param path file path
     * @return new {@link KartotekaManager} with loaded Document
     * @throws Exception when load operation fails
     */
    KartotekaManager load(String path) throws Exception;

    /**
     * Saves the document from {@link KartotekaManager} into file
     * @param path file path
     * @param manager {@link KartotekaManager} instance
     * @throws Exception when save operation fails
     */
    void save(String path, KartotekaManager manager) throws Exception;
}
