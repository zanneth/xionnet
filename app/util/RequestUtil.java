package util;

import java.util.*;
import play.mvc.*;

public class RequestUtil {
    public static String getBodyValue(Http.RequestBody body, String key)
    {
        String value = null;
        Map<String, String[]> reqMap = body.asFormUrlEncoded();
        String values[] = reqMap.get(key);
        if (values != null && values.length > 0) {
            value = values[0];
        }
        
        return value;
    }
}
