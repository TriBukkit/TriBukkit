package hu.trigary.tribukkit.json;

import com.google.gson.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class JsonBlockVectorAdapter implements JsonSerializer<BlockVector>, JsonDeserializer<BlockVector> {
	
	@Override
	public JsonElement serialize(BlockVector vector, Type type, JsonSerializationContext context) {
		return new JsonPrimitive(serialize(vector));
	}
	
	@NotNull
	public static String serialize(@NotNull BlockVector vector) {
		return vector.getBlockX() + " " + vector.getBlockY() + " " + vector.getBlockZ();
	}
	
	
	
	@Override
	public BlockVector deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		return deserialize(json.getAsString());
	}
	
	@NotNull
	public static BlockVector deserialize(@NotNull String value) {
		String[] split = StringUtils.split(value, ' ');
		return new BlockVector(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
	}
}
