package controllers;

import controllers.SessionController;
import java.util.*;
import play.*;
import play.mvc.*;
import views.html.*;
import models.Score;
import models.PlayerUser;

public class Application extends Controller {
    public Result index()
    {
        Result result;
        
        boolean adminExists = (PlayerUser.getAdministratorUser() != null);
        if (adminExists) {
            List<Score> scores = Score.find.fetch("playerUser").findList();
            result = ok(index.render(scores));
        } else {
            result = ok(createadmin.render());
        }
        
        return result;
    }
}
