package iu.lagerverwaltung.usermanagement;

import iu.lagerverwaltung.dto.UserRegistrationDTO;
import iu.lagerverwaltung.repository.UserRepository;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class UserRegistrationBean implements Serializable {

    @Inject
    private UserRepository userRepository;

    private UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();

    public List<UserRole> getAvailableRoles() {
        return List.of(UserRole.values());
    }

    public void register() {

        // Benutzername prüfen
        if (userRepository.usernameExists(userRegistrationDTO.getUsername())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Fehler",
                    "Dieser Benutzername ist bereits vergeben."
            ));
            return;
        }

        // E-Mail prüfen
        if (userRepository.emailExists(userRegistrationDTO.getEmail())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Fehler",
                    "Diese E-Mail Adresse ist bereits registriert."
            ));
            return;
        }

        try {
            userRegistrationDTO.setPassword(PWUtil.hashPassword(userRegistrationDTO.getPassword()));

            User user = new User(userRegistrationDTO);

            userRepository.save(user);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Erfolg",
                    "Registrierung war erfolgreich"
            ));

            userRegistrationDTO = new UserRegistrationDTO();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Fehler",
                    "Der Benutzer konnte nicht gespeichert werden."
            ));
        }
    }

    public UserRegistrationDTO getUserRegistrationDTO() {
        return userRegistrationDTO;
    }

    public void setUserRegistrationDTO(UserRegistrationDTO userRegistrationDTO) {
        this.userRegistrationDTO = userRegistrationDTO;
    }
}