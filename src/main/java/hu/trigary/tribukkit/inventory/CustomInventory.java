package hu.trigary.tribukkit.inventory;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CustomInventory implements InventoryHolder {
	private Inventory inventory;
	
	protected CustomInventory() {
		Validate.isTrue(CustomInventoryListener.enabled,
				"Custom inventories are not enabled, call TriJavaPlugin#enableCustomInventories");
	}
	
	protected void initialize(int size, @NotNull String title) {
		inventory = Bukkit.createInventory(this, size, title);
	}
	
	
	
	public void onOpen(@NotNull Player player, @NotNull InventoryOpenEvent event) { }
	
	public abstract void onClick(@NotNull Player player, @NotNull InventoryClickEvent event);
	
	public abstract void onDrag(@NotNull Player player, @NotNull InventoryDragEvent event);
	
	public void onClose(@NotNull Player player, @NotNull InventoryCloseEvent event) { }
	
	
	
	@Override
	@NotNull
	public Inventory getInventory() {
		return inventory;
	}
	
	public void open(@NotNull Player player) {
		player.openInventory(inventory);
	}
	
	
	
	@Contract(pure = true)
	protected static boolean isNull(@Nullable ItemStack item) {
		return item == null || item.getType() == Material.AIR;
	}
	
	
	
	//TODO builder pattern for filling this with items?
	//also can register on click actions like that
	//https://www.spigotmc.org/resources/api-menubuilder.12995/
}
