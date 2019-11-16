package com.kirchnersolutions.PiCenter.servers.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class CreateUser {

    private String userName = "null";
    private String firstName = "null";
    private String lastName = "null";
    private String password = "null";
    @Getter
    @Setter
    private boolean admin = false;

    /*public boolean getAdmin() {
    }*/
}
