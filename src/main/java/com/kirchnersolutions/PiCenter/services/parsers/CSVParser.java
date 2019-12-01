package com.kirchnersolutions.PiCenter.services.parsers;

import com.kirchnersolutions.PiCenter.entites.DBItem;

import java.util.List;

public interface CSVParser {

    public String parseToCSV(List<DBItem> items);

    public List<DBItem> parseToList(String CSV);

    public List<DBItem> parseToListWithoutId(String CSV);

}
