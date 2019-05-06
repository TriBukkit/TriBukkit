package hu.trigary.tribukkit.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import hu.trigary.tribukkit.TriJavaPlugin;
import hu.trigary.tribukkit.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonItemSimpleAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
	private static final Type MAP_TYPE = new TypeToken<Map<String, Object>>() {}.getType();
	
	@Override
	public JsonElement serialize(ItemStack item, Type type, JsonSerializationContext context) {
		Map<String, Object> serialized = serialize(item);
		return context.serialize(serialized, MAP_TYPE);
	}
	
	@NotNull
	public static Map<String, Object> serialize(@NotNull ItemStack item) {
		ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : null;
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", item.getType().name());
		
		if (item.getAmount() != 1) {
			map.put("amount", item.getAmount());
		}
		
		if (meta instanceof Damageable) {
			Damageable damageable = (Damageable) meta;
			if (damageable.hasDamage()) {
				map.put("damage", damageable.getDamage());
			}
		}
		
		if (meta != null) {
			if (meta.hasDisplayName()) {
				map.put("name", meta.getDisplayName());
			}
			if (meta.hasLore()) {
				map.put("lore", meta.getLore());
			}
			if (meta.hasEnchants()) {
				map.put("glowing", true);
			}
		}
		
		return map;
	}
	
	
	
	@Override
	public ItemStack deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
		return deserialize(context.deserialize(json, MAP_TYPE));
	}
	
	@NotNull
	public static ItemStack deserialize(@NotNull Map<String, Object> value) {
		ItemBuilder builder = new ItemBuilder(getMaterial((String) value.get("type")));
		
		Object amount = value.get("amount");
		if (amount != null) {
			builder.setAmount(Integer.parseInt((String) amount));
		}
		
		Object damage = value.get("damage");
		if (damage != null) {
			builder.setDamage(Integer.parseInt((String) damage));
		}
		
		Object name = value.get("name");
		if (name != null) {
			builder.setName(ChatColor.translateAlternateColorCodes('&', (String) name));
		}
		
		Object lore = value.get("lore");
		if (lore != null) {
			//noinspection unchecked
			List<String> loreList = (List<String>) lore;
			loreList.replaceAll(line -> ChatColor.translateAlternateColorCodes('&', line));
			builder.setLore(loreList);
		}
		
		Object glowing = value.get("glowing");
		if (glowing != null && (boolean) glowing) {
			builder.makeGlowing();
		}
		
		return builder.build();
	}
	
	private static Material getMaterial(String name) {
		Material result = Material.getMaterial(name);
		if (result == null) {
			TriJavaPlugin.getInstance().getLogger().severe("Unable to find item type named: " + name);
		}
		return result;
	}
}
