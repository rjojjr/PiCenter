package com.kirchnersolutions.PiCenter.servers.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@AllArgsConstructor
public class RoomSummary {

    private String roomName = "";

    //0 - instant, 1 - 1hr, 2 - 2hr, 3 - 3hr, 4 - 6hr, 5 - 12hr, 6 - 24hr
    //<>Devi = standard deviation
    private String[] temps, humiditys, tempDevi, humidityDevi;
    private double[] relation, longTermRelation, change, longChange;

    public RoomSummary(String roomName, String[] temps, String[] humiditys, String[] tempDevi, String[] humidityDevi){
        this.roomName = roomName;
        this.temps = temps;
        this.humiditys = humiditys;
        this.tempDevi = tempDevi;
        this.humidityDevi = humidityDevi;
    }

}
