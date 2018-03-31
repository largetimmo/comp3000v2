package util;

import com.alibaba.fastjson.JSONObject;

public class JSONHelper {
    public static JSONObject constructJson( Object action, Object target, Object data){
        JSONObject result = new JSONObject();
        result.put("ACTION",action);
        result.put("TARGET",target);
        result.put("DATA",data);
        return result;
    }

}
