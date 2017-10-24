package main;

import models.PatchObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TemplateConverter {
    private String fileContent;
    private Connection con = null;
    private List<PatchObject> patchList = null;

    public TemplateConverter(String fileContent) {
        this.fileContent = fileContent;
        this.con = new DBConnection().getConnection();
    }

    /**
     * @throws IllegalArgumentException
     */
    public void convertTemplate() throws IllegalArgumentException{

        if (con == null) throw new IllegalArgumentException("No database connection");

        patchList = new ArrayList();
        String[] lines = fileContent.split("\n");
        String tableName = null;
        String[] columns = null;


        for(int i = 0; i < lines.length; i++){
            String[] singleLine = lines[i].split(":");
            String[] values = singleLine[1].split("|");

            //TODO: fixxen

            System.out.println(Arrays.toString(values));
            switch (singleLine[0]){
                case "t":
                    tableName = singleLine[1];

                    try {
                        PreparedStatement stmt = con.prepareStatement(String.format("SELECT * FROM %s LIMIT 1", tableName));
                        ResultSet rs = stmt.executeQuery();

                        int columnCount = rs.getMetaData().getColumnCount();
                        columns = new String[columnCount];

                        for (int n = 1; n < columnCount + 1; n++) {
                            columns[n - 1] = rs.getMetaData().getColumnLabel(n);
                            System.out.println(columns[n - 1]);
                        }

                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "i":
                    patchList.add(new PatchObject(tableName, PatchObject.statementType.INSERT, columns, values));
                    break;
                case "u":
                    patchList.add(new PatchObject(tableName, PatchObject.statementType.UPDATE, columns, values));
                    break;
                case "d":
                    patchList.add(new PatchObject(tableName, PatchObject.statementType.DELETE, columns, values));
                    break;
                default:
                    throw new IllegalArgumentException("Unkown option: '" + singleLine[0] + "'");
            }
        }
    }

    public List<PatchObject> getPatchList() {
        return patchList;
    }

}

