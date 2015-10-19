package controllers;

import controllers.GameObserver;
import com.fasterxml.jackson.databind.JsonNode;
import java.sql.Timestamp;
import java.util.*;
import play.*;
import play.libs.Json;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.F.RedeemablePromise;
import play.mvc.*;
import views.html.*;
import models.*;
import util.RequestUtil;

public class GameController extends Controller {
    public Result createGameView()
    {
        return ok(creategame.render());
    }
    
    public Result gameView(Long id)
    {
        Game game = Game.find.byId(id);
        if (game != null) {
            return ok(gamedetails.render(game));
        } else {
            return notFound("not found");
        }
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
        List<Game> games = Game.find.orderBy("name desc").findList();
        Map<String, Object> responseObject = new HashMap<String, Object>();
        responseObject.put("games", games);
        
        return ok(Json.toJson(responseObject));
    }
    
    public Result getGame(Long id)
    {
        Game game = Game.find.byId(id);
        if (game != null) {
            return ok(Json.toJson(game));
        } else {
            return notFound("not found");
        }
    }
    
    public Promise<Result> pollGame(Long id)
    {
        Promise<Result> promise = null;
        
        // make sure game exists first
        Game game = Game.find.byId(id);
        if (game != null) {
            GameObserver observer = GameObserver.getSharedObserver();
            Promise<Game> gamePromise = observer.createChangedGamePromise(id);
            promise = gamePromise.map(
                changedGame -> (Result)ok(Json.toJson(changedGame))
            );
        } else {
            promise = Promise.pure(notFound("not found"));
        }
        
        return promise;
    }
}
