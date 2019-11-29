package com.kirchnersolutions.PiCenter.services.interfaces;

import java.util.ArrayList;
import java.util.List;

public interface CSVParser {

    public String parseToCSV(List<DBItem> items);

    public List<DBItem> parseToList(String CSV);

}
