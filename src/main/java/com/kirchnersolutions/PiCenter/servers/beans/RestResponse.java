package com.kirchnersolutions.PiCenter.servers.beans;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse {

    private String responseBody = "{ body: 'null' }";

    private RestUser restUser = new RestUser();

    private RoomSummary[] summary = new RoomSummary[0];

    private ChartResponse chart = new ChartResponse(new TempInterval[0]);

    private DeviceStatus[] deviceStatuses = new DeviceStatus[0];

    public RestResponse(String responseBody){
        this.responseBody = responseBody;
    }

    public RestResponse(String responseBody, RestUser restUser){
        this.responseBody = responseBody;
        this.restUser = restUser;
    }

    public RestResponse(String responseBody, RestUser restUser, ChartResponse chartResponse){
        this.responseBody = responseBody;
        this.restUser = restUser;
        this.chart = chartResponse;
    }

    public RestResponse(String responseBody, RestUser restUser, DeviceStatus[] deviceStatuses){
        this.responseBody = responseBody;
        this.restUser = restUser;
        this.deviceStatuses = deviceStatuses;
    }

    public RestResponse(RestUser restUser){
        this.restUser = restUser;
    }

    public RestResponse(String responseBody, RestUser restUser, RoomSummary[] summary){
        this.responseBody = responseBody;
        this.restUser = restUser;
        this.summary = summary;
    }

    public RestResponse(RestUser restUser, RoomSummary[] summary){
        this.restUser = restUser;
        this.summary = summary;
    }

    public RestResponse(RestUser restUser, RoomSummary[] summary, ChartResponse chartResponse){
        this.restUser = restUser;
        this.summary = summary;
        this.chart = chartResponse;
    }

}
