package com.kirchnersolutions.PiCenter.servers.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScatterPoint {

    private String time = "";
    private int inside = 0;
    private int outside = 0;

}
