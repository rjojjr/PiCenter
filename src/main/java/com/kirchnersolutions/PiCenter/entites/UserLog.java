package com.kirchnersolutions.PiCenter.entites;

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

    public UserLog(Long id, Long userId, String action, Long time){
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.time = time;
    }

    public String getCSVHeader() {
        return "id,user_id,action,time";
    }



    public String getType(){
        return "UserLog";
    }


    public String toCSV() {
        return this.getId() + "," +
                this.getUserId() + "," +
                this.getAction() + "," +
                this.getTime();
    }

    public void fromCSV(String csv, boolean withId) {
        String[] columns = csv.split(",");
        if (withId) {
            this.id = Long.parseLong(columns[0]);
            this.userId = Long.parseLong(columns[1]);
            this.action= columns[2];
            this.time = Long.parseLong(columns[3]);

        } else {
            this.userId = Long.parseLong(columns[1]);
            this.action= columns[2];
            this.time = Long.parseLong(columns[3]);
        }
    }
/*
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userId")
    private User owner;

 */

}
