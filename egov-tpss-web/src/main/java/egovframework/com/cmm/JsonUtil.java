package egovframework.com.cmm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
 
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
	
    /**
     * Map을 json으로 변환한다.
     *
     * @param map Map<String, Object>.
     * @return JSONObject.
     */
    @SuppressWarnings("unchecked")
	public static JSONObject getJsonStringFromMap( Map<String, Object> map )
    {
        JSONObject jsonObject = new JSONObject();
        try {
            for( Map.Entry<String, Object> entry : map.entrySet() ) {
                String key = entry.getKey();
                Object value = entry.getValue();
                jsonObject.put(key, value);
            }
        } catch (JSONException e) {
        	LOGGER.error("JSONException : getJsonStringFromMap error");
        } catch (Exception ex) {
        	LOGGER.error("Exception : getJsonStringFromMap error");
        }
        
        return jsonObject;
    }
    
    /**
     * List<Map>을 jsonArray로 변환한다.
     *
     * @param list List<Map<String, Object>>.
     * @return JSONArray.
     */
    @SuppressWarnings("unchecked")
	public static JSONArray getJsonArrayFromList( List<Map<String, Object>> list )
    {
        JSONArray jsonArray = new JSONArray();
        for( Map<String, Object> map : list ) {
        	jsonArray.put( getJsonStringFromMap( map ) );
        }        	
        
        return jsonArray;
    }
    
    /**
     * List<Map>을 jsonString으로 변환한다.
     *
     * @param list List<Map<String, Object>>.
     * @return String.
     */
    public static String getJsonStringFromList( List<Map<String, Object>> list )
    {
        JSONArray jsonArray = getJsonArrayFromList( list );
        return jsonArray.toString();
    }
 
    /**
     * JsonObject를 Map<String, String>으로 변환한다.
     *
     * @param jsonObj JSONObject.
     * @return Map<String, Object>.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMapFromJsonObject( JSONObject jsonObj )
    {
        Map<String, Object> map = null;
        
        try {
            
            map = new ObjectMapper().readValue(jsonObj.toString(), Map.class) ;
            
        } catch (JsonParseException e) {
            LOGGER.debug("ERROR");
        } catch (JsonMappingException e) {
        	LOGGER.debug("ERROR");
        } catch (IOException e) {
        	LOGGER.debug("ERROR");
        }
 
        return map;
    }
 
    /**
     * JsonArray를 List<Map<String, String>>으로 변환한다.
     *
     * @param jsonArray JSONArray.
     * @return List<Map<String, Object>>.
     */
    public static List<Map<String, Object>> getListMapFromJsonArray( JSONArray jsonArray )
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        
        if( jsonArray != null )
        {
        	try {
                int jsonSize = jsonArray.length();
                for( int i = 0; i < jsonSize; i++ )
                {
                    Map<String, Object> map = JsonUtil.getMapFromJsonObject( ( JSONObject ) jsonArray.getJSONObject(i) );
                    list.add( map );
                }
        	} catch (JSONException e) {
            	LOGGER.error("JSONException : getListMapFromJsonArray error");
            } catch (Exception ex) {
            	LOGGER.error("Exception : getListMapFromJsonArray error");
            }
        }
        
        return list;
    }
}