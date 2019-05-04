package hu.trigary.tribukkit.json;

import com.google.gson.*;
import hu.trigary.tribukkit.TriJavaPlugin;
import hu.trigary.tribukkit.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class JsonItemStringAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
	
	@Override
	public JsonElement serialize(ItemStack item, Type type, JsonSerializationContext context) {
		return new JsonPrimitive(serialize(item));
	}
	
	@NotNull
	public static String serialize(@NotNull ItemStack item) {
		String temp = item.getType().name();
		
		ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : null;
		if (meta instanceof Damageable) {
			Damageable damageable = (Damageable) meta;
			if (damageable.hasDamage()) {
				temp += ":" + damageable.getDamage();
			}
		}
		
		if (item.getAmount() != 1) {
			temp += " " + item.getAmount();
		}
		
		return temp;
	}
	
	
	
	@Override
	public ItemStack deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		return deserialize(json.getAsString());
	}
	
	@NotNull
	public static ItemStack deserialize(@NotNull String value) {
		int space = value.lastIndexOf(' ');
		int colon = value.lastIndexOf(':');
		
		if (space == -1 && colon == -1) {
			return new ItemStack(getMaterial(value));
		} else if (space != -1 && colon == -1) {
			return new ItemStack(getMaterial(value.substring(0, space)),
					Integer.parseInt(value.substring(space + 1)));
		} else if (space == -1) {
			return new ItemBuilder(getMaterial(value.substring(0, colon)))
					.setDamage(Integer.parseInt(value.substring(colon + 1)))
					.build();
		} else {
			return new ItemBuilder(getMaterial(value.substring(0, colon)))
					.setDamage(Integer.parseInt(value.substring(colon + 1, space)))
					.setAmount(Integer.parseInt(value.substring(space + 1)))
					.build();
		}
	}
	
	private static Material getMaterial(String name) {
		Material result = Material.getMaterial(name);
		if (result == null) {
			TriJavaPlugin.getInstance().getLogger().severe("Unable to find item type named: " + name);
		}
		return result;
	}
}
