package cz.muni.fi.pb138.kartoteka.loaders;

import cz.muni.fi.pb138.kartoteka.entities.Category;
import cz.muni.fi.pb138.kartoteka.entities.Film;
import cz.muni.fi.pb138.kartoteka.exceptions.CategoryException;
import cz.muni.fi.pb138.kartoteka.exceptions.FileManagerException;
import cz.muni.fi.pb138.kartoteka.exceptions.FilmException;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManager;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManagerImpl;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Table;

import java.io.File;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Peter Stanko on 5/6/15.
 *
 * @author Peter Stanko
 */
public class FileManagerImpl implements FileManager {

    public final int FILM_NAME = 1;
    public final int FILM_YEAR = 2;
    public final int FILM_RATING = 3;
    public final int FILM_DIECTOR = 4;
    public final int FILM_DESCRIPTION = 5;
    public final int FILM_ID = 0;


    @Override
    public KartotekaManager load(String path) throws Exception {
        KartotekaManager kartotekaManager = new KartotekaManagerImpl();


        SpreadsheetDocument document = SpreadsheetDocument.loadDocument(path);

        document.getTableList().forEach((table -> {

            Category cat = new Category(table.getTableName());
            try {
                kartotekaManager.addCategory(cat);
            } catch (CategoryException e) {
                e.printStackTrace();
            }

            try {
                loadAllFilms(table, cat);
            } catch (FilmException e) {
                e.printStackTrace();
            }

        }));



        return kartotekaManager;
    }

    private void loadAllFilms(Table table, Category cat) throws FilmException {

        for(int i = 1; i < table.getRowCount(); i++)
        {
            Film film = new Film();


            String name = getNameCell(table, i).getDisplayText();
            if(name == null || name.length() == 0) continue;
            film.setName(name);

            String year = getYearCell(table, i).getDisplayText();
            film.setYear(Integer.parseInt(year));

            String rating = getRatingCell(table, i).getDisplayText();
            film.setRating(Byte.parseByte(rating));

            String description = getDescriptionCell(table, i).getDisplayText();
            film.setDescription(description);

            String author = getDirectorCell(table, i).getDisplayText();
            film.setDescription(author);

            cat.addFilm(film);


        }


    }

    @Override
    public void save(String path, KartotekaManager manager) throws Exception {
        SpreadsheetDocument document = SpreadsheetDocument.newSpreadsheetDocument();
        document.removeSheet(0);
        manager.getCategories().forEach((category ->
        {
            Table actualTable =  document.appendSheet(category.getName());

            printHeaders(actualTable);
            
            printFilms(actualTable, category.getFilms());


        }));

        document.save(new File(path));
    }

    private void printFilms(Table actualTable, List<Film> films) {

        int row = 1;

        for(Film film : films)
        {
            getIdCell(actualTable, row).setDisplayText("" +film.getId());
            getNameCell(actualTable, row).setDisplayText(film.getName());
            getYearCell(actualTable, row).setDisplayText("" + film.getYear());
            getRatingCell(actualTable, row).setDisplayText(""+film.getRating());
            getDirectorCell(actualTable, row).setDisplayText(film.getDirector());
            getDescriptionCell(actualTable, row).setDisplayText(film.getDescription());


            row++;
        }
    }


    private Cell getNameCell(Table table,int row){
        return table.getCellByPosition(FILM_NAME, row);
    }

    private Cell getYearCell(Table table,int row){
        return table.getCellByPosition(FILM_YEAR, row);
    }

    private Cell getRatingCell(Table table, int row){
        return table.getCellByPosition(FILM_RATING, row);
    }
    private Cell getDirectorCell(Table table,int row){
        return table.getCellByPosition(FILM_DIECTOR, row);
    }
    private Cell getDescriptionCell(Table table,int row){
        return table.getCellByPosition(FILM_DESCRIPTION, row);
    }

    private Cell getIdCell(Table table, int row){
        return table.getCellByPosition(FILM_ID, row);
    }

    private void printHeaders(Table actualTable)
    {
        final int ROW = 0;

        getNameCell(actualTable, ROW)
                .setDisplayText("Film name");


        getYearCell(actualTable,ROW).setDisplayText("Year");

        getRatingCell(actualTable, ROW).setDisplayText("Rating");

        getDirectorCell(actualTable,ROW).setDisplayText("Director");

        getDescriptionCell(actualTable, ROW).setDisplayText("Description");


    }
}
