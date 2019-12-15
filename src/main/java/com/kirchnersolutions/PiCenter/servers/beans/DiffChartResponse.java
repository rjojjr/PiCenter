package com.kirchnersolutions.PiCenter.servers.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiffChartResponse {
    private DiffInterval[] intervals = new DiffInterval[0];
}
