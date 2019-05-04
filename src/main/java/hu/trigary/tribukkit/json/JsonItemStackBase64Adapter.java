package hu.trigary.tribukkit.json;

import com.google.gson.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @noinspection resource, IOResourceOpenedButNotSafelyClosed
 */
public class JsonItemStackBase64Adapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
	
	@Override
	public JsonElement serialize(ItemStack item, Type type, JsonSerializationContext context) {
		try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
			new BukkitObjectOutputStream(stream).writeObject(item);
			return new JsonPrimitive(Base64Coder.encodeLines(stream.toByteArray()));
		} catch (IOException | RuntimeException e) {
			throw new JsonParseException(e);
		}
	}
	
	@Override
	public ItemStack deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		try (ByteArrayInputStream stream = new ByteArrayInputStream(
				Base64Coder.decodeLines(json.getAsString()))) {
			return (ItemStack) new BukkitObjectInputStream(stream).readObject();
		} catch (IOException | ClassNotFoundException | RuntimeException e) {
			throw new JsonParseException(e);
		}
	}
}
