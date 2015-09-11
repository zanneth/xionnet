package models;

import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class Score extends Model {
    @Id
    public Long id;
    public long scoreValue;
    
    public static Finder<Long, Score> find = 
        new Finder<Long, Score>(Score.class);
}
