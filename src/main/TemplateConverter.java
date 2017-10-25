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
    private String fileContent;
    private Connection con = null;
    private List<PatchObject> patchList = null;

    public TemplateConverter(String fileContent) {
        this.fileContent = fileContent;
    }

    /**
     * @throws IllegalArgumentException
     */
    public List<PatchObject> convertTemplate() throws IllegalArgumentException{

        this.con = new DBConnection().getConnection();

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

                    try {
                        PreparedStatement stmt = con.prepareStatement(String.format("SELECT * FROM %s LIMIT 1", tableName));
                        ResultSet rs = stmt.executeQuery();

                        int columnCount = rs.getMetaData().getColumnCount();
                        columns = new Column[columnCount];

                        for (int n = 0; n < columns.length; n++) {
                            columns[n] = new Column(rs.getMetaData().getColumnName(n+1), rs.getMetaData().getColumnType(n+1));
                        }
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
        return this.patchList;
    }

    public void generateStatements(){
        if(this.patchList == null || this.patchList.isEmpty()){
            try{
                convertTemplate();
            }catch(IllegalArgumentException e){
                e.printStackTrace();
            }
        }

        for(PatchObject patch : this.patchList){
            switch(patch.getType()){
                case INSERT:
                    System.out.println(Arrays.toString(patch.getColumn()));

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

                    String stmnt = String.format("INSERT INTO %s (%s)values(%s)", patch.getTableName(), columns, values);
                    System.out.println(stmnt);
                    try {
                        PreparedStatement pstmn = con.prepareStatement(stmnt);
                        for(int i = 0; i < patch.getColumn().length; i++){
                            pstmn.setObject(i+1, patch.getValues()[i], patch.getColumn()[i].getSqlType());
                        }

                        pstmn.executeQuery();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                    break;
                case UPDATE:
                    break;
                case DELETE:
                    break;
                default:
                    break;
            }
        }
    }

    public List<PatchObject> getPatchList() {
        return patchList;
    }

}

