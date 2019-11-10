package com.kirchnersolutions.PiCenter.servers.beans;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
public class RestResponse {

    @Getter
    @Setter
    private String responseBody = "{ body: 'null' }";

    @Getter
    @Setter
    private RestUser restUser = new RestUser();

    @Getter
    @Setter
    private RoomSummary[] summary = new RoomSummary[0];

    public RestResponse(String responseBody){
        this.responseBody = responseBody;
    }

    public RestResponse(String responseBody, RestUser restUser){
        this.responseBody = responseBody;
        this.restUser = restUser;
    }

    public RestResponse(RestUser restUser){
        this.restUser = restUser;
    }

    public RestResponse(RestUser restUser, RoomSummary[] summary){
        this.restUser = restUser;
        this.summary = summary;
    }

}
