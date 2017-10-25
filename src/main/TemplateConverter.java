package main;

import models.Column;
import models.PatchObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class TemplateConverter {
    private String fileContent = null;
    private List<PatchObject> patchList = null;
    private Connection con = null;

    public TemplateConverter(String fileContent) {
        this.fileContent = fileContent;
    }


    public void start() {
        this.con = new DBConnection().getConnection();

        try {
            con.setAutoCommit(false);

            this.patchList = convertTemplate();
            generateStatements(this.patchList);

            for (PatchObject b : this.patchList) {
                b.getStmnt().execute();
            }

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * @return
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    public List<PatchObject> convertTemplate() throws SQLException, IllegalArgumentException {
        if (con == null) throw new IllegalArgumentException("No database connection");

        patchList = new ArrayList();
        String[] lines = fileContent.split("\n");
        String tableName = null;
        Column[] columns = null;


        for(int i = 0; i < lines.length; i++){
            String[] singleLine = lines[i].split(":");
            String[] values = singleLine[1].split(Pattern.quote("|"));

            switch (singleLine[0]){
                case "t":
                    tableName = singleLine[1];
                    PreparedStatement stmt = con.prepareStatement(String.format("SELECT * FROM %s LIMIT 1", tableName));
                    ResultSet rs = stmt.executeQuery();

                    int columnCount = rs.getMetaData().getColumnCount();
                    columns = new Column[columnCount];

                    for (int n = 0; n < columns.length; n++) {
                        columns[n] = new Column(rs.getMetaData().getColumnName(n + 1), "", rs.getMetaData().getColumnType(n + 1));
                    }
                    break;
                case "i":
                    patchList.add(new PatchObject(tableName, PatchObject.statementType.INSERT, storeValues(columns, values)));
                    break;
                case "u":
                    patchList.add(new PatchObject(tableName, PatchObject.statementType.UPDATE, storeValues(columns, values)));
                    break;
                case "d":
                    patchList.add(new PatchObject(tableName, PatchObject.statementType.DELETE, storeValues(columns, values)));
                    break;
                default:
                    throw new IllegalArgumentException("Unkown option: '" + singleLine[0] + "'");
            }
        }
        return this.patchList;
    }

    private Column[] storeValues(Column[] column, String[] values) {
        Column[] columns = new Column[column.length];
        for (int i = 0; i < column.length; i++) {
            columns[i] = new Column(column[i].getName(), values[i], column[i].getSqlType());
        }
        return columns;
    }

    /**
     * generates the database statements.
     */
    public void generateStatements(List<PatchObject> patchList) {
        int error = 0;

        if (patchList == null || patchList.isEmpty()) {
            return;
        }

        loop:
        for (PatchObject patch : this.patchList) {
            String stmnt = "";
            PreparedStatement pstmn = null;
            switch(patch.getType()){
                case INSERT:
                    //Format columns and values to a String
                    String columns = "";
                    String values = "";
                    for(int i = 0; i < patch.getColumn().length; i++){
                        if(i != 0) {
                            columns += ",";
                            values += ",";
                        }
                        columns += patch.getColumn()[i].getName();
                        values += "?";
                    }

                    stmnt = String.format("INSERT INTO %s (%s)values(%s);", patch.getTableName(), columns, values);

                    try {
                        pstmn = con.prepareStatement(stmnt);
                        for(int i = 0; i < patch.getColumn().length; i++){
                            pstmn.setObject(i + 1, patch.getColumn()[i].getValue(), patch.getColumn()[i].getSqlType());
                        }
                        patch.setStmnt(pstmn);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        error = 1;
                        break loop;
                    }

                    break;
                case UPDATE:
                    String updateValues = "";
                    String primaryKey = String.format("%s=?", patch.getPrimaryKeyColumn().getName());
                    for (int i = 1; i < patch.getColumn().length; i++) {
                        if (i != 1) {
                            updateValues += ",";
                        }
                        updateValues += String.format("%s=?", patch.getColumn()[i].getName());
                    }

                    stmnt = String.format("UPDATE %s SET %s WHERE %s;", patch.getTableName(), updateValues, primaryKey);

                    try {
                        pstmn = con.prepareStatement(stmnt);
                        for (int i = 1; i < patch.getColumn().length; i++) {
                            pstmn.setObject(i, patch.getColumn()[i].getValue(), patch.getColumn()[i].getSqlType());
                        }
                        pstmn.setObject(patch.getColumn().length, patch.getPrimaryKeyValue(), patch.getPrimaryKeyColumn().getSqlType());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        error = 1;
                        break loop;
                    }

                    patch.setStmnt(pstmn);
                    break;
                case DELETE:
                    String deleteValue = String.format("%s=?", patch.getPrimaryKeyColumn().getName());
                    stmnt = String.format("DELETE FROM %s WHERE %s", patch.getTableName(), deleteValue);

                    try {
                        pstmn = con.prepareStatement(stmnt);
                        pstmn.setObject(1, patch.getPrimaryKeyValue(), patch.getPrimaryKeyColumn().getSqlType());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        error = 1;
                        break loop;
                    }
                    patch.setStmnt(pstmn);
                    break;
                default:
                    System.err.println("Error");
                    error = 1;
                    break;
            }
        }

        if (error == 1) {
            this.patchList = null;
            System.err.println("Error while creating patchlist");
        }

    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public List<PatchObject> getPatchList() {
        return patchList;
    }

}

