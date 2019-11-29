package com.kirchnersolutions.PiCenter.services.interfaces;

import com.kirchnersolutions.PiCenter.entites.Reading;

import java.util.ArrayList;
import java.util.List;

public class ReadingCSVImpl implements CSVParser {

    @Override
    public String parseToCSV(List<DBItem> items) {
        return null;
    }

    @Override
    public List<DBItem> parseToList(String CSV) {
        return null;
    }

    private String getCSV(List<DBItem> items) {
        StringBuilder CSV = new StringBuilder("id,reading_time,temperature,humidity,room_name");
        for (DBItem item : items) {
            Reading reading = (Reading) item;
            CSV.append("\r\r" +
                    reading.getId() + "," +
                    reading.getTime() + "," +
                    reading.getTemp() + "," +
                    reading.getHumidity() + "," +
                    reading.getRoom());
        }
        return CSV.toString();
    }


    private Reading toItem(String string){
        String[] columns = string.split(",");
        return new Reading(
                Long.parseLong(columns[0]),
                Long.parseLong(columns[1]),
                Integer.parseInt(columns[2]),
                Integer.parseInt(columns[3]),
                columns[4]
        );
    }

    private List<DBItem> getDbItems(String CSV) {
        boolean first = true;
        List<DBItem> output = new ArrayList<>();
        String[] items = CSV.split("\r\n");
        for (String item : items) {
            if (first) {
                first = false;
            } else {
                output.add(toItem(item));
            }
        }
        return output;
    }
}
