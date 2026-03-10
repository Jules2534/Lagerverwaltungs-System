package iu.lagerverwaltung.service;

import iu.lagerverwaltung.event.Event;
import iu.lagerverwaltung.eventsignup.EventSignup;
import iu.lagerverwaltung.eventsignup.SignupStatus;
import iu.lagerverwaltung.repository.EventRepository;
import iu.lagerverwaltung.repository.EventSignupRepository;
import iu.lagerverwaltung.repository.UserRepository;
import iu.lagerverwaltung.usermanagement.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class EventSignupService {

    @Inject
    private EventSignupRepository eventSignupRepository;

    @Inject
    private EventRepository eventRepository;

    @Inject
    private UserRepository userRepository;

    public boolean isUserSignedUp(Long userId, Long eventId){
        // ist user für ein bestimmtes event registriert? == verknüpfung besteht und
        // status == Registered
        return eventSignupRepository.findByUserAndEvent(userId, eventId)
                .map(s -> s.getStatus() == SignupStatus.REGISTERED)
                .orElse(false);
    }

    public boolean signupUserForEvent(Long userId, Long eventId){
        User user = userRepository.findById(userId);
        Event event = eventRepository.findById(eventId);

        if (user == null || event == null){
            return false;
        }

        Optional<EventSignup> existingSignup = eventSignupRepository.findByUserAndEvent(userId, eventId);

        if (existingSignup.isPresent()){
            EventSignup signup = existingSignup.get();

            // ist der user schon für dieses event registriert?
            if (signup.getStatus() == SignupStatus.REGISTERED){
                return false;
            }

            // falls status == cancelled, erlauben wir eine erneute registrierung und wechseln den
            // status zu REGISTERED
            if (signup.getStatus() == SignupStatus.CANCELLED){
                signup.setStatus(SignupStatus.REGISTERED);
                signup.setSignupDate(LocalDateTime.now());
                eventSignupRepository.save(signup);
                return true;
            }
        }

        // signup gab es noch nicht, d.h. wir müssen diesen neu anlegen
        EventSignup signup = new EventSignup(user, event, LocalDateTime.now(), SignupStatus.REGISTERED);
        eventSignupRepository.save(signup);
        return true;

    }

    public boolean cancelSignup(Long userId, Long eventId) {
        // gibt es zu der event id und der userid eine passende signup?
        Optional<EventSignup> signup = eventSignupRepository.findByUserAndEvent(userId, eventId);
        // wenn nicht, kann ich auch nicht cancel, also brechen wir ab
        if (signup.isEmpty()) {
            return false;
        }
        // falls ja, dann setze den status auf cancelled
        signup.get().setStatus(SignupStatus.CANCELLED);
        // und update das signup im repository
        eventSignupRepository.save(signup.get());
        return true;
    }

}
