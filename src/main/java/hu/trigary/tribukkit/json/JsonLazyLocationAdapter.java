package hu.trigary.tribukkit.json;

import com.google.gson.*;
import hu.trigary.tribukkit.data.LazyLocation;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class JsonLazyLocationAdapter implements JsonSerializer<LazyLocation>, JsonDeserializer<LazyLocation> {
	
	@Override
	public JsonElement serialize(LazyLocation location, Type type, JsonSerializationContext context) {
		return new JsonPrimitive(serialize(location));
	}
	
	@NotNull
	public static String serialize(@NotNull LazyLocation location) {
		String temp = location.getX() + " " + location.getY() + " " + location.getZ();
		if (location.getYaw() != 0 || location.getPitch() != 0) {
			temp += " " + location.getYaw() + " " + location.getPitch();
		}
		return location.getWorldName() == null ? temp : location.getWorldName() + " " + temp;
	}
	
	
	
	@Override
	public LazyLocation deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
		return deserialize(json.getAsString());
	}
	
	@NotNull
	public static LazyLocation deserialize(@NotNull String value) {
		String[] split = StringUtils.split(value, ' ');
		
		String world = null;
		int offset = 0;
		if (split.length == 4 || split.length == 6) { //World,X,Y,Z || World,X,Y,Z,Yaw,Pitch
			world = split[offset++];
		}
		
		float yaw = 0;
		float pitch = 0;
		if (split.length > 4) { //X,Y,Z,Yaw,Pitch || World,X,Y,Z,Yaw,Pitch
			yaw = Float.parseFloat(split[offset + 3]);
			pitch = Float.parseFloat(split[offset + 4]);
		}
		
		return new LazyLocation(world, Double.parseDouble(split[offset]), Double.parseDouble(split[offset + 1]),
				Double.parseDouble(split[offset + 2]), yaw, pitch);
	}
}
