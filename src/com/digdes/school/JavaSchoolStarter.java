package com.digdes.school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaSchoolStarter {

    List<Map<String, Object>> table = new ArrayList<>();

    public JavaSchoolStarter() {
    }

    public List<Map<String, Object>> execute(String sqlString) throws Exception {

        sqlString = normaliseSpaces(sqlString);
        sqlString = lowingCase(sqlString);
        List<String> listStrings = List.of(sqlString.split("(?!\\B‘[^’]*)\\s+(?![^‘]*’\\B)"));
        int lastValueComma = findLastComma(listStrings);
        switch (listStrings.get(0)) {
            case ("select") -> {
                if (listStrings.size() == 1) {
                } else if (listStrings.get(1).equals("where")) {
                    return new WhereHandler(listStrings.subList(2, listStrings.size()), table).getTable();
                }
            }
            case ("insert") -> {
                Map<String, Object> row = new HashMap<>();
                if (listStrings.get(1).equals("values")) {
                    row = new InsertHandler(listStrings.subList(2, listStrings.size())).getRow();
                    table.add(row);
                }
                List<Map<String, Object>> newTable = new ArrayList<>();
                newTable.add(row);
                return newTable;

            }
            case ("update") -> {
                List<Map<String, Object>> whereTable;
                if (listStrings.contains("where")) {
                    whereTable = new WhereHandler(listStrings.subList(listStrings.indexOf("where") + 1, listStrings.size()), table).getTable();
                } else {
                    whereTable = table;
                }

                if (listStrings.get(1).equals("values")) {
                     new InsertHandler(listStrings.subList(2, lastValueComma + 4), table, whereTable);
                }
                checkEmptyRow(table);
                return whereTable;
            }
            case ("delete") -> {
                if (listStrings.contains("where")) {
                    List<Map<String, Object>> whereList = new WhereHandler(listStrings.subList(listStrings.indexOf("where") + 1, listStrings.size()), table).getTable();
                    for (Map<String, Object> row : whereList) {
                        table.remove(row);
                    }
                    return whereList;
                } else {
                    List<Map<String, Object>> bufferTable = table;
                    table = new ArrayList<>();
                    return bufferTable;
                }
            }
        }
        return table;
    }

    public static String normaliseSpaces(String input) {
        String output = input.trim().replaceAll("(?!\\B‘[^’]*)(=|!=|>=|<=|<|>|,)(?![^‘]*’\\B)", " $1 ").replaceAll("(?!\\B‘[^’]*)\\s+(?![^‘]*’\\B)", " ");
        return output;
    }

    public static String lowingCase(String input) {
        char[] chars = input.toCharArray();

        boolean insideQuotes = false;
        for (int i = 0; i < chars.length; i++) {
            if ((int) chars[i] == 8216 || (int) chars[i] == 8217) {
                insideQuotes = !insideQuotes;
            } else if (!insideQuotes) {
                chars[i] = Character.toLowerCase(chars[i]);
            }
        }
        return new String(chars);
    }

    public static int findLastComma(List<String> stringList) {
        int commaIndex = stringList.indexOf("values");
        for (int i = 0; i < stringList.size(); i++) {
            if (stringList.get(i).equals(",")) {
                commaIndex = i;
            }
        }
        return commaIndex;
    }

    public static void checkEmptyRow(List<Map<String, Object>> table) {
        table.removeIf(Map::isEmpty);
    }
}
