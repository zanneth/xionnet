package controllers;

import java.util.*;
import play.mvc.*;
import models.PlayerUser;
import util.CryptoUtil;

class Session {
    public Date       date;
    public String     authToken;
    public PlayerUser user;
    
    public Session(String token, PlayerUser user)
    {
        this.date = new Date();
        this.authToken = token;
        this.user = user;
    }
}

public class SessionController {
    private final int SESSION_DURATION_MS = 6 * 60 * 60 * 1000; // 6 hours
    private final String AUTH_TOKEN_SESSION_KEY = "AUTH_TOKEN_SESSION_KEY";
    private static SessionController sharedInstance = null;
    
    private HashMap<String, Session> sessionMap;
    
    public SessionController()
    {
        this.sessionMap = new HashMap<String, Session>();
    }
    
    public static SessionController sharedInstance()
    {
        if (sharedInstance == null) {
            sharedInstance = new SessionController();
        }
        return sharedInstance;
    }
    
    public String authenticateUser(String username, String password)
    {
        String authToken = null;
        PlayerUser user = PlayerUser.find.where().eq("name", username).findUnique();
        
        if (user != null) {
            String userPasswdHash = CryptoUtil.hashPassword(user.passwordHash, user.passwordSalt);
            String passwdHash = CryptoUtil.hashPassword(password, user.passwordSalt);
            
            if (userPasswdHash.equals(passwdHash)) {
                authToken = Integer.toString((int)Math.random());
                
                Session session = new Session(authToken, user);
                this.sessionMap.put(authToken, session);
            }
        }
        
        return authToken;
    }
    
    public String authenticateUser(String username, String password, Http.Context httpContext)
    {
        String authToken = this.authenticateUser(username, password);
        if (authToken != null) {
            Http.Session session = httpContext.session();
            session.put(AUTH_TOKEN_SESSION_KEY, authToken);
        }
        return authToken;
    }
    
    public PlayerUser getAuthenticatedUser(String authToken)
    {
        PlayerUser authenticatedUser = null;
        
        Session session = this.sessionMap.get(authToken);
        Date now = new Date();
        if (session != null && (now.getTime() - session.date.getTime()) <= SESSION_DURATION_MS) {
            authenticatedUser = session.user;
        }
        
        return authenticatedUser;
    }
    
    public PlayerUser getAuthenticatedUser(Http.Context httpContext)
    {
        PlayerUser authenticatedUser = null;
        String authToken = httpContext.session().get(AUTH_TOKEN_SESSION_KEY);
        if (authToken != null) {
            authenticatedUser = this.getAuthenticatedUser(authToken);
        }
        return authenticatedUser;
    }
    
    public boolean currentHTTPContextIsAuthenticated()
    {
        boolean isAuthenticated = false;
        
        Http.Context currentContext = Http.Context.current();
        Http.Session session = currentContext.session();
        String authToken = session.get(AUTH_TOKEN_SESSION_KEY);
        if (authToken != null) {
            isAuthenticated = (this.getAuthenticatedUser(authToken) != null);
        }
        
        return isAuthenticated;
    }
}
