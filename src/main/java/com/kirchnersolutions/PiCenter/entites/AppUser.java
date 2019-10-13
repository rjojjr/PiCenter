package com.kirchnersolutions.PiCenter.entites;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Entity
@Table(name = "app_users")
public class AppUser {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter @Setter private Long id;

    @Column(name = "creation_time", nullable = false)
    @Getter@Setter private Long createTime;

    @Column(name = "first_name", nullable = false)
    @Getter @Setter private String firstName;

    @Column(name = "last_name", nullable = false)
    @Getter @Setter private String lastName;

    @Column(name = "user_name", nullable = false, unique = true)
    @Getter @Setter private String userName;

    @Column(name = "password", nullable = false)
    @Getter @Setter private String password;

    @Column(name = "admin", nullable = false)
    @Getter @Setter private boolean admin;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "appuser_sessions",
            joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "session_id", referencedColumnName = "id") })
    private UserSession userSession;

    public AppUser(Long createTime, String userName, String firstName, String lastName, String password, boolean admin){
        this.createTime = createTime;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.admin = admin;
    }

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
