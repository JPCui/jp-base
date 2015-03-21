/**
 * 
 */
/**
 * @author Sucre Cui
 *
 */
package cn.cjp.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil
{
	/**
	 * 把字符串数组转换为json对象
	 * @param <T>
	 * @param t 可以是任意类型（int、String）
	 * @return
	 */
	@SafeVarargs
	public static <T> String toJson(T... t)
	{
		ObjectMapper objectMapper = new ObjectMapper();
		String json = null;
		for(int i=0; i<t.length; i++)
		{
			try {
				json = objectMapper.writeValueAsString( t[0] );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return json;
	}
	
	/**
	 * 将json转换到map里
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonToMap(String json)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			map = objectMapper.readValue(json, Map.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 把List转换为json
	 * @param <T>
	 * @param list
	 * @return json
	 */
	public static <T> String listToJson(List<T> list)
	{
		Map<String, Object>maps = new HashMap<String, Object>();
		
		for(int i=0; i<list.size(); i++)
		{
			maps.put(""+i,  list.get(i));
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "";
		try {
			json = objectMapper.writeValueAsString(maps);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 反序列化
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> jsonToList(String json)
	{
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		try {
			List<T>list = mapper.readValue(json, List.class);
			return list;
		} catch (IOException e) {
			System.err.println("json无法转换为List");
//			e.printStackTrace();
		}
		return null;
	}
}