package iu.lagerverwaltung.accessmanagement;

import iu.lagerverwaltung.usermanagement.User;
import jakarta.faces.context.FacesContext;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@WarehouseOperatorOnly
@Interceptor
public class WarehouseOperatorOnlyInterceptor {

    @AroundInvoke
    public Object checkAccess(InvocationContext ctx) throws Exception {

        FacesContext faces = FacesContext.getCurrentInstance();

        if (faces == null){
            throw new SecurityException("Kein FacesContext Objekt gefunden.");
        }

        // lies den user auf dem security principle in der session
        User user = (User) faces.getExternalContext().getSessionMap().get("user");

        // ist der user eingeloggt?
        if (user == null){
            throw new SecurityException("Es ist kein Benutzer eingeloggt.");
        }

        // ist der user LAGERIST oder admin?
        if (!user.isWarehouseOperatorOrAdmin()){
            throw new SecurityException("Unzureichende Authorisierung.");
        }

        // wenn ich hier ankomme, dann haben alle meinen checks gepasst und der
        // user request darf weiter verarbeitet werden
        return ctx.proceed();

    }

}
