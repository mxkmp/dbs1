package models;

public class Column {
    private String name;
    private int sqlType;
    private int value;

    //TODO: value anpassen
    public Column(String name, int type) {
        this.name = name;
        this.sqlType = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSqlType() {
        return sqlType;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

}
