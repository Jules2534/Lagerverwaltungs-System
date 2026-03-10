package iu.lagerverwaltung.session;

import iu.lagerverwaltung.event.Event;
import iu.lagerverwaltung.usermanagement.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String speaker;
    private String room;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(optional = false)
    @JoinColumn(name = "warehouseOperator_user_id")
    private User warehouseOperator;

    protected Session() {}

    public Session(String title, String speaker, String room, LocalDateTime startTime, LocalDateTime endTime) {
        this.title = title;
        this.speaker = speaker;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getWarehouseOperator() {
        return warehouseOperator;
    }

    public void setWarehouseOperator(User warehouseOperator) {
        this.warehouseOperator = warehouseOperator;
    }
}