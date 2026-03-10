package iu.lagerverwaltung;

import iu.lagerverwaltung.usermanagement.User;
import iu.lagerverwaltung.usermanagement.UserRole;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebFilter("/*")
public class AuthServlet implements Filter {

    // Login-Seite definieren
    private static final String LOGIN_PAGE = "/login.xhtml";

    // Seiten, die ohne Login zugänglich sind
    private static final List<String> PUBLIC_PAGES = List.of(
            "/login.xhtml",
            "/register.xhtml"
    );

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws ServletException, IOException {

        // Casten der Request- und Response-Objekte
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // Die angeforderte URL der Seite
        String uri = req.getRequestURI();
        String context = req.getContextPath();
        String path = uri.substring(context.length());

        // Session des Nutzers holen (falls vorhanden)
        HttpSession session = req.getSession(false);
        User user = null;

        if (session != null) {
            user = (User) session.getAttribute("loggedInUser");
        }

        // Erlauben von JSF Ressourcen wie CSS, Bilder, etc.
        boolean isResource = path.startsWith("/jakarta.faces.resource/");
        if (isResource) {
            chain.doFilter(request, response);
            return;
        }

        // Überprüfen, ob es eine öffentliche Seite ist (Login, Registrierung, etc.)
        boolean isPublicPage = PUBLIC_PAGES.contains(path);
        if (isPublicPage) {
            chain.doFilter(request, response);
            return;
        }

        // Falls die Seite Root ("/") oder die Startseite ("index.xhtml") ist, erlaube den Zugriff
        if (path.equals("/") || path.equals("/index.xhtml")) {
            chain.doFilter(request, response);
            return;
        }

        // Wenn der Nutzer nicht eingeloggt ist, leite zur Login-Seite weiter
        if (user == null) {
            resp.sendRedirect(context + LOGIN_PAGE);
            return;
        }

        // Rolle des Nutzers ermitteln
        UserRole role = user.getRole();

        // ADMIN und LAGERIST dürfen auf alle Seiten zugreifen
        if (role == UserRole.ADMIN || role == UserRole.LAGERIST) {
            chain.doFilter(request, response);
            return;
        }

        // PRODUKTIONSPLANER darf nur bestimmte Seiten sehen
        if (role == UserRole.PRODUKTIONSPLANER) {
            // Zugelassene Seiten für Produktionsplaner
            boolean allowed =
                    path.equals("/index.xhtml") ||
                            path.equals("/inventory.xhtml");

            // Wenn die Seite nicht zugelassen ist, gebe einen Fehler 403 (Forbidden) zurück
            if (!allowed) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        // Wenn alle Überprüfungen durch sind, setze den Request fort
        chain.doFilter(request, response);
    }
}