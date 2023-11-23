package org.platform.vehicle.utils;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.platform.vehicle.exception.JsonDeserializedException;
import org.platform.vehicle.exception.JsonSerializedException;

/**
 * JSON与JAVA对象之间转换的工具类
 *
 * ClassName: JsonBinderUtil <br/>
 * Function: ADD FUNCTION. <br/>
 * Reason: ADD REASON(可选). <br/>
 * date: <br/>
 *
 * @author zengxinyan
 * @version
 * @since JDK 1.8
 */
public final class JsonBinderUtil {

	/**
	 * @Fields instance: 创建输出值不为空的属性到Json字符串的Binder
	 */
	@SuppressWarnings("unused")
	private static JsonBinderUtil instance = new JsonBinderUtil(Include.NON_EMPTY);

	private static ObjectMapper mapper;

	private JsonBinderUtil(Include include) {
		mapper = new ObjectMapper();
		// 设置输出时包含属性的风格
		mapper.setSerializationInclusion(include);
		// 序列化时，忽略空的bean(即沒有任何Field)
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		// 序列化时，忽略在JSON字符串中存在但Java对象实际没有的属性
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

	}

	/**
	 * @Description 对象转换成json字符串。Object可以是POJO，也可以是Collection或数组。 如果对象为Null,
	 *              返回"null"。如果集合为空集合, 返回"[]"
	 * @param object
	 * @return
	 */
	public static String toJson(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch(IOException e) {
			throw new JsonSerializedException("Serialized Object to json string error : " + object, e);
		}
	}

	/**
	 * 将JSON字符串转换成对象T
	 *
	 * @param <T>
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public static <T> T fromJson(String jsonString, Class<T> clazz) {
		if(jsonString == null || "".equals(jsonString.trim())) {
			return null;
		}
		try {
			return mapper.readValue(jsonString, clazz);
		} catch(IOException e) {
			throw new JsonDeserializedException("Deserialized json string error : " + jsonString, e);
		}
	}

	/**
	 * json字符串反序列化List<T>类型的集合。
	 * 这种实现方式List<ParentChapterVo> parentList=JsonBinderUtil.fromJsontolist(jsonStr, new ParentChapterVo());
	 * 这样通过new一个对象传进去是无法转换为一个list的泛型对象的。
	 * 始终以java.util.LinkedHashMap对象返回。此方法存在问题。
	 *
	 * fromJsontolist:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author frank_zeng
	 * @param jsonString
	 * @param t
	 * @return
	 */
	// public static <T> List<T> fromJsontolist(String jsonString,T t) {
	// if (jsonString == null || "".equals(jsonString.trim())) {
	// return null;
	// }
	//
	// try {
	// return mapper.readValue(jsonString, new TypeReference<List<T>>(){} );
	// } catch (IOException e) {
	// throw new JsonDeserializedException(
	// "Deserialized fromJsontolist type json string error : "
	// + jsonString, e);
	// }
	// }

	/**
	 * 获取JavaType对象
	 *
	 * getCollectionType:(这里用一句话描述这个方法的作用). <br/>
	 * (这里描述这个方法适用条件 – 可选).<br/>
	 * (这里描述这个方法的执行流程 – 可选).<br/>
	 * (这里描述这个方法的使用方法 – 可选).<br/>
	 * (这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author zengxinyan
	 * @param collectionClass
	 * @param elementClasses
	 * @return
	 * @since JDK 1.8
	 */
	@SuppressWarnings("deprecation")
	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	/**
	 *
	 * 根据JAVATYPE去转换JSON字符串为LIST对象
	 *
	 * fromJsonToListByType:(这里用一句话描述这个方法的作用). <br/>
	 * (这里描述这个方法适用条件 – 可选).<br/>
	 * (这里描述这个方法的执行流程 – 可选).<br/>
	 * (这里描述这个方法的使用方法 – 可选).<br/>
	 * (这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author zengxinyan
	 * @param jsonString
	 * @param clazz
	 * @return
	 * @since JDK 1.8
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> fromJsonToListByType(String jsonString, Class<T> clazz) {
		if(StringUtils.isEmpty(jsonString)) {
			return null;
		}
		// 如果是ArrayList<YourBean>那么使用ObjectMapper 的getTypeFactory().constructParametricType(collectionClass,
		// elementClasses);
		// 如果是HashMap<String,YourBean>那么 ObjectMapper
		// 的getTypeFactory().constructParametricType(HashMap.class,String.class, YourBean.class);
		try {
			return (List<T>)mapper.readValue(jsonString, getCollectionType(Collection.class, clazz));
		} catch(IOException e) {
			throw new JsonDeserializedException("Deserialized fromJsonToList type json string error : " + jsonString,
					e);
		}

	}

	/**
	 * 利用com.alibaba.fastjson.JSONArray包将json字符串转为list对象
	 *
	 * fromJsonToListByParseArray:(这里用一句话描述这个方法的作用). <br/>
	 * (这里描述这个方法适用条件 – 可选).<br/>
	 * (这里描述这个方法的执行流程 – 可选).<br/>
	 * (这里描述这个方法的使用方法 – 可选).<br/>
	 * (这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author zengxinyan
	 * @param jsonString
	 * @param clazz
	 * @return
	 * @since JDK 1.8
	 */
	public static <T> List<T> fromJsonToListByParseArray(String jsonString, Class<T> clazz) {
		return JSONArray.parseArray(jsonString, clazz);
	}

	/**
	 *
	 * 将json字符串转换为java的list对象
	 *
	 * fromJsontolist:(这里用一句话描述这个方法的作用). <br/>
	 * (这里描述这个方法适用条件 – 可选).<br/>
	 * (这里描述这个方法的执行流程 – 可选).<br/>
	 * (这里描述这个方法的使用方法 – 可选).<br/>
	 * (这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author zengxinyan
	 * @param jsonString
	 * @param typeReference
	 * @return
	 * @since JDK 1.8
	 */
	public static <T> T fromJsontolist(String jsonString, TypeReference<T> typeReference) {
		if(jsonString == null || "".equals(jsonString.trim())) {
			return null;
		}
		try {
			// 这个比较单一，前台换一种日期格式就出错了
			mapper.setDateFormat(new SimpleDateFormat(DateUtil.STANDARD_DATE_TIME_PATTERN));
			return mapper.readValue(jsonString, typeReference);
		} catch(IOException e) {
			e.printStackTrace();
			try {
				mapper.setDateFormat(new SimpleDateFormat(DateUtil.STANDARD_DATE_PATTERN));
				return mapper.readValue(jsonString, typeReference);
			} catch(IOException ee) {
				throw new JsonDeserializedException(
						"Deserialized fromJsonToList type json string error : " + jsonString, ee);
			}
		}
	}

}
