package com.kirchnersolutions.PiCenter.entites;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long createTime;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String userName;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private boolean admin;

    @Override
    public String toString() {
        if(admin){
            return String.format(
                    "User[id=%d, firstName='%s', lastName='%s', userName='%s' role=admin]",
                    id, firstName, lastName, userName);
        }
        return String.format(
                "User[id=%d, firstName='%s', lastName='%s', userName='%s' role=user]",
                id, firstName, lastName, userName);
    }

}
