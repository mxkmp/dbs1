package models;

public class Column {
    private String name;
    private String value;
    private int sqlType;

    public Column(String name, int type) {
        this.name = name;
        this.sqlType = type;
    }

    public Column(String name, String value, int sqlType) {
        this.name = name;
        this.value = value;
        this.sqlType = sqlType;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", sqlType=" + sqlType +
                '}';
    }
}
