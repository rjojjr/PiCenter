package com.kirchnersolutions.PiCenter.servers.beans;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse {

    private String responseBody = "{ body: 'null' }";

    private RestUser restUser = new RestUser();

    private RoomSummary[] summary = new RoomSummary[0];

    private TempChartResponse chart = new TempChartResponse(new TempInterval[0]);

    public RestResponse(String responseBody){
        this.responseBody = responseBody;
    }

    public RestResponse(String responseBody, RestUser restUser){
        this.responseBody = responseBody;
        this.restUser = restUser;
    }

    public RestResponse(String responseBody, RestUser restUser, TempChartResponse tempChartResponse){
        this.responseBody = responseBody;
        this.restUser = restUser;
        this.chart = tempChartResponse;
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

    public RestResponse(RestUser restUser, RoomSummary[] summary, TempChartResponse tempChartResponse){
        this.restUser = restUser;
        this.summary = summary;
        this.chart = tempChartResponse;
    }

}
