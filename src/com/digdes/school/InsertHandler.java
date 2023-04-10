package com.digdes.school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertHandler {

    Map<String, Object> row;
    List<Assign> listAssigns = new ArrayList<>();

    public InsertHandler(List<String> listStrings) {

        for (int i = 0; i < listStrings.size() - 1; i++) {
            this.listAssigns.add(new Assign(listStrings.get(i), listStrings.get(i + 2), listStrings.get(i + 1)));
            i = i + 3;
        }
        Map<String, Object> newRow = new HashMap<>();
        for (Assign a : listAssigns) {
            newRow.putAll(a.singleMap);
        }
//        System.out.println(row);
        this.row = newRow;
    }

    public InsertHandler(List<String> listStrings, List<Map<String, Object>> table, List<Map<String, Object>> whereTable) {
        this(listStrings);
        for (int i = 0; i < whereTable.size(); i++) {
            int finalI = i;
            row.forEach((column, value) -> {
                if (value == null) {
                    table.get(table.indexOf(whereTable.get(finalI))).remove(column);
                } else {
                    table.get(table.indexOf(whereTable.get(finalI))).put(column, value);
                }
            });
        }
    }

    public Map<String, Object> getRow() {
        return row;
    }

}