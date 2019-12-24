package com.kirchnersolutions.PiCenter.services.parsers;

import com.kirchnersolutions.PiCenter.entites.AppUser;
import com.kirchnersolutions.PiCenter.entites.DBItem;
import com.kirchnersolutions.PiCenter.entites.Reading;
import com.kirchnersolutions.PiCenter.entites.UserLog;

import java.util.ArrayList;
import java.util.List;

public class CSVParserImpl implements CSVParser{

    public String parseToCSV(List<DBItem> items){
        return getCSV(items);
    }

    public List<DBItem> parseToList(String CSV) {
        return getDbItems(CSV, true);
    }

    public List<DBItem> parseToListWithoutId(String CSV) {
        return getDbItems(CSV, false);
    }

    private String getCSV(List<DBItem> items) {
        if(items.size() <= 0){
            return null;
        }
        StringBuilder CSV = new StringBuilder(items.get(0).getType() + "." + items.get(0).getCSVHeader());
        for (DBItem item : items) {
            CSV.append("\r\n" +
                    item.toCSVDateString());
        }
        return CSV.toString();
    }

    private DBItem toItem(String type, String csv, boolean withId){
        DBItem item;
        switch (type){
            case "AppUser" :
                item = new AppUser();
                item.fromCSV(csv, withId);
                return item;
            case "Reading" :
                item = new Reading();
                item.fromCSV(csv, withId);
                return item;
            case "UserLog" :
                item = new UserLog();
                item.fromCSV(csv, withId);
                return item;
            default:
                return null;
        }


    }

    private List<DBItem> getDbItems(String CSV, boolean withId) {
        boolean first = true;
        String type = "";
        List<DBItem> output = new ArrayList<>();
        String[] items = CSV.split("\r\n");
        for (String item : items) {
            if (first) {
                first = false;
                type = item.split(".")[0];
            } else {
                DBItem item1 = toItem(type, item, withId);
                if(item != null){
                    output.add(toItem(type, item, withId));
                }else{
                    //log here
                }
            }
        }
        return output;
    }
}
