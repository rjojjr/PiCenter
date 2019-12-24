package com.kirchnersolutions.PiCenter.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Device {

    private String name = "";
    private String url = "";
    private String token = "";

}
