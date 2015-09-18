package controllers;

import controllers.SessionController;
import java.util.*;
import play.*;
import play.mvc.*;
import views.html.*;
import models.*;

public class Application extends Controller {
    public Result index()
    {
        Result result;
        
        boolean adminExists = (PlayerUser.getAdministratorUser() != null);
        if (adminExists) {
            result = ok(index.render());
        } else {
            result = redirect("/users/createadmin");
        }
        
        return result;
    }
}
