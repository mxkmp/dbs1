package main;

import org.dbunit.DBTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;

@Ignore
public class DatabaseTest extends DBTestCase {

    private String file1;
    private String file2;
    private IDatabaseConnection databaseConnection;
    private IDataSet dataSet;
    private IDataSet dataSetExpected;

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new FileInputStream("full.xml"));
    }

    @Before
    public void setUp() throws Exception {
        //create inputfiles
        file1 = "t:Books\n" +
                "i:3-238-21751-0|Test1|Test|Test|7.90|1987\n" +
                "i:3-238-21752-0|Test Titel|Vorname|Nachname|1.11|2000\n" +
                "d:3-237-21755-2|Test3|Brian|Moore|7.90|1987\n" +
                "u:3-257-21755-2|Weißrock|Brian|Moore|11.90|1987\n";

        file2 = "t:Zinsen\n" +
                "i:12345|Neuer Zinssatz|0.75|2014-02-01\n" +
                "u:11111|Zins Extra-Konto|0.5|2014-02-01\n" +
                "d:11112|Zins Extra-Konto|0.5|2014-02-01\n";

        //create database
        databaseConnection = new DatabaseConnection(new DBConnection().getConnection());

        dataSet = getDataSet();
        dataSetExpected = new FlatXmlDataSetBuilder().build(new FileInputStream("expected.xml"));


        DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet); //Eventuell auf Refresh ändern

    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    public void testBooks() throws Exception {
        /*TemplateConverter cnv = new TemplateConverter(file1, databaseConnection.getConnection());
        cnv.writeToDatabase();

        //Prüfen ob richtig
        ITable actualTable = dataSet.getTable("BOOKS");
        ITable expectedTable = dataSetExpected.getTable("BOOKS");

        //Schreibt es nicht in die Testdatenbank sondern in die richtige

        assertEquals("Schreiben des Templates nicht erfolgreich.", actualTable, expectedTable);
        */
    }


}