package models;

import java.sql.PreparedStatement;
import java.util.Arrays;

public class PatchObject {
    public enum statementType{
        UPDATE, DELETE, INSERT
    }

    private static final int IDX_PRIMARY = 0;

    private String tableName;
    private statementType type;
    private Column[] column;
    //TODO: 3 statements global definieren
    private PreparedStatement stmnt;

    public PatchObject(String tableName) {
        this.tableName = tableName;
    }

    public PatchObject(String tableName, statementType type, Column[] column) {
        this(tableName);
        this.type = type;
        this.column = column;
    }

    public Column getPrimaryKeyColumn() {
        return this.column[IDX_PRIMARY];
    }

    public String getPrimaryKeyValue() {
        return this.column[IDX_PRIMARY].getValue();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public statementType getType() {
        return type;
    }

    public void setType(statementType type) {
        this.type = type;
    }

    public Column[] getColumn() {
        return column;
    }

    public void setColumn(Column[] column) {
        this.column = column;
    }

    public PreparedStatement getStmnt() {
        return stmnt;
    }

    public void setStmnt(PreparedStatement stmnt) {
        this.stmnt = stmnt;
    }

    @Override
    public String toString() {
        return "PatchObject{" +
                "tableName='" + tableName + '\'' +
                ", type=" + type +
                ", column=" + Arrays.toString(column) +
                ", stmnt=" + stmnt +
                '}';
    }
}
