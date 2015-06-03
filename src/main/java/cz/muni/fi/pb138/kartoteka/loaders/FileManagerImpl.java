package cz.muni.fi.pb138.kartoteka.loaders;

import cz.muni.fi.pb138.kartoteka.entities.Category;
import cz.muni.fi.pb138.kartoteka.entities.Film;
import cz.muni.fi.pb138.kartoteka.exceptions.CategoryException;
import cz.muni.fi.pb138.kartoteka.exceptions.FilmException;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManager;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManagerImpl;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * File manager implementation
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 * @version 2015-05-26
 */
public class FileManagerImpl implements FileManager {

    public static final int FILM_ID = 0;
    public static final int FILM_NAME = 1;
    public static final int FILM_YEAR = 2;
    public static final int FILM_RATING = 3;
    public static final int FILM_DIRECTOR = 4;
    public static final int FILM_DESCRIPTION = 5;

    /**
     * Class logger
     */
    final static Logger logger = LoggerFactory.getLogger(FileManagerImpl.class);

    /**
     * Loads the document from file into {@link KartotekaManager}
     * @param path file path
     * @return new {@link KartotekaManager} with loaded Document
     * @throws Exception when load operation fails
     */
    @Override
    public KartotekaManager load(String path) throws Exception {
        KartotekaManager kartotekaManager = new KartotekaManagerImpl();

        SpreadsheetDocument document = SpreadsheetDocument.loadDocument(path);

        document.getTableList().forEach((table -> {

            Category cat = new Category(table.getTableName());
            logger.info("Loading category: " + cat);
            try {
                kartotekaManager.addCategory(cat);
                logger.info("Adding category to memory: "+ cat);
            } catch (CategoryException e) {
                logger.error("Load", e);
            }

            try {
                loadAllFilms(table, cat);
            } catch (FilmException e) {
                logger.error("Load", e);

            }

        }));

        return kartotekaManager;
    }

    /**
     * Loads all films from {@link Table} to {@link Category}
     * @param table table
     * @param cat category
     * @throws FilmException when adding film to category failed
     */
    private void loadAllFilms(Table table, Category cat) throws FilmException {

        for(int i = 1; i < table.getRowCount(); i++)
        {
            Film film = new Film();


            try {

                String name = getNameCell(table, i).getDisplayText();
                if (name == null || name.length() == 0) continue;
                film.setName(name);

                String year = getYearCell(table, i).getDisplayText();
                film.setYear(year);

                String rating = getRatingCell(table, i).getDisplayText();
                film.setRating(rating);

                String description = getDescriptionCell(table, i).getDisplayText();
                film.setDescription(description);

                String author = getDirectorCell(table, i).getDisplayText();
                film.setDirector(author);

                cat.addFilm(film);
                logger.info("Adding film to memory: "+ film);
            } catch (NullPointerException ex) {
                logger.warn("Unsupported format:", ex);
                throw  new UnsupportedOperationException("Unsupported format exception!", ex);
            }
        }

    }

    /**
     * Saves the document from {@link KartotekaManager} into file
     * @param path file path
     * @param manager {@link KartotekaManager} instance
     * @throws Exception when save operation fails
     */
    @Override
    public void save(String path, KartotekaManager manager) throws Exception {
        SpreadsheetDocument document = SpreadsheetDocument.newSpreadsheetDocument();
        document.removeSheet(0);
        manager.getCategories().forEach((category ->
        {
            Table actualTable =  document.appendSheet(category.getName());

            printHeaders(actualTable);
            
            printFilms(actualTable, category.getFilms());
            logger.info("Saving category: "+ category);

        }));

        document.save(new File(path));
        logger.info("Saving document.");
    }

    /**
     * Prints all films to table
     * @param actualTable table
     * @param films films
     */
    private void printFilms(Table actualTable, List<Film> films) {
        int row = 1;

        for(Film film : films) {
            getIdCell(actualTable, row).setDisplayText("" + film.getId());
            getNameCell(actualTable, row).setDisplayText(film.getName());
            getYearCell(actualTable, row).setDisplayText("" + film.getYear());
            getRatingCell(actualTable, row).setDisplayText("" + film.getRating());
            getDirectorCell(actualTable, row).setDisplayText(film.getDirector());
            getDescriptionCell(actualTable, row).setDisplayText(film.getDescription());

            row++;

            logger.info("Saving film: "+ film );
        }
    }

    /**
     * Getter for name cell
     * @param table table
     * @param row row index
     * @return found cell
     */
    private Cell getNameCell(Table table,int row){
        return table.getCellByPosition(FILM_NAME, row);
    }

    /**
     * Getter for year cell
     * @param table table
     * @param row row index
     * @return found cell
     */
    private Cell getYearCell(Table table,int row){
        return table.getCellByPosition(FILM_YEAR, row);
    }

    /**
     * Getter for rating cell
     * @param table table
     * @param row row index
     * @return found cell
     */
    private Cell getRatingCell(Table table, int row){
        return table.getCellByPosition(FILM_RATING, row);
    }

    /**
     * Getter for director cell
     * @param table table
     * @param row row index
     * @return found cell
     */
    private Cell getDirectorCell(Table table,int row) {
        return table.getCellByPosition(FILM_DIRECTOR, row);
    }

    /**
     * Getter for description cell
     * @param table table
     * @param row row index
     * @return found cell
     */
    private Cell getDescriptionCell(Table table,int row){
        return table.getCellByPosition(FILM_DESCRIPTION, row);
    }

    /**
     * Getter for id cell
     * @param table table
     * @param row row index
     * @return found cell
     */
    private Cell getIdCell(Table table, int row){
        return table.getCellByPosition(FILM_ID, row);
    }

    /**
     * Writes all headers into table
     * @param actualTable table
     */
    private void printHeaders(Table actualTable) {
        final int ROW = 0;

        getNameCell(actualTable, ROW)
                .setDisplayText("Film name");

        getYearCell(actualTable, ROW).setDisplayText("Year");

        getRatingCell(actualTable, ROW).setDisplayText("Rating");

        getDirectorCell(actualTable, ROW).setDisplayText("Director");

        getDescriptionCell(actualTable, ROW).setDisplayText("Description");

    }
}
