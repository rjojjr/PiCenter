package com.kirchnersolutions.PiCenter.Configuration;
/**
 *2019 Kirchner Solutions
 * @Author Robert Kirchner Jr.
 *
 * This code may not be decompiled, recompiled, copied, redistributed or modified
 * in any way unless given express written consent from Kirchner Solutions.
 */

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class TableConfiguration {

    private String initTablePageSize = "20", tablePageGrowthSize = "100000", maxCacheSize = "500", growthRate = "2";

    public String getInitTablePageSize() {
        return initTablePageSize;
    }

    public String getMaxCacheSize() {
        return maxCacheSize;
    }

    public String getTablePageGrowthSize() {
        return tablePageGrowthSize;
    }

    public void setInitTablePageSize(String initTablePageSize) {
        this.initTablePageSize = initTablePageSize;
    }

    public void setMaxCacheSize(String maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    public void setTablePageGrowthSize(String tablePageGrowthSize) {
        this.tablePageGrowthSize = tablePageGrowthSize;
    }

    public void setVars(String[] vars){
        initTablePageSize = vars[0];
        tablePageGrowthSize = vars[1];
        maxCacheSize = vars[2];
        growthRate = vars[3];
    }

    public String[] getVars(){
        String[] vars = new String[4];
        vars[0] = initTablePageSize;
        vars[1] = tablePageGrowthSize;
        vars[2] = maxCacheSize;
        vars[3] = growthRate;
        return vars;
    }

    public String[] getVarsNames(){
        String[] vars = new String[4];
        vars[0] = "Initial Table Pages:" + initTablePageSize;
        vars[1] = "Table Page Growth Size:" + tablePageGrowthSize;
        vars[2] = "Max Cache Size:" + maxCacheSize;
        vars[3] = "Table Page Growth Rate:" + maxCacheSize;
        return vars;
    }
}