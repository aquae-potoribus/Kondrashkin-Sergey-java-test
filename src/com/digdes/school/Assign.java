package com.digdes.school;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Assign {
    Object tableColumn;
    String tableColumnString;
    Object SecondValue;
    String SecondValueString;
    String operator;
    Map<String, Object> singleMap = new HashMap<>();

    Map<String, String> columnTypes;


    public Assign(String tableColumn, String second, String operator) {

        columnTypes = new HashMap<>();
        columnTypes.put("id", "Long");
        columnTypes.put("lastName", "String");
        columnTypes.put("age", "Long");
        columnTypes.put("cost", "Double");
        columnTypes.put("active", "Boolean");


        this.tableColumn = tableColumn.substring(1, tableColumn.length() - 1);
        this.tableColumnString = this.tableColumn.toString();
        this.operator = operator;
        this.SecondValueString = second;

        int firstChar = second.charAt(0);
        int lastChar = second.charAt(second.length() - 1);

        if (firstChar == 8216 && lastChar == 8217) {
            SecondValue = second.substring(1, second.length() - 1);
        }

        if (Pattern.compile("\\.").matcher(second).results().count() == 1 && second.matches("[0-9\\.]+")) {
            SecondValue = Double.valueOf(second);
        }

        if (second.matches("[0-9]+")) {
            SecondValue = Long.valueOf(second);
        }

        if (second.equals("true") || second.equals("false")) {
            SecondValue = Boolean.valueOf(second);
        }

        if (second.equals("null")) {
            SecondValue = "null";
        }
        filter();

    }

    public Map<String, Object> filter() {
        String SecondValueClass = SecondValue.getClass().getSimpleName();

        if (!tableColumnString.matches("(id|lastName|age|cost|active)")) {
            throw new RuntimeException("column with name '" + tableColumnString + "' not exist ");
        }
        if (!operator.equals("=")) {
            throw new IllegalStateException("Unexpected operator: " + operator);
        }

        if (columnTypes.get(tableColumn).equals(SecondValueClass)) {

            this.singleMap.put(tableColumnString, SecondValue);

        } else if (SecondValueString.equals("null")) {
            this.singleMap.put(tableColumnString, null);
        } else {
            throw new RuntimeException(SecondValueClass + " wrong class to assign to column '" + tableColumnString + "'");
        }

        return this.singleMap;
    }
}
