package controllers;

import java.util.*;
import play.*;
import play.libs.Json;
import play.mvc.*;
import views.html.*;
import models.*;
import util.RequestUtil;

public class GameController extends Controller {
    public Result createGameView()
    {
        return ok(creategame.render());
    }
    
    public Result createGame()
    {
        Result result = null;
        Http.RequestBody body = this.request().body();
        String gameName = RequestUtil.getBodyValue(body, "name");
        
        // make sure game doesn't exist
        boolean exists = (Game.find.where().eq("name", gameName).findRowCount() > 0);
        if (exists) {
            result = badRequest("game already exists");
        } else {
            Game game = new Game(gameName);
            game.save();
            
            result = created();
        }
        
        return result;
    }
    
    public Result getGames()
    {
        List<Game> games = Game.find.findList();
        Map<String, Object> responseObject = new HashMap<String, Object>();
        responseObject.put("games", games);
        
        return ok(Json.toJson(responseObject));
    }
}
