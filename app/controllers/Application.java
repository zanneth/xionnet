package controllers;

import java.util.*;
import play.*;
import play.mvc.*;
import views.html.*;
import models.Score;
import models.PlayerUser;

public class Application extends Controller {
    public Result index()
    {
        List<Score> scores = Score.find.fetch("playerUser").findList();
        return ok(index.render(scores));
    }
}
