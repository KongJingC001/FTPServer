package kj.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kj.entity.Doc;
import java.util.List;

public class JsonUtil {

    private JsonUtil() {}


    /**
     * 将列表转换为json
     * @param list 指定的List对象
     * @return 返回json字符串
     */
    public static String ListToJson(List<Doc> list)  {
        ObjectMapper om = new ObjectMapper();
        String json = null;
        try {
            json =  om.writerWithDefaultPrettyPrinter().writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

}
