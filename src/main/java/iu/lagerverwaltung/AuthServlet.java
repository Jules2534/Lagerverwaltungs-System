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

    private static final String LOGIN_PAGE = "/login.xhtml";

    private static final List<String> PUBLIC_PAGES = List.of(
            "/login.xhtml",
            "/register.xhtml"
    );

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String context = req.getContextPath();
        String path = uri.substring(context.length());

        HttpSession session = req.getSession(false);

        User user = null;

        if (session != null) {
            user = (User) session.getAttribute("loggedInUser");
        }

        // JSF Ressourcen erlauben
        boolean isResource = path.startsWith("/jakarta.faces.resource/");
        if (isResource) {
            chain.doFilter(request, response);
            return;
        }

        // Öffentliche Seiten erlauben
        boolean isPublicPage = PUBLIC_PAGES.contains(path);
        if (isPublicPage) {
            chain.doFilter(request, response);
            return;
        }

        // Wenn nicht eingeloggt → Login
        if (user == null) {
            resp.sendRedirect(context + LOGIN_PAGE);
            return;
        }

        UserRole role = user.getRole();

        // ADMIN und LAGERIST dürfen alles
        if (role == UserRole.ADMIN || role == UserRole.LAGERIST) {
            chain.doFilter(request, response);
            return;
        }

        // PRODUKTIONSPLANER darf nur bestimmte Seiten
        if (role == UserRole.PRODUKTIONSPLANER) {

            boolean allowed =
                    path.equals("/index.xhtml") ||
                            path.equals("/inventory.xhtml");

            if (!allowed) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        chain.doFilter(request, response);
    }
}