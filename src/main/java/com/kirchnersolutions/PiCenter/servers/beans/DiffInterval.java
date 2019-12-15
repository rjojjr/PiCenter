package com.kirchnersolutions.PiCenter.servers.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiffInterval implements Interval {
    private String name = "interval";
    private String Bedroom = "0-0";
    private String LivingRoom = "0-0";
    private String ServerRoom = "0-0";
    private String Office = "0-0";
    private String Outside = "0-0";
    private String Heat = "0-0";
}
