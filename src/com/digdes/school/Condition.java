package com.digdes.school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Condition {
    Object tableColumn;
    String tableColumnString;
    Object SecondValue;
    String operator;
    List<Map<String, Object>> data;

    Map<String, String> columnTypes;

    public Condition(String tableColumn, String second, String operator, List<Map<String, Object>> data) {

        columnTypes = new HashMap<>();
        columnTypes.put("id", "Long");
        columnTypes.put("lastName", "String");
        columnTypes.put("age", "Long");
        columnTypes.put("cost", "Double");
        columnTypes.put("active", "Boolean");

        this.tableColumn = tableColumn.substring(1, tableColumn.length() - 1);
        this.tableColumnString = this.tableColumn.toString();
        this.operator = operator;

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
        this.data = filter(data);
    }

    public List<Map<String, Object>> filter(List<Map<String, Object>> base) {
        List<Map<String, Object>> newlist = new ArrayList<>();
        if (SecondValue == null) {
            if (operator.equals("=")) {
                newlist = base.stream().filter((a) -> (a.get(tableColumn)) == null).collect(Collectors.toList());
            } else if (operator.equals("!=")) {
                newlist = base.stream().filter((a) -> (a.get(tableColumn)) != null).collect(Collectors.toList());
            }
            return newlist;
        }
        String SecondValueClass = SecondValue.getClass().getSimpleName();

        if (!tableColumnString.matches("(id|lastName|age|cost|active)")) {
            throw new RuntimeException("column with name '" + tableColumnString + "' not exist ");
        }
        switch (operator) {
            case (">") -> {
                if (!(tableColumnString.matches("(cost|age|id)") && SecondValueClass.matches("(Double|Long)"))) {
                    throw new RuntimeException(SecondValueClass + " wrong class to compare with operator '" + operator + "' to column '" + tableColumnString + "'");
                } else if (tableColumnString.equals("cost") || SecondValueClass.equals("Double")) {
                    newlist = base.stream().filter((a) -> Double.parseDouble(String.valueOf(a.get(tableColumn))) > Double.parseDouble(String.valueOf(SecondValue))).collect(Collectors.toList());
                } else {
                    newlist = base.stream().filter((a) -> Long.parseLong(String.valueOf(a.get(tableColumn))) > Long.parseLong(String.valueOf(SecondValue))).collect(Collectors.toList());
                }
            }
            case (">=") -> {
                if (!(tableColumnString.matches("(cost|age|id)") && SecondValueClass.matches("(Double|Long)"))) {
                    throw new RuntimeException(SecondValueClass + " wrong class to compare with operator '" + operator + "' to column '" + tableColumnString + "'");
                } else if (tableColumnString.equals("cost") || SecondValueClass.equals("Double")) {
                    newlist = base.stream().filter((a) -> Double.parseDouble(String.valueOf(a.get(tableColumn))) >= Double.parseDouble(String.valueOf(SecondValue))).collect(Collectors.toList());
                } else {
                    newlist = base.stream().filter((a) -> Long.parseLong(String.valueOf(a.get(tableColumn))) >= Long.parseLong(String.valueOf(SecondValue))).collect(Collectors.toList());
                }
            }
            case ("<=") -> {
                if (!(tableColumnString.matches("(cost|age|id)") && SecondValueClass.matches("(Double|Long)"))) {
                    throw new RuntimeException(SecondValueClass + " wrong class to compare with operator '" + operator + "' to column '" + tableColumnString + "'");
                } else if (tableColumnString.equals("cost") || SecondValueClass.equals("Double")) {
                    newlist = base.stream().filter((a) -> Double.parseDouble(String.valueOf(a.get(tableColumn))) <= Double.parseDouble(String.valueOf(SecondValue))).collect(Collectors.toList());
                } else {
                    newlist = base.stream().filter((a) -> Long.parseLong(String.valueOf(a.get(tableColumn))) <= Long.parseLong(String.valueOf(SecondValue))).collect(Collectors.toList());
                }
            }
            case ("=") -> {
                if (!columnTypes.get(tableColumn).equals(SecondValueClass)) {
                    newlist = new ArrayList<>();
                } else if (columnTypes.get(tableColumn).equals(SecondValueClass)) {
                    newlist = base.stream().filter((a) -> String.valueOf(a.get(tableColumn)).equals(String.valueOf(SecondValue))).collect(Collectors.toList());
                } else {
                    throw new RuntimeException(SecondValueClass + " wrong class to compare with operator '" + operator + "' to column '" + tableColumnString + "'");
                }
            }
            case ("!=") -> {
                if (!columnTypes.get(tableColumn).equals(SecondValueClass)) {
                    newlist = base;
                } else if (columnTypes.get(tableColumn).equals(SecondValueClass)) {
                    newlist = base.stream().filter((a) -> !String.valueOf(a.get(tableColumn)).equals(String.valueOf(SecondValue))).collect(Collectors.toList());
                } else {
                    throw new RuntimeException(SecondValueClass + " wrong class to compare with operator '" + operator + "' to column '" + tableColumnString + "'");
                }
            }
            case ("like") -> {
                if (tableColumnString.matches("(lastName)") && SecondValueClass.matches("(String)")) {
                    newlist = base.stream().filter((a) -> likeOperation((String) a.get(tableColumn), SecondValue.toString())).collect(Collectors.toList());
                } else {
                    throw new RuntimeException(SecondValueClass + " wrong class to compare with operator '" + operator + "' to column '" + tableColumnString + "'");
                }
            }
            case ("ilike") -> {
                if (tableColumnString.matches("(lastName)") && SecondValueClass.matches("(String)")) {
                    newlist = base.stream().filter((a) -> likeOperation(a.get(tableColumn).toString().toLowerCase(), SecondValue.toString().toLowerCase())).collect(Collectors.toList());
                } else {
                    throw new RuntimeException(SecondValueClass + " wrong class to compare with operator '" + operator + "' to column '" + tableColumnString + "'");
                }
            }
            case ("<") -> {
                if (!(tableColumnString.matches("(cost|age|id)") && SecondValueClass.matches("(Double|Long)"))) {
                    throw new RuntimeException(SecondValueClass + " wrong class to compare with operator '" + operator + "' to column '" + tableColumnString + "'");
                } else if (tableColumnString.equals("cost") || SecondValueClass.equals("Double")) {
                    newlist = base.stream().filter((a) -> Double.parseDouble(String.valueOf(a.get(tableColumn))) < Double.parseDouble(String.valueOf(SecondValue))).collect(Collectors.toList());
                } else {
                    newlist = base.stream().filter((a) -> Long.parseLong(String.valueOf(a.get(tableColumn))) < Long.parseLong(String.valueOf(SecondValue))).collect(Collectors.toList());
                }
            }
            default -> throw new IllegalStateException("Unexpected operator: " + operator);
        }
        return newlist;
    }

    private Boolean likeOperation(String tableInput, String conditionInput) {
        StringBuilder regexBuilder = new StringBuilder();
        regexBuilder.append(conditionInput);
        if (Character.valueOf(conditionInput.charAt(0)).hashCode() == 37) {
            regexBuilder.insert(0, ".*");
            regexBuilder.deleteCharAt(2);
        }
        if (Character.valueOf(conditionInput.charAt(conditionInput.length() - 1)).hashCode() == 37) {
            regexBuilder.insert(regexBuilder.length() - 1, ".*");
            regexBuilder.deleteCharAt(regexBuilder.length() - 1);
        }
        return tableInput.matches(String.valueOf(regexBuilder));
    }
}
