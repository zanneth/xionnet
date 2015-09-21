package models;

import models.PlayerUser;

import java.util.*;
import java.sql.Timestamp;
import javax.persistence.*;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class Score extends Model {
    /** Persistent ID */
    @Id
    @GeneratedValue
    public Long id;
    
    /** Value of the score */
    public long scoreValue;
    
    /** Date the score was created */
    @CreatedTimestamp
    public Timestamp dateCreated;
    
    /** The highscore name to use when displaying this score. Customizable per
        score, and orthogonal from the originating user. */
    public String playerName;
    
    /** The player user that created the highscore */
    @ManyToOne(cascade=CascadeType.ALL)
    public PlayerUser playerUser;
    
    /** Finder for Score class */
    public static Finder<Long, Score> find = new Finder<Long, Score>(Score.class);
    
    /** Formatted date string for date created timestamp */
    public String getDateCreatedString()
    {
        return this.dateCreated.toString();
    }
}
