package com.kirchnersolutions.PiCenter.entites;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
public class Read {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long time;

    @Getter
    @Setter
    private int temp;

    @Getter
    @Setter
    private int humidity;

    @Getter
    @Setter
    private String room;

}
