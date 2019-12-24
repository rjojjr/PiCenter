package com.kirchnersolutions.PiCenter.servers.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessLine {

    private String user;
    private int pid;
    private double cpu;
    private double mem;
    private int vsz;
    private int rss;
    private String tty;
    private String stat;
    private String start;
    private String time;
    private String command;

}
