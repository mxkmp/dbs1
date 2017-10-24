package models;

import java.util.Arrays;

public class PatchObject {
    public enum statementType{
        UPDATE, DELETE, INSERT
    }

    public final static int IDX_PRIMARY = 0;
    private String tableName;
    private statementType type;
    private String[] columnNames;
    private String[] content;

    public PatchObject(String tableName) {
        this.tableName = tableName;
    }

    public PatchObject(String tableName, statementType type, String[] columnNames, String[] content) {
        this(tableName);
        this.type = type;
        this.columnNames = columnNames;
        this.content = content;
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

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public String[] getContent() {
        return content;
    }

    public void setContent(String[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PatchObject{" +
                "tableName='" + tableName + '\'' +
                ", type=" + type +
                ", columnNames=" + Arrays.toString(columnNames) +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
