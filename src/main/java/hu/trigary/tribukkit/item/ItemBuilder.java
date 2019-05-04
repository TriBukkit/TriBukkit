package hu.trigary.tribukkit.item;

import com.google.common.collect.Multimap;
import hu.trigary.tribukkit.TriJavaPlugin;
import hu.trigary.tribukkit.persistent.PersistentUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ItemBuilder {
	private final ItemStack item;
	private final ItemMeta meta;
	
	public ItemBuilder(@NotNull ItemStack item) {
		this.item = item.clone();
		meta = item.getItemMeta();
		Validate.notNull(meta, "Item (type) must have ItemMeta");
	}
	
	public ItemBuilder(@NotNull Material material) {
		this(new ItemStack(material));
	}
	
	
	
	@NotNull
	@Contract(pure = true)
	public ItemStack build() {
		item.setItemMeta(meta);
		return item.clone();
	}
	
	
	
	@NotNull
	public ItemBuilder setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}
	
	@NotNull
	public ItemBuilder setDamage(int damage) {
		((Damageable) meta).setDamage(damage);
		return this;
	}
	
	@NotNull
	public ItemBuilder setName(@Nullable String name) {
		meta.setDisplayName(name);
		return this;
	}
	
	@NotNull
	public ItemBuilder setUnbreakable(boolean unbreakable) {
		meta.setUnbreakable(unbreakable);
		return this;
	}
	
	
	
	@NotNull
	public ItemBuilder setLore(@Nullable List<String> lore) {
		meta.setLore(lore);
		return this;
	}
	
	@NotNull
	public ItemBuilder setLore(@Nullable String... lore) {
		meta.setLore(lore == null ? null : Arrays.asList(lore));
		return this;
	}
	
	@NotNull
	public ItemBuilder clearLore() {
		meta.setLore(null);
		return this;
	}
	
	
	
	@NotNull
	public <T, Z> ItemBuilder setTag(@NotNull NamespacedKey key,
			@NotNull PersistentDataType<T, Z> type, @NotNull Z value) {
		meta.getPersistentDataContainer().set(key, type, value);
		return this;
	}
	
	@NotNull
	public <T, Z> ItemBuilder setTag(@NotNull JavaPlugin plugin, @NotNull String key,
			@NotNull PersistentDataType<T, Z> type, @NotNull Z value) {
		return setTag(new NamespacedKey(plugin, key), type, value);
	}
	
	@NotNull
	public <T, Z> ItemBuilder setTag(@NotNull String key,
			@NotNull PersistentDataType<T, Z> type, @NotNull Z value) {
		return setTag(TriJavaPlugin.getInstance(), key, type, value);
	}
	
	@NotNull
	public <Z> ItemBuilder setTag(@NotNull NamespacedKey key, @NotNull Z supportedValue) {
		//noinspection unchecked
		meta.getPersistentDataContainer().set(key, (PersistentDataType<?, Z>)
				PersistentUtils.forceGetSupportedType(supportedValue.getClass()), supportedValue);
		return this;
	}
	
	@NotNull
	public <Z> ItemBuilder setTag(@NotNull JavaPlugin plugin,
			@NotNull String key, @NotNull Z supportedValue) {
		return setTag(new NamespacedKey(plugin, key), supportedValue);
	}
	
	@NotNull
	public <Z> ItemBuilder setTag(@NotNull String key, @NotNull Z supportedValue) {
		return setTag(TriJavaPlugin.getInstance(), key, supportedValue);
	}
	
	@NotNull
	public ItemBuilder removeTag(@NotNull NamespacedKey key) {
		meta.getPersistentDataContainer().remove(key);
		return this;
	}
	
	@NotNull
	public ItemBuilder removeTag(@NotNull JavaPlugin plugin, @NotNull String key) {
		return removeTag(new NamespacedKey(plugin, key));
	}
	
	@NotNull
	public ItemBuilder removeTag(@NotNull String key) {
		return removeTag(TriJavaPlugin.getInstance(), key);
	}
	
	
	
	@NotNull
	public ItemBuilder setAttribute(@Nullable Multimap<Attribute, AttributeModifier> modifiers) {
		meta.setAttributeModifiers(modifiers);
		return this;
	}
	
	@NotNull
	public ItemBuilder addAttribute(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
		meta.addAttributeModifier(attribute, modifier);
		return this;
	}
	
	@NotNull
	public ItemBuilder removeAttribute(@NotNull EquipmentSlot slot) {
		meta.removeAttributeModifier(slot);
		return this;
	}
	
	@NotNull
	public ItemBuilder removeAttribute(@NotNull Attribute attribute) {
		meta.removeAttributeModifier(attribute);
		return this;
	}
	
	@NotNull
	public ItemBuilder removeAttribute(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
		meta.removeAttributeModifier(attribute, modifier);
		return this;
	}
	
	
	
	@NotNull
	public ItemBuilder addEnchantment(@NotNull Enchantment enchantment, int level) {
		meta.addEnchant(enchantment, level, true);
		return this;
	}
	
	@NotNull
	public ItemBuilder removeEnchantment(@NotNull Enchantment enchantment) {
		meta.removeEnchant(enchantment);
		return this;
	}
	
	
	
	@NotNull
	public ItemBuilder addFlags(@NotNull ItemFlag... flags) {
		meta.addItemFlags(flags);
		return this;
	}
	
	@NotNull
	public ItemBuilder removeFlags(@NotNull ItemFlag... flags) {
		meta.removeItemFlags(flags);
		return this;
	}
	
	
	
	@NotNull
	public ItemBuilder makeGlowing() {
		//TODO docs: call after adding enchantments
		if (!meta.hasEnchants()) {
			meta.addEnchant(Enchantment.DURABILITY, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		return this;
	}
	
	@NotNull
	public ItemBuilder addAllFlags() {
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
				ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
		return this;
	}
	
	@NotNull
	public <T extends ItemMeta> ItemBuilder applyCustomMeta(@NotNull Class<T> type, @NotNull Consumer<T> applier) {
		applier.accept(type.cast(meta));
		return this;
	}
}
