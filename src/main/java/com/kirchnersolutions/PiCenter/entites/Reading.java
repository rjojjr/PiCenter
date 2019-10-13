package com.kirchnersolutions.PiCenter.entites;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
//@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "readings")
public class Reading {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @Column(name = "reading_time", nullable = false)
    private Long time;

    @Getter
    @Setter
    @Column(name = "temperature", nullable = false)
    private int temp;

    @Getter
    @Setter
    @Column(name = "humidity", nullable = false)
    private int humidity;

    @Getter
    @Setter
    @Column(name = "room_name", nullable = false)
    private String room;

    public Reading(Long time, int temp, int humidity, String room){
        this.time = time;
        this.temp = temp;
        this.humidity = humidity;
        this.room = room;
    }

}
