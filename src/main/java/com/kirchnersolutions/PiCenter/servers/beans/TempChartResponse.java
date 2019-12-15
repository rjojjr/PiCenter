package com.kirchnersolutions.PiCenter.servers.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TempChartResponse {

    private TempInterval[] intervals = new TempInterval[0];

}


