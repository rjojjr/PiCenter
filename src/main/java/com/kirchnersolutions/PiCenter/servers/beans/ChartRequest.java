package com.kirchnersolutions.PiCenter.servers.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChartRequest {

    private String fromDate;
    private String toDate;

    //temp || humidity
    private String type = "temp";

}
