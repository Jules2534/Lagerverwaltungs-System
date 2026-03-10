package iu.lagerverwaltung.service;

import iu.lagerverwaltung.accessmanagement.WarehouseOperatorOnly;
import iu.lagerverwaltung.event.Event;
import iu.lagerverwaltung.repository.EventRepository;
import iu.lagerverwaltung.repository.SessionRepository;
import iu.lagerverwaltung.session.Session;
import iu.lagerverwaltung.usermanagement.User;
import iu.lagerverwaltung.usermanagement.UserRole;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SessionService {

    @Inject
    private SessionRepository sessionRepository;

    @Inject
    private EventRepository eventRepository;

    public boolean canCreateSession(User user, Long eventId){

        if (user == null){
            return false;
        }

        if(user.getRole().equals(UserRole.ADMIN)){
            return true;
        }

        if(!user.getRole().equals(UserRole.LAGERIST)){
            return false;
        }

        Event event = eventRepository.findById(eventId);
        if (event == null || event.getWarehouseOperator() == null){
            return false;
        }

        return user.getId().equals(event.getWarehouseOperator().getId());
    }

    @WarehouseOperatorOnly
    public boolean createSession(Session session, User warehouseOperator, Long eventId){

        if (!canCreateSession(warehouseOperator, eventId)){
            return false;
        }

        Event event = eventRepository.findById(eventId);
        if (event == null){
            return false;
        }

        session.setEvent(event);
        session.setWarehouseOperator(warehouseOperator);

        try{
            sessionRepository.save(session);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}