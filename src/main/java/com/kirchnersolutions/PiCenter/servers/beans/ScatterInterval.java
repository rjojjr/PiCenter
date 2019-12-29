package com.kirchnersolutions.PiCenter.servers.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScatterInterval implements Interval{

    private ScatterPoint[] interval = new ScatterPoint[0];

}


