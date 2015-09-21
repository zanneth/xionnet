package controllers;

import controllers.SessionController;
import java.util.*;
import play.*;
import play.mvc.*;
import views.html.*;
import models.*;
import util.RequestUtil;

public class ScoreController extends Controller {
    public Result createScore()
    {
        Result result = null;
        Http.RequestBody body = this.request().body();
        
        String name = RequestUtil.getBodyValue(body, "name");
        long scoreValue = Long.parseLong(RequestUtil.getBodyValue(body, "value"));
        long gameID = Long.parseLong(RequestUtil.getBodyValue(body, "gameid"));
        Game game = Game.find.byId(gameID);
        
        if (name != null && game != null) {
            Score score = new Score();
            score.playerName = name;
            score.scoreValue = scoreValue;
            
            game.addScore(score);
            game.save();
            
            result = created();
        } else {
            result = badRequest();
        }
        
        return result;
    }
}
