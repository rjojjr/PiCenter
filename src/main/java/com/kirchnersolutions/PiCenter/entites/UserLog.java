package com.kirchnersolutions.PiCenter.entites;

import com.kirchnersolutions.PiCenter.services.interfaces.DBItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(name = "user_logs")
public class UserLog implements DBItem {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Getter
    @Setter
    @Column(name = "action", nullable = false)
    private String action;

    @Getter
    @Setter
    @Column(name = "time", nullable = false)
    private Long time;

    public UserLog(Long userId, String action, Long time){
        this.userId = userId;
        this.action = action;
        this.time = time;
    }
/*
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userId")
    private User owner;

 */

}
