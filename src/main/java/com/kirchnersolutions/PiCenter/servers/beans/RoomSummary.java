package com.kirchnersolutions.PiCenter.servers.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoomSummary {

    private String roomName = "";

    //0 - instant, 1 - 1hr, 2 - 2hr, 3 - 3hr, 4 - 6hr, 5 - 12hr, 6 - 24hr
    private String[] temps, humiditys;

}