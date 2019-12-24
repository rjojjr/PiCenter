package com.kirchnersolutions.PiCenter.servers.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceStatus {

    private String name = "", running = "", piTempStart = "", dhtStart = "";

}
