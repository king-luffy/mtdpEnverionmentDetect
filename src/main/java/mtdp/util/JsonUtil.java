package mtdp.util;

import com.alibaba.fastjson.JSON;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by king_luffy on 2017/9/7.
 */
public class JsonUtil {

    /**
     * 转换Object对象到Json
     * Translate Object to Json String
     * @param obj
     * @return
     * @throws IOException
     */
    public static String bean2Json(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter sw = new StringWriter();
        JsonGenerator gen = new JsonFactory().createJsonGenerator(sw);
        mapper.writeValue(gen, obj);
        gen.close();
        return sw.toString();
    }

    /**
     * Read Json Data and Translate into Object
     * 读取json数据并转换为内存对象
     * @param jsonStr
     * @param objClass
     * @param <T>
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <T> T json2Bean(String jsonStr, Class<T> objClass)
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonStr, objClass);
    }

    /**
     * Translate into Json String
     * 转换为 Json String
     * @param obj
     * @param prettyFormat
     * @return
     */
    public static String getJsonString(Object obj,boolean prettyFormat){
        return JSON.toJSONString(obj,prettyFormat);
    }

    /**
     * Translate into Json String
     * 转换为 Json String
     * @param obj
     * @return
     */
    public static String getJsonString(Object obj){
        return JSON.toJSONString(obj);
    }
}
