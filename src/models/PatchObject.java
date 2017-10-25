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
    private String[] values;
    private PreparedStatement stmnt;

    public PatchObject(String tableName) {
        this.tableName = tableName;
    }

    public PatchObject(String tableName, statementType type, Column[] column, String[] values) {
        this(tableName);
        this.type = type;
        this.column = column;
        this.values = values;
    }

    public Column getPrimaryKeyColumn() {
        return this.column[IDX_PRIMARY];
    }

    public String getPrimaryKeyValue() {
        return this.values[IDX_PRIMARY];
    }

    public String getTableName() {
        return tableName;
    }

    public statementType getType() {
        return type;
    }

    public Column[] getColumn() {
        return column;
    }

    public String[] getValues() {
        return values;
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
                ", values=" + Arrays.toString(values) +
                ", stmnt=" + stmnt +
                '}';
    }
}
