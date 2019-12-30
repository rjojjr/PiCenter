package com.kirchnersolutions.PiCenter.servers.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScatterPoint {

    private long time = 0;
    private int inside = 0;
    private int outside = 0;

}
