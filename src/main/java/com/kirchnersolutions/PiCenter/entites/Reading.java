package com.kirchnersolutions.PiCenter.entites;

import com.kirchnersolutions.utilities.CalenderConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "readings")
public class Reading implements DBItem {

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

    public Reading(Long id, Long time, int temp, int humidity, String room){
        this.id = id;
        this.time = time;
        this.temp = temp;
        this.humidity = humidity;
        this.room = room;
    }

    public String getCSVHeader() {
        return "id,reading_time,temperature,humidity,room_name";
    }

    public String toCSV() {
        return this.getId() + "," +
                this.getTime() + "," +
                this.getTemp() + "," +
                this.getHumidity() + "," +
                this.getRoom();
    }

    public String toCSVDateString() {
        return this.getId() + "," +
                CalenderConverter.getMonthDayYearHourMinuteSecond(this.getTime(), "/", ":") + "," +
                this.getTemp() + "," +
                this.getHumidity() + "," +
                this.getRoom();
    }

    public String getType(){
        return "Reading";
    }


    public void fromCSV(String csv, boolean withId) {
        String[] columns = csv.split(",");
        if (withId) {
            this.id = Long.parseLong(columns[0]);
            this.time = Long.parseLong(columns[1]);
            this.temp = Integer.parseInt(columns[2]);
            this.humidity = Integer.parseInt(columns[3]);
            this.room = columns[4];

        } else {
            this.time = Long.parseLong(columns[1]);
            this.temp = Integer.parseInt(columns[2]);
            this.humidity = Integer.parseInt(columns[3]);
            this.room = columns[4];
        }
    }

}
