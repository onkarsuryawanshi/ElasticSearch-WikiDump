package com.ex.springdemo.wikidump;

public class Mapper {
    public static Dump map(String data) {
        String[] arr = data.split(":");

        String field1 = arr[0];
        String field2 = arr[1];
        String field3 = arr[2];
        String field4 = arr[3];
        Dump obj = new Dump(field1,field2,field3,field4);
        return obj;
    }

}
