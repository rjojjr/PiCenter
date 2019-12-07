package com.kirchnersolutions.PiCenter.servers.beans;

import lombok.Data;

@Data
public class Interval {
    private String name = "interval";
    private double Bedroom = 0.0;
    private double LivingRoom = 0.0;
    private double ServerRoom = 0.0;
    private double Office = 0.0;
    private double Outside = 0.0;
    private double Heat = 0.0;
}