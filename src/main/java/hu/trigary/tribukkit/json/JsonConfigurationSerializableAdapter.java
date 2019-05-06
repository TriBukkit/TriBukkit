package hu.trigary.tribukkit.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonConfigurationSerializableAdapter implements
		JsonSerializer<ConfigurationSerializable>, JsonDeserializer<ConfigurationSerializable> {
	private static final Type MAP_TYPE = new TypeToken<Map<String, Object>>() {}.getType();
	
	@Override
	public JsonElement serialize(ConfigurationSerializable value, Type type, JsonSerializationContext context) {
		Map<String, Object> map = new HashMap<>(value.serialize());
		map.put(ConfigurationSerialization.SERIALIZED_TYPE_KEY, ConfigurationSerialization.getAlias(value.getClass()));
		return context.serialize(map, MAP_TYPE);
	}
	
	@Override
	public ConfigurationSerializable deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
		try {
			//noinspection unchecked
			return (ConfigurationSerializable) deserialize((Map<String, Object>) context.deserialize(json, MAP_TYPE));
		} catch (RuntimeException e) {
			throw new JsonParseException("Error while deserializing ConfigurationSerializable", e);
		}
	}
	
	
	
	private Object deserialize(Map<String, Object> map) {
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			entry.setValue(deserialize(entry.getValue()));
		}
		
		return map.containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)
				? ConfigurationSerialization.deserializeObject(map)
				: map;
	}
	
	private void deserialize(List<Object> list) {
		for (int i = 0; i < list.size(); i++) {
			list.set(i, deserialize(list.get(i)));
		}
	}
	
	private Object deserialize(Object object) {
		if (object instanceof Map) {
			//noinspection unchecked
			return deserialize((Map<String, Object>) object);
		} else if (object instanceof List) {
			//noinspection unchecked
			deserialize((List<Object>) object);
			return object;
		} else if (object instanceof Number) {
			Number value = (Number) object;
			//noinspection FloatingPointEquality
			if (Double.compare(value.doubleValue(), value.intValue()) == 0) {
				//stupid hack, but better than not having it
				//(GSON treats everything as doubles, but some stuff only accept integers)
				return value.intValue();
			}
		}
		return object;
	}
}
