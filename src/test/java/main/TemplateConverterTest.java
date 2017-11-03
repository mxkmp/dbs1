package main;

import models.PatchObject;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class TemplateConverterTest {

    Connection con = null;
    String fileContent = null;
    String[] statements;
    PreparedStatement[] pStatement;

    @Before
    public void setup() {
        con = new DBConnection().getConnection();
    }

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
        pStatement = new PreparedStatement[4];


        statements[0] = String.format("INSERT INTO %s (%s)values(%s)", table,
                "isbn,title,fname,author,price,year_published",
                "'3-238-21751-0','Test1','Brian','Moore','7.90','1987'");

        pStatement[0] = con.prepareStatement(String.format("INSERT INTO %s (%s)values(?,?,?,?,?,?)", table,
                "isbn,title,fname,author,price,year_published"));
        pStatement[0].setString(1, "3-238-21751-0");
        pStatement[0].setString(2, "Test1");
        pStatement[0].setString(3, "Brian");
        pStatement[0].setString(4, "Moore");
        pStatement[0].setObject(5, "7.90", Types.NUMERIC);
        pStatement[0].setInt(6, 1987);

        statements[1] = String.format("INSERT INTO %s (%s)values(%s)", table,
                "isbn,title,fname,author,price,year_published",
                "'3-238-21752-0','Test Titel','Vorname','Nachname','1.11','2000'");

        pStatement[1] = con.prepareStatement(String.format("INSERT INTO %s (%s)values(?,?,?,?,?,?)", table,
                "isbn,title,fname,author,price,year_published"));
        pStatement[1].setString(1, "3-238-21752-0");
        pStatement[1].setString(2, "Test Titel");
        pStatement[1].setString(3, "Vorname");
        pStatement[1].setString(4, "Nachname");
        pStatement[1].setObject(5, "1.11", Types.NUMERIC);
        pStatement[1].setInt(6, 2000);


        statements[2] = String.format("DELETE FROM %s WHERE %s", table, "isbn='3-240-21755-2'");

        pStatement[2] = con.prepareStatement(String.format("DELETE FROM %s WHERE isbn=?", table));
        pStatement[2].setString(1, "3-240-21755-2");

        statements[3] = String.format("UPDATE %s SET %s WHERE %s", table,
                "title='Test100',fname='Brian',author='Moore',price='12.90',year_published='1987'",
                "isbn='3-239-21755-2'");
        pStatement[3] = con.prepareStatement(String.format("UPDATE %s SET title=?,fname=?,author=?,price=?,year_published=? WHERE isbn=?", table));

        pStatement[3].setString(1, "Test100");
        pStatement[3].setString(2, "Brian");
        pStatement[3].setString(3, "Moore");
        pStatement[3].setObject(4, "12.90", Types.NUMERIC);
        pStatement[3].setInt(5, 1987);
        pStatement[3].setString(6, "3-239-21755-2");

        //act
        TemplateConverter converter = new TemplateConverter(fileContent, con);
        List<PatchObject> list = converter.convertTemplate();
        converter.generateStatements(list);

        //Assert
        int i = 0;
        for (PatchObject b : list) {
            assertEquals(b.getStmnt().toString(), statements[i]);
            assertEquals(b.getStmnt().toString(), pStatement[i].toString());
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
                "'12345','Neuer Zinssatz','0.75','2014-02-01 00:00:00.000000 +01:00:00'");
        statements[1] = String.format("UPDATE %s SET %s WHERE %s", table,
                "description='Zins Extra-Konto',amount='0.5',date='2014-02-01 00:00:00.000000 +01:00:00'",
                "id='12231'");
        statements[2] = String.format("DELETE FROM %s WHERE %s", table,
                "id='11111'");

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