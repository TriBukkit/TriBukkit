package hu.trigary.tribukkit.json;

import com.google.gson.*;
import hu.trigary.tribukkit.data.LazyLocation;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class JsonLocationAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {
	
	@Override
	public JsonElement serialize(Location location, Type type, JsonSerializationContext context) {
		return new JsonPrimitive(serialize(location));
	}
	
	@NotNull
	public static String serialize(@NotNull Location location) {
		return JsonLazyLocationAdapter.serialize(new LazyLocation(location));
	}
	
	
	
	@Override
	public Location deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
		return deserialize(json.getAsString());
	}
	
	@NotNull
	public static Location deserialize(@NotNull String value) {
		LazyLocation lazy = JsonLazyLocationAdapter.deserialize(value);
		Location location = lazy.toLocation();
		if (location.getWorld() == null && lazy.getWorldName() != null) {
			throw new JsonParseException("The world in the deserialized location is not yet loaded");
		}
		return location;
	}
}
