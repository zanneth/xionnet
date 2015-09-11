package controllers;

import java.util.*;
import play.*;
import play.mvc.*;
import views.html.*;
import models.Score;

public class Application extends Controller {
    public Result index()
    {
        Score s = new Score();
        s.scoreValue = 1337;
        s.save();
        
        List<Score> scores = Score.find.findList();
        return ok(index.render(scores));
    }
}
