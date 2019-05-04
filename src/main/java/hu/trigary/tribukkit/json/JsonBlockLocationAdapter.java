package hu.trigary.tribukkit.json;

import com.google.gson.*;
import hu.trigary.tribukkit.data.BlockLocation;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class JsonBlockLocationAdapter implements JsonSerializer<BlockLocation>, JsonDeserializer<BlockLocation> {
	
	@Override
	public JsonElement serialize(BlockLocation location, Type type, JsonSerializationContext context) {
		return new JsonPrimitive(serialize(location));
	}
	
	@NotNull
	public static String serialize(@NotNull BlockLocation location) {
		String temp = location.getX() + " " + location.getY() + " " + location.getZ();
		return location.getWorldName() == null ? temp : location.getWorldName() + " " + temp;
	}
	
	
	
	@Override
	public BlockLocation deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		return deserialize(json.getAsString());
	}
	
	@NotNull
	public static BlockLocation deserialize(@NotNull String value) {
		String[] split = StringUtils.split(value, ' ');
		//noinspection IfMayBeConditional
		if (split.length == 3) { //X,Y,Z
			return new BlockLocation(null, Integer.parseInt(split[0]),
					Integer.parseInt(split[1]), Integer.parseInt(split[2]));
		} else { //World,X,Y,Z
			return new BlockLocation(split[0], Integer.parseInt(split[1]),
					Integer.parseInt(split[2]), Integer.parseInt(split[3]));
		}
	}
}
