package com.kirchnersolutions.PiCenter.entites;

import com.kirchnersolutions.PiCenter.servers.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_sessions")
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    /*
    @Column(name = "user_id", nullable = false)
    @Getter@Setter private Long userId;
     */

    //@Column(name = "start_time", nullable = false)
    @Column(name = "start_time")
    @Getter
    @Setter
    private Long createTime;

    //@Column(name = "expiration_time", nullable = false)
    @Column(name = "expiration_time")
    @Getter
    @Setter
    private Long expirationTime;

    //@Column(name = "token", nullable = false, unique = true)
    @Column(name = "token", nullable = true, unique = true)
    @Getter
    @Setter
    private String token;

    @Column(name = "page", nullable = true)
    @Getter
    @Setter
    private String page;

    @Column(name = "stomp_id", nullable = true, unique = true)
    @Getter
    @Setter
    private String stompId;

    //@Column(name = "ip_address", nullable = false, unique = false)
    @Column(name = "ip_address", nullable = true, unique = false)
    @Getter
    @Setter
    private String ipAddress;

    @OneToOne(mappedBy = "userSession")
    @Getter
    @Setter
    private AppUser appUser;

    public UserSession() {

    }

    public UserSession(Long createTime, Long expirationTime, String token) {
        this.createTime = createTime;
        this.expirationTime = expirationTime;
        this.token = token;
        this.page = null;
    }

    public UserSession(Long createTime, Long expirationTime, String token, String page, String ipAddress) {
        this.createTime = createTime;
        this.expirationTime = expirationTime;
        this.token = token;
        this.page = page;
        this.ipAddress = ipAddress;
        stompId = null;
    }
/*
    public UserSession(Long userId, Long createTime, Long expirationTime, BigInteger token){
        this.userId = userId;
        this.createTime = createTime;
        this.expirationTime = expirationTime;
        this.token = token;
    }
*/
}
