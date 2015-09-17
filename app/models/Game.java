package models;

import models.PlayerUser;
import models.Score;

import java.util.*;
import java.sql.Timestamp;
import javax.persistence.*;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class Game extends Model {
    /** Persistent ID */
    @Id
    public Long id;
    
    /** Name */
    public String name;
    
    /** Scores for this game */
    @OneToMany(cascade=CascadeType.PERSIST)
    public List<Score> scores;
    
    /** Finder for Game class */
    public static Finder<Long, Game> find = new Finder<Long, Game>(Game.class);
    
    /** Constructor */
    public Game(String name)
    {
        this.name = name;
    }
}
