package com.kirchnersolutions.PiCenter.entites;

public interface DBItem {

    public String getCSVHeader();

    public String toCSV();

    public void fromCSV(String csv, boolean withId);

    public String getType();

    public String toCSVDateString();

}
