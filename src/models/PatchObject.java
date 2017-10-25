package models;

import java.util.Arrays;

public class PatchObject {
    public enum statementType{
        UPDATE, DELETE, INSERT
    }

    private String tableName;
    private statementType type;
    private Column[] column;
    private String[] values;

    public PatchObject(String tableName) {
        this.tableName = tableName;
    }

    public PatchObject(String tableName, statementType type, Column[] column, String[] values) {
        this(tableName);
        this.type = type;
        this.column = column;
        this.values = values;
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

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "PatchObject{" +
                "tableName='" + tableName + '\'' +
                ", type=" + type +
                ", column=" + Arrays.toString(column) +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
