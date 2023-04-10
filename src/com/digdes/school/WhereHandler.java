package com.digdes.school;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WhereHandler {

    List<Map<String, Object>> table;

    public WhereHandler(List<String> listStrings, List<Map<String, Object>> oldTable) {

        List<Map<String, Object>> data = oldTable;
        List<String> logicOperatorsList = new ArrayList<>();

        List<Condition> listConditions = new ArrayList<>();
        for (int i = 0; i < listStrings.size() - 1; i++) {
            listConditions.add(new Condition(listStrings.get(i), listStrings.get(i + 2), listStrings.get(i + 1), data));
            i = i + 3;
        }

        for (String listString : listStrings) {
            if (listString.matches("(and|or|id)")) {
                logicOperatorsList.add(listString);
            }
        }

        List<Map<String, Object>> newTable = listConditions.get(0).data;
        for (int i = 0; i < listConditions.size() - 1; i++) {
            switch (logicOperatorsList.get(i)) {
                case ("or") -> {
                    for (Map<String, Object> row : listConditions.get(i + 1).data) {
                        if (!newTable.contains(row)) {
                            newTable.add(row);
                        }
                    }
                }
                case ("and") -> {
                    List<Map<String, Object>> bufferTable = new ArrayList<>();
                    for (Map<String, Object> row : listConditions.get(i + 1).data) {
                        if (newTable.contains(row)) {
                            bufferTable.add(row);
                        }
                    }
                    newTable = bufferTable;
                }
            }
        }
        this.table = newTable;
    }

    public List<Map<String, Object>> getTable() {
        return table;
    }
}