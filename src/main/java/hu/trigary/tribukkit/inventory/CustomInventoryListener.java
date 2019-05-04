package hu.trigary.tribukkit.inventory;

import hu.trigary.tribukkit.TriJavaPlugin;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.InventoryHolder;

public class CustomInventoryListener implements Listener {
	static boolean enabled;
	
	CustomInventoryListener() {
		Validate.isTrue(!enabled, "CustomInventoryListener is already registered");
		enabled = true;
	}
	
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDisable(PluginDisableEvent event) { //TODO does this work?
		if (event.getPlugin() == TriJavaPlugin.getInstance()) {
			Bukkit.getOnlinePlayers().stream()
					.filter(player -> player.getOpenInventory().getTopInventory()
							.getHolder() instanceof CustomInventory)
					.forEach(Player::closeInventory);
		}
	}
	
	
	
	@EventHandler(ignoreCancelled = true)
	public void onOpen(InventoryOpenEvent event) {
		CustomInventory inventory = tryGet(event, event.getPlayer());
		if (inventory != null) {
			inventory.onOpen((Player) event.getPlayer(), event);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onClick(InventoryClickEvent event) {
		CustomInventory inventory = tryGet(event, event.getWhoClicked());
		if (inventory != null) {
			inventory.onClick((Player) event.getWhoClicked(), event);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onDrag(InventoryDragEvent event) {
		CustomInventory inventory = tryGet(event, event.getWhoClicked());
		if (inventory != null) {
			inventory.onDrag((Player) event.getWhoClicked(), event);
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		CustomInventory inventory = tryGet(event, event.getPlayer());
		if (inventory != null) {
			inventory.onClose((Player) event.getPlayer(), event);
		}
	}
	
	
	
	private CustomInventory tryGet(InventoryEvent event, HumanEntity entity) {
		InventoryHolder holder = event.getInventory().getHolder();
		if (!(holder instanceof CustomInventory)) {
			return null;
		} else if (!(entity instanceof Player)) {
			TriJavaPlugin.getInstance().getLogger()
					.severe("Use of CustomInventory by non-player HumanEntities is not allowed");
			return null;
		} else {
			return (CustomInventory) holder;
		}
	}
}
