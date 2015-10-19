package controllers;

import java.util.*;
import play.mvc.*;
import play.libs.F.Promise;
import play.libs.F.RedeemablePromise;
import models.Game;
import util.CryptoUtil;

public class GameObserver {
    private static volatile GameObserver sharedInstance = null;
    private HashMap<Long, ArrayList<RedeemablePromise<Game>>> gameIDToPromiseMap;
    
    public static GameObserver getSharedObserver()
    {
        if (sharedInstance == null) {
            synchronized(GameObserver.class) {
                if (sharedInstance == null) {
                    sharedInstance = new GameObserver();
                }
            }
        }
        return sharedInstance;
    }
    
    private GameObserver()
    {
        this.gameIDToPromiseMap = new HashMap();
    }
    
    public Promise<Game> createChangedGamePromise(Long id)
    {
        RedeemablePromise<Game> promise = RedeemablePromise.empty();
        ArrayList<RedeemablePromise<Game>> promises = this.gameIDToPromiseMap.get(id);
        
        if (promises == null) {
            promises = new ArrayList();
            this.gameIDToPromiseMap.put(id, promises);
        }
        
        promises.add(promise);
        return promise;
    }
    
    public void notifyGameChanged(Long id)
    {
        ArrayList<RedeemablePromise<Game>> promises = this.gameIDToPromiseMap.get(id);
        if (promises != null) {
            Game game = Game.find.byId(id);
            for (RedeemablePromise<Game> promise : promises) {
                promise.success(game);
            }
            promises.clear();
        }
    }
}
