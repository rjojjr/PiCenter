package com.kirchnersolutions.PiCenter.entites;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
public class UserLog {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long userId;

    @Getter
    @Setter
    private String action;

    @Getter
    @Setter
    private Long time;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userId")
    private User owner;

}
