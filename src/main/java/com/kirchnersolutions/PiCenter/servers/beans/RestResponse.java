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

    public RestResponse(RestUser restUser){
        this.restUser = restUser;
    }

}
