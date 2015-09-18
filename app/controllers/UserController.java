package controllers;

import java.util.*;
import play.*;
import play.data.*;
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
            boolean valid = this.validateRequest(body);
            
            if (valid) {
                String username = RequestUtil.getBodyValue(body, "username");
                String password = RequestUtil.getBodyValue(body, "password1");
                
                PlayerUser user = new PlayerUser(username, password);
                user.isAdministrator = isCreatingAdmin;
                user.canVerifyScores = isCreatingAdmin;
                user.save();
                
                result = redirect("/index");
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
    
    public Result createUserView()
    {
        return this.renderCreateUserView(false);
    }
    
    public Result createAdminView()
    {
        return this.renderCreateUserView(true);
    }
    
    /** Internal **/
    
    private Result renderCreateUserView(boolean admin)
    {
        if (admin) {
            return ok(createadmin.render());
        } else {
            return notFound();
        }
    }
    
    private static boolean validateRequest(Http.RequestBody requestBody)
    {
        String username = RequestUtil.getBodyValue(requestBody, "username");
        String password1 = RequestUtil.getBodyValue(requestBody, "password1");
        String password2 = RequestUtil.getBodyValue(requestBody, "password2");
        
        return (
            username != null && username.length() > 0 &&
            password1 != null && password1.length() > 0 &&
            password1.equals(password2)
        );
    }
}
