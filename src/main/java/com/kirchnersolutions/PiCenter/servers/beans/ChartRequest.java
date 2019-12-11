package com.kirchnersolutions.PiCenter.servers.beans;

import lombok.Data;

@Data
public class ChartRequest {

    private String fromDate;
    private String toDate;

    //temp || humidity
    private String type = "temp";

}
