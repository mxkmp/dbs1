import main.DBConnection;
import main.TemplateConverter;
import models.PatchObject;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

public class TemplateConverterTest {

    String fileContent = null;
    String[] statements;

    @Test
    public void Test1() throws Exception {
        //Arrange
        fileContent = "t:Books\n" +
                "i:3-238-21751-0|Test1|Brian|Moore|7.90|1987\n" +
                "i:3-238-21752-0|Test Titel|Vorname|Nachname|1.11|2000\n" +
                "d:3-240-21755-2|Test3|Brian|Moore|7.90|1987\n" +
                "u:3-239-21755-2|Test100|Brian|Moore|12.90|1987\n";

        String table = "Books";
        statements = new String[4];
        statements[0] = String.format("INSERT INTO %s (%s)values(%s)", table,
                "isbn,title,fname,author,price,year_published",
                "'3-238-21751-0','Test1','Brian','Moore','7.90',1987");
        statements[1] = String.format("INSERT INTO %s (%s)values(%s)", table,
                "isbn,title,fname,author,price,year_published",
                "'3-238-21752-0','Test Titel','Vorname','Nachname','1.11',2000");
        statements[2] = String.format("DELETE FROM %s WHERE %s", table, "isbn='3-240-21755-2'");
        statements[3] = String.format("UPDATE %s SET %s WHERE %s", table,
                "title='Test100',fname='Brian',author='Moore',price='12.90',year_published=1987",
                "isbn='3-239-21755-2'");

        //act
        TemplateConverter converter = new TemplateConverter(fileContent);
        converter.setCon(new DBConnection().getConnection());
        List<PatchObject> list = converter.convertTemplate();
        converter.generateStatements(list);

        //Assert
        int i = 0;
        for (PatchObject b : list) {
            assertEquals(b.getStmnt().toString(), statements[i]);
            i++;
        }
    }

    @Test
    public void Test2() throws Exception {
        fileContent = "t:Zinsen\n" +
                "i:12345|Neuer Zinssatz|0.75|2014-02-01\n" +
                "u:12231|Zins Extra-Konto|0.5|2014-02-01\n" +
                "d:11111|Zins Plus-Konto|1.25|2013-01-01\n";

        String table = "Zinsen";
        statements = new String[3];
        statements[0] = String.format("INSERT INTO %s (%s)values(%s)", table,
                "id,description,amount,date",
                "12345,'Neuer Zinssatz','0.75','2014-02-01 00:00:00+01'");
        statements[1] = String.format("UPDATE %s SET %s WHERE %s", table,
                "description='Zins Extra-Konto',amount='0.5',date='2014-02-01 00:00:00+01'",
                "id=12231");
        statements[2] = String.format("DELETE FROM %s WHERE %s", table,
                "id=11111");

        TemplateConverter converter = new TemplateConverter(fileContent);
        converter.setCon(new DBConnection().getConnection());
        List<PatchObject> list = converter.convertTemplate();
        converter.generateStatements(list);

        int i = 0;
        for (PatchObject b : list) {
            assertEquals(b.getStmnt().toString(), statements[i]);
            i++;
        }
    }

}