package controllers;

import java.util.*;
import play.*;
import play.libs.Json;
import play.mvc.*;
import views.html.*;
import models.Score;
import models.PlayerUser;
import util.RequestUtil;

public class UserController extends Controller {
    public Result createUser()
    {
        Result result = null;
        Http.RequestBody body = this.request().body();
        Map<String, String[]> reqMap = body.asFormUrlEncoded();
        
        // if an administrator account is trying to be created, make sure
        // one doesn't exist already
        boolean isCreatingAdmin = reqMap.containsKey("administrator");
        if (isCreatingAdmin && PlayerUser.getAdministratorUser() != null) {
            result = forbidden();
        }
        
        if (result == null) {
            String username = RequestUtil.getBodyValue(body, "username");
            String password = RequestUtil.getBodyValue(body, "password");
            if (username != null && password != null) {
                PlayerUser user = new PlayerUser(username, password);
                user.isAdministrator = isCreatingAdmin;
                user.canVerifyScores = isCreatingAdmin;
                user.save();
                
                result = ok();
            } else {
                result = badRequest();
            }
        }
        
        return result;
    }
    
    public Result getUsers()
    {
        List<PlayerUser> users = PlayerUser.find.findList();
        return ok(Json.toJson(users));
    }
}
