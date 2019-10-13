package com.kirchnersolutions.PiCenter.entites;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Getter @Setter
@Entity
@Table(name = "user_sessions")
public class UserSession {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter @Setter private Long id;

    @Column(name = "user_id", nullable = false)
    @Getter@Setter private Long userId;

    @Column(name = "start_time", nullable = false)
    @Getter@Setter private Long createTime;

    @Column(name = "expiration_time", nullable = false)
    @Getter@Setter private Long expirationTime;

    @Column(name = "token", nullable = false)
    @Getter@Setter private BigInteger token;

    public UserSession(Long userId, Long createTime, Long expirationTime, BigInteger token){
        this.userId = userId;
        this.createTime = createTime;
        this.expirationTime = expirationTime;
        this.token = token;
    }

}
