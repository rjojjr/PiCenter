package com.kirchnersolutions.PiCenter.services.interfaces;

import java.util.List;

public interface CSVParser {

    public String parseToCSV(List<Object> items);

    public List<Object> parseToList(String CSV);

}
