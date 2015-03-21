package cn.cjp.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 实体与json直接相互转换
 */
public class DomainMapper {

	private static ObjectMapper mapper = new ObjectMapper();
	
	static{
		mapper.setSerializationInclusion(Inclusion.ALWAYS);
	}
	/**
	 * 根据类型和字段名获取getter方法
	 * @param clazz 该字段的类型
	 * @param field 字段名
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public static String getSetterMethod(Class clazz,String field)
	{
		//布尔类型的字段，如果以“iss”开头把is去掉
		if(field.toLowerCase().startsWith("iss"))
		{
			field = field.substring(2);
		}
		field = (field.substring(0,1).toUpperCase()+field.substring(1));
		if(clazz.equals(boolean.class))
		{
		}
		return "set" + field;
	}

	/**
	 * 根据类型和字段名获取getter方法
	 * @param clazz 该字段的类型
	 * @param field 字段名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getGetterMethod(Class clazz,String field)
	{
		//布尔类型的字段，如果以“iss”开头把is去掉
		if(field.toLowerCase().startsWith("iss"))
		{
			return field;
		}
		// serialVersionUID
		else if(field.toLowerCase().equals("serialVersionUID"))
		{
			return "getSerialVersionUID";
		}
		/* 首字母大写 */
		field = (field.substring(0,1).toUpperCase()+field.substring(1));
		return "get" + field;
	}
	
	/**
	 * 将json串转换为实体
	 * <br><br>
	 * 测试见 : {@link DomainMapperTest#testMapper()}
	 * @param json 通过函数 {@link DomainMapper#toJson(Object)} 获取的json
	 * @param t 实例化的Entity（比如：new User()）
	 * @return 返回Entity
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws ParseException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * 
	 * @see ObjectMapper#readValue(String, Class)
	 * @see Method#invoke(Object, Object...)
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> T toEntity(String json, T t) throws JsonParseException, JsonMappingException, IOException, ParseException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		return (T) mapper.readValue(json, t.getClass());
	}
	
	public static <T> String toJson(T t) throws IOException
	{
		ByteArrayOutputStream json = new ByteArrayOutputStream();
		
		JsonGenerator jsonGenerator = mapper.getJsonFactory().createJsonGenerator(json, JsonEncoding.UTF8);
		
		jsonGenerator.writeObject(t);
		
		return json.toString();
	}
	
	/**
	 * 将实体转换成Map
	 * @param t 实体
	 * @return 将实体转换为Map的结果
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static <T> Map<String, Object> toMap(T t) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Map<String, Object> entityMap = new HashMap<String, Object>();
		
		Field[] fields = t.getClass().getDeclaredFields();
		
		for(int i=0; i<fields.length; i++)
		{
			Field field = fields[i];
			try {
				Method method = t.getClass().getMethod(getGetterMethod(field.getType(), field.getName()), new Class[0]);
				// 通过getter方法获取字段值
				Object methodResult = method.invoke(t, new Object[0]);
				entityMap.put(field.getName(), methodResult);
			} catch (NoSuchMethodException e) {
				/* 找不到该方法 */
				e.printStackTrace();
			}
		}
		
		return entityMap;
	}
	
	/**
	 * 将 Map 转换为 实体
	 * <br><br>
	 * 测试见 {@link DomainMapperTest#testToMap()}
	 * @param map
	 * @param t 实体
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T toEntity(Map<String, Object> map, T t)
	{

		//获取所有被声明的字段
		Field[] fields = t.getClass().getDeclaredFields();

		for(int i=0; i<fields.length; i++)
		{
			String field = fields[i].getName();	//获取字段名
			Class type = fields[i].getType();	//获取字段类型
			Method method;
			try {
				// 获取setter方法
				method = t.getClass().getMethod(getSetterMethod(type, field), type);
			} catch (NoSuchMethodException e) {
				continue;
			}
			//通过setter方法设置属性值
			if(map.get(field) != null)
			{
				try {
					method.invoke(t, map.get(field));
				} catch (Exception e) {
				}
			}
		}
		return t;
	}
	

	
	
	
	
	
	
}
