package com.kirchnersolutions.PiCenter.dev;

import java.io.File;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class LogSorter implements Comparator<File> {

    public int compare(File m1, File m2){
        long m1i = Long.parseLong(m1.getName().split(".t")[0]);
        long m2i = Long.parseLong(m2.getName().split(".t")[0]);
        if(m1i < m2i){
            return 1;
        }
        if(m1i > m2i){
            return -1;
        }
        return 0;
    }
}