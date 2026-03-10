package iu.lagerverwaltung.event;

import iu.lagerverwaltung.eventsignup.EventSignup;
import iu.lagerverwaltung.session.Session;
import iu.lagerverwaltung.usermanagement.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String location;
    private String date;
    private String state;

    @ManyToOne
    @JoinColumn(name = "warehouseOperator_user_id")
    private User warehouseOperator;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventSignup> productionPlanners = new ArrayList<>();

    protected Event() {
    }

    public Event(String name, String location, String date, String state) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public User getWarehouseOperator() {
        return warehouseOperator;
    }

    public void setWarehouseOperator(User warehouseOperator) {
        this.warehouseOperator = warehouseOperator;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public List<EventSignup> getProductionPlanners() {
        return productionPlanners;
    }

    public void setProductionPlanners(List<EventSignup> productionPlanners) {
        this.productionPlanners = productionPlanners;
    }
}