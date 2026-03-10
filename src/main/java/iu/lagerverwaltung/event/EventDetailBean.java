package iu.lagerverwaltung.event;

import iu.lagerverwaltung.repository.EventRepository;
import iu.lagerverwaltung.usermanagement.User;
import iu.lagerverwaltung.usermanagement.UserLoginBean;
import iu.lagerverwaltung.usermanagement.UserRole;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class EventDetailBean implements Serializable {

    @Inject
    private EventRepository eventRepository;

    private Long eventId;
    private Event selectedEvent;
    @Named
    @Inject
    private UserLoginBean userLoginBean;

    public boolean canViewProductionPlanners(){
        User currentUser = userLoginBean.getLoggedInUser();

        if (currentUser == null || selectedEvent == null){
            return false;
        }

        if (currentUser.getRole().equals(UserRole.ADMIN)){
            return true;
        }

        User eventWarehouseOperator = selectedEvent.getWarehouseOperator();
        return (eventWarehouseOperator != null && currentUser.getId().equals(eventWarehouseOperator.getId()));
    }

    public void loadEvent(){
        if (eventId == null){
            selectedEvent = null;
            return;
        }
        selectedEvent = eventRepository.findById(eventId);
    }

    public String getWarehouseOperatorNames(){
        loadEvent();
        User warehouseOperator = selectedEvent.getWarehouseOperator();
        if (selectedEvent == null || warehouseOperator == null){
            return "";
        }
        return warehouseOperator.getFirstname() + " " + warehouseOperator.getName();
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }
}
