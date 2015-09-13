package models;

import java.util.*;
import java.sql.Timestamp;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

import util.CryptoUtil;

@Entity
public class PlayerUser extends Model {
    /** Persistent ID */
    @Id
    public Long id;
    
    /** Name */
    public String name;
    
    /** Password hash */
    public String passwordHash;
    
    /** Password salt */
    public String passwordSalt;
    
    /** Whether the user is allowed to verify highscores */
    public boolean canVerifyScores = false;
    
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
}