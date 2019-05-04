package hu.trigary.tribukkit.json;

import com.google.gson.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class JsonVectorAdapter implements JsonSerializer<Vector>, JsonDeserializer<Vector> {
	
	@Override
	public JsonElement serialize(Vector vector, Type type, JsonSerializationContext context) {
		return new JsonPrimitive(serialize(vector));
	}
	
	@NotNull
	public static String serialize(@NotNull Vector vector) {
		return vector.getX() + " " + vector.getY() + " " + vector.getZ();
	}
	
	
	
	@Override
	public Vector deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		return deserialize(json.getAsString());
	}
	
	@NotNull
	public static Vector deserialize(@NotNull String value) {
		String[] split = StringUtils.split(value, ' ');
		return new Vector(Double.parseDouble(split[0]), Double.parseDouble(split[1]),
				Double.parseDouble(split[2]));
	}
}
