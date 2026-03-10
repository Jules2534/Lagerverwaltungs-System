package iu.lagerverwaltung.usermanagement;

import iu.lagerverwaltung.repository.UserRepository;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@SessionScoped
public class UserLoginBean implements Serializable {

    private String username;
    private String password;

    private User loggedInUser;

    @Inject
    private UserRepository userRepository;

    public String login() {

        User user = userRepository.findByUsername(username);

        if (user == null || !PWUtil.verifyPassword(password, user.getPassword())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Login fehlgeschlagen.",
                            "Benutzername oder Passwort ist falsch."));
            return null;
        }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Login war erfolgreich",
                        "Login war erfolgreich"));

        loggedInUser = user;
        password = null;

        FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .put("loggedInUser", loggedInUser);

        return "/index.xhtml?faces-redirect=true";
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public boolean isAdmin() {
        return loggedInUser != null && loggedInUser.getRole() == UserRole.ADMIN;
    }

    public boolean isLagerist() {
        return loggedInUser != null && loggedInUser.getRole() == UserRole.LAGERIST;
    }

    public boolean isProduktionsplaner() {
        return loggedInUser != null && loggedInUser.getRole() == UserRole.PRODUKTIONSPLANER;
    }

    public boolean isAdminOrLagerist() {
        return isAdmin() || isLagerist();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}