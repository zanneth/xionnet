package models;

import java.util.*;
import java.sql.Timestamp;
import javax.persistence.*;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.format.*;
import play.data.validation.*;

import util.CryptoUtil;

@Entity
public class PlayerUser extends Model {
    /** Persistent ID */
    @Id
    @GeneratedValue
    public Long id;
    
    /** Name */
    public String name;
    
    /** Password hash */
    @JsonIgnore
    public String passwordHash;
    
    /** Password salt */
    @JsonIgnore
    public String passwordSalt;
    
    /** Whether the user is allowed to verify highscores */
    public boolean canVerifyScores = false;
    
    /** Whether the user is an administrator (able to create new users and edit
        data). */
    public boolean isAdministrator = false;
    
    /** Finder for PlayerUser class */
    public static Finder<Long, PlayerUser> find = new Finder<Long, PlayerUser>(PlayerUser.class);
    
    /** Constructor */
    public PlayerUser(String name, String password)
    {
        this.name = name;
        
        String salt = CryptoUtil.generateSalt();
        String passwdHash = CryptoUtil.hashPassword(password, salt);
        
        this.passwordHash = passwdHash;
        this.passwordSalt = salt;
    }
    
    /** Returns the administrator account already exists in the
        persistence store. Otherwise null. */
    public static PlayerUser getAdministratorUser()
    {
        return PlayerUser.find.where().eq("isAdministrator", true).findUnique();
    }
}
