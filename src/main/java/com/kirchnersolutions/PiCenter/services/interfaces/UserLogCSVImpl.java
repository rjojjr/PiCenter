package com.kirchnersolutions.PiCenter.services.interfaces;


import com.kirchnersolutions.PiCenter.entites.UserLog;

import java.util.ArrayList;
import java.util.List;

public class UserLogCSVImpl implements CSVParser{

    public String parseToCSV(List<DBItem> items) {
        return getCSV(items);
    }

    public List<DBItem> parseToList(String CSV) {
        return getDbItems(CSV, true);
    }

    public List<DBItem> parseToListWithoutId(String CSV) {
        return getDbItems(CSV, false);
    }

    private String getCSV(List<DBItem> items) {
        StringBuilder CSV = new StringBuilder("id,user_id,action,time");
        for (DBItem item : items) {
            UserLog userLog = (UserLog) item;
            CSV.append("\r\r" +
                    userLog.getId() + "," +
                    userLog.getUserId() + "," +
                    userLog.getAction() + "," +
                    userLog.getTime());
        }
        return CSV.toString();
    }


    private UserLog toItem(String string, boolean withId){
        String[] columns = string.split(",");
        if(withId){
            return new UserLog(
                    Long.parseLong(columns[0]),
                    Long.parseLong(columns[1]),
                    columns[2],
                    Long.parseLong(columns[3])
            );
        }else {
            return new UserLog(
                    Long.parseLong(columns[1]),
                    columns[2],
                    Long.parseLong(columns[3])
            );
        }
    }

    private List<DBItem> getDbItems(String CSV, boolean withId) {
        boolean first = true;
        List<DBItem> output = new ArrayList<>();
        String[] items = CSV.split("\r\n");
        for (String item : items) {
            if (first) {
                first = false;
            } else {
                output.add(toItem(item, withId));
            }
        }
        return output;
    }
}
