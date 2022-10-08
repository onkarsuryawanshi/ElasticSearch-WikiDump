package com.ex.springdemo.wikidump;

public class Dump {
    private String field1;
    private String field2;
    private String field3;

    private String field4;

    public Dump() {}

    public Dump(String field1, String field2, String field3, String field4) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.field4 = field4;
    }

    public String getField1() {
        return field1;
    }

    public String getField2() {
        return field2;
    }

    public String getField3() {
        return field3;
    }


    public String getField4() {
        return field4;
    }
    @Override
    public String toString() {
        return "Pojo{" +
                "field1=" + field1 +
                ", field2=" + field2 +
                ", field3='" + field3 +
                ",field4="+field4+
                '\'' +
                '}';
    }
}
