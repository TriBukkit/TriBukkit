package hu.trigary.tribukkit.persistent;

import hu.trigary.tribukkit.TriJavaPlugin;
import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

public abstract class PersistentUtils {
	private static final Map<Class<?>, PersistentDataType<?, ?>> SUPPORTED_TYPE_MAP = new IdentityHashMap<>();
	
	static {
		addSupportedType(PersistentDataType.BYTE);
		addSupportedType(PersistentDataType.BYTE_ARRAY);
		addSupportedType(PersistentDataType.DOUBLE);
		addSupportedType(PersistentDataType.FLOAT);
		addSupportedType(PersistentDataType.INTEGER);
		addSupportedType(PersistentDataType.INTEGER_ARRAY);
		addSupportedType(PersistentDataType.LONG);
		addSupportedType(PersistentDataType.LONG_ARRAY);
		addSupportedType(PersistentDataType.SHORT);
		addSupportedType(PersistentDataType.STRING);
		addSupportedType(PersistentDataType.TAG_CONTAINER);
		
		addSupportedType(StringArrayDataType.INSTANCE);
		addSupportedType(UUIDDataType.INSTANCE);
		addSupportedType(UUIDArrayDataType.INSTANCE);
	}
	
	public static void addSupportedType(@NotNull PersistentDataType<?, ?> type) {
		SUPPORTED_TYPE_MAP.put(type.getComplexType(), type);
	}
	
	@Nullable
	@Contract(pure = true)
	public static <Z> PersistentDataType<?, Z> getSupportedType(@NotNull Class<Z> supportedClass) {
		//noinspection unchecked
		return (PersistentDataType<?, Z>) SUPPORTED_TYPE_MAP.get(supportedClass);
	}
	
	@NotNull
	@Contract(pure = true)
	public static <Z> PersistentDataType<?, Z> forceGetSupportedType(@NotNull Class<Z> supportedClass) {
		PersistentDataType<?, Z> type = getSupportedType(supportedClass);
		Validate.notNull(type, "Persistent data type value class not supported: "
				+ supportedClass.getSimpleName());
		return type;
	}
	
	
	
	@Nullable
	@Contract(pure = true)
	public static <Z> Z get(@NotNull PersistentDataHolder holder,
			@NotNull NamespacedKey key, @NotNull PersistentDataType<?, Z> type) {
		return holder.getPersistentDataContainer().get(key, type);
	}
	
	@Nullable
	@Contract(pure = true)
	public static <Z> Z get(@NotNull PersistentDataHolder holder, @NotNull JavaPlugin plugin,
			@NotNull String key, @NotNull PersistentDataType<?, Z> type) {
		return get(holder, new NamespacedKey(plugin, key), type);
	}
	
	@Nullable
	@Contract(pure = true)
	public static <Z> Z get(@NotNull PersistentDataHolder holder,
			@NotNull String key, @NotNull PersistentDataType<?, Z> type) {
		return get(holder, new NamespacedKey(TriJavaPlugin.getInstance(), key), type);
	}
	
	@Nullable
	@Contract(pure = true)
	public static <Z> Z get(@NotNull ItemStack item,
			@NotNull NamespacedKey key, @NotNull PersistentDataType<?, Z> type) {
		//noinspection ConstantConditions
		return item.hasItemMeta() ? item.getItemMeta().getPersistentDataContainer()
				.get(key, type) : null;
	}
	
	@Nullable
	@Contract(pure = true)
	public static <Z> Z get(@NotNull ItemStack item, @NotNull JavaPlugin plugin,
			@NotNull String key, @NotNull PersistentDataType<?, Z> type) {
		return get(item, new NamespacedKey(plugin, key), type);
	}
	
	@Nullable
	@Contract(pure = true)
	public static <Z> Z get(@NotNull ItemStack item,
			@NotNull String key, @NotNull PersistentDataType<?, Z> type) {
		return get(item, new NamespacedKey(TriJavaPlugin.getInstance(), key), type);
	}
	
	
	
	@Nullable
	@Contract(pure = true)
	public static <Z> Z get(@NotNull PersistentDataHolder holder,
			@NotNull NamespacedKey key, @NotNull Class<Z> supportedClass) {
		return holder.getPersistentDataContainer().get(key, forceGetSupportedType(supportedClass));
	}
	
	@Nullable
	@Contract(pure = true)
	public static <Z> Z get(@NotNull PersistentDataHolder holder, @NotNull JavaPlugin plugin,
			@NotNull String key, @NotNull Class<Z> supportedClass) {
		return get(holder, new NamespacedKey(plugin, key), supportedClass);
	}
	
	@Nullable
	@Contract(pure = true)
	public static <Z> Z get(@NotNull PersistentDataHolder holder,
			@NotNull String key, @NotNull Class<Z> supportedClass) {
		return get(holder, new NamespacedKey(TriJavaPlugin.getInstance(), key), supportedClass);
	}
	
	@Nullable
	@Contract(pure = true)
	public static <Z> Z get(@NotNull ItemStack item,
			@NotNull NamespacedKey key, @NotNull Class<Z> supportedClass) {
		//noinspection ConstantConditions
		return item.hasItemMeta() ? get(item.getItemMeta(), key, supportedClass) : null;
	}
	
	@Nullable
	@Contract(pure = true)
	public static <Z> Z get(@NotNull ItemStack item, @NotNull JavaPlugin plugin,
			@NotNull String key, @NotNull Class<Z> supportedClass) {
		return get(item, new NamespacedKey(plugin, key), supportedClass);
	}
	
	@Nullable
	@Contract(pure = true)
	public static <Z> Z get(@NotNull ItemStack item,
			@NotNull String key, @NotNull Class<Z> supportedClass) {
		return get(item, new NamespacedKey(TriJavaPlugin.getInstance(), key), supportedClass);
	}
	
	
	
	public static <Z> void set(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key,
			@NotNull PersistentDataType<?, Z> type, @NotNull Z value) {
		holder.getPersistentDataContainer().set(key, type, value);
	}
	
	public static <Z> void set(@NotNull PersistentDataHolder holder, @NotNull JavaPlugin plugin,
			@NotNull String key, @NotNull PersistentDataType<?, Z> type, @NotNull Z value) {
		set(holder, new NamespacedKey(plugin, key), type, value);
	}
	
	public static <Z> void set(@NotNull PersistentDataHolder holder, @NotNull String key,
			@NotNull PersistentDataType<?, Z> type, @NotNull Z value) {
		set(holder, new NamespacedKey(TriJavaPlugin.getInstance(), key), type, value);
	}
	
	public static <Z> void set(@NotNull ItemStack item, @NotNull NamespacedKey key,
			@NotNull PersistentDataType<?, Z> type, @NotNull Z value) {
		ItemMeta meta = item.getItemMeta();
		Validate.notNull(meta, "Item must have ItemMeta");
		set(meta, key, type, value);
	}
	
	public static <Z> void set(@NotNull ItemStack item, @NotNull JavaPlugin plugin,
			@NotNull String key, @NotNull PersistentDataType<?, Z> type, @NotNull Z value) {
		set(item, new NamespacedKey(plugin, key), type, value);
	}
	
	public static <Z> void set(@NotNull ItemStack item, @NotNull String key,
			@NotNull PersistentDataType<?, Z> type, @NotNull Z value) {
		set(item, new NamespacedKey(TriJavaPlugin.getInstance(), key), type, value);
	}
	
	
	
	public static <Z> void set(@NotNull PersistentDataHolder holder,
			@NotNull NamespacedKey key, @NotNull Z supportedValue) {
		//noinspection unchecked
		holder.getPersistentDataContainer().set(key, (PersistentDataType<?, Z>)
				forceGetSupportedType(supportedValue.getClass()), supportedValue);
	}
	
	public static <Z> void set(@NotNull PersistentDataHolder holder, @NotNull JavaPlugin plugin,
			@NotNull String key, @NotNull Z supportedValue) {
		set(holder, new NamespacedKey(plugin, key), supportedValue);
	}
	
	public static <Z> void set(@NotNull PersistentDataHolder holder,
			@NotNull String key, @NotNull Z supportedValue) {
		set(holder, new NamespacedKey(TriJavaPlugin.getInstance(), key), supportedValue);
	}
	
	public static <Z> void set(@NotNull ItemStack item,
			@NotNull NamespacedKey key, @NotNull Z supportedValue) {
		ItemMeta meta = item.getItemMeta();
		Validate.notNull(meta, "Item must have ItemMeta");
		set(meta, key, supportedValue);
	}
	
	public static <Z> void set(@NotNull ItemStack item, @NotNull JavaPlugin plugin,
			@NotNull String key, @NotNull Z supportedValue) {
		set(item, new NamespacedKey(plugin, key), supportedValue);
	}
	
	public static <Z> void set(@NotNull ItemStack item,
			@NotNull String key, @NotNull Z supportedValue) {
		set(item, new NamespacedKey(TriJavaPlugin.getInstance(), key), supportedValue);
	}
	
	
	
	public static void remove(@NotNull PersistentDataHolder holder, @NotNull NamespacedKey key) {
		holder.getPersistentDataContainer().remove(key);
	}
	
	public static void remove(@NotNull PersistentDataHolder holder,
			@NotNull JavaPlugin plugin, @NotNull String key) {
		remove(holder, new NamespacedKey(plugin, key));
	}
	
	public static void remove(@NotNull PersistentDataHolder holder, @NotNull String key) {
		remove(holder, new NamespacedKey(TriJavaPlugin.getInstance(), key));
	}
	
	public static void remove(@NotNull ItemStack item, @NotNull NamespacedKey key) {
		if (item.hasItemMeta()) {
			//noinspection ConstantConditions
			remove(item.getItemMeta(), key);
		}
	}
	
	public static void remove(@NotNull ItemStack item,
			@NotNull JavaPlugin plugin, @NotNull String key) {
		remove(item, new NamespacedKey(plugin, key));
	}
	
	public static void remove(@NotNull ItemStack item, @NotNull String key) {
		remove(item, new NamespacedKey(TriJavaPlugin.getInstance(), key));
	}
}
