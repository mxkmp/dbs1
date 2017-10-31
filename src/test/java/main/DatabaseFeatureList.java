package main;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

public class DatabaseFeatureList {
    private static boolean setupFlag = false;
    private String file;
    private Connection con;

    @Before
    public void setup() throws Throwable {
        con = new DBConnection().getConnection();

        if (setupFlag) return;
        Statement stm = con.createStatement();
        stm.executeUpdate("DELETE FROM test_zinsen;");
        setupFlag = true;
    }

    @Given("^Template$")
    public void template(String arg0) throws Throwable {
        file = arg0;
    }

    @When("^Template is executed$")
    public void templateIsExecuted() throws Throwable {
        TemplateConverter cvn = new TemplateConverter(file);
        cvn.writeToDatabase();

    }

    @Then("^table \"([^\"]*)\" should have (\\d+) entries$")
    public void tableShouldHaveEntries(String arg0, int arg1) throws Throwable {
        PreparedStatement preparedStatement = con.prepareStatement(String.format("SELECT COUNT(*) from %s", arg0));
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            assertEquals(rs.getInt(1), arg1);
        }
    }

    @Then("^the entry is deleted$")
    public void theEntryIsDeleted() throws Throwable {
        PreparedStatement preparedStatement = con.prepareStatement("SELECT * from test_zinsen WHERE id = 2");
        ResultSet rs = preparedStatement.executeQuery();

        int count = 0;
        while (rs.next()) {
            count++;
        }

        assertEquals(0, count);

    }

    @Then("^Exception will be fired and nothing will be written, count of the database is still (\\d+)$")
    public void exceptionWillBeFiredAndNothingWillBeWrittenCountOfTheDatabaseIsStill(int arg0) throws Throwable {
        PreparedStatement preparedStatement = con.prepareStatement("SELECT COUNT(*) from test_zinsen");
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            assertEquals(rs.getInt(1), arg0);
        }
    }


    @Then("^Amount from Entry id = (\\d+) is now (\\d+)$")
    public void amountFromEntryIdIsNow(int arg0, int arg1) throws Throwable {
        PreparedStatement preparedStatement = con.prepareStatement(String.format("SELECT amount FROM test_zinsen WHERE id = %d", arg0));
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            assertEquals(rs.getInt(1), arg1);
        }
    }
}
