package controllers;

import java.util.*;
import play.*;
import play.libs.Json;
import play.mvc.*;
import views.html.*;
import models.*;
import util.RequestUtil;

public class GameController extends Controller {
    public Result getGames()
    {
        List<Game> games = Game.find.findList();
        return ok(Json.toJson(games));
    }
}
