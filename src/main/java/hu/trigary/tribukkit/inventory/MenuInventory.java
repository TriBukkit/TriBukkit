package hu.trigary.tribukkit.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MenuInventory extends CustomInventory {
	
	@Override
	public final void onClick(@NotNull Player player, @NotNull InventoryClickEvent event) {
		if (event.getClickedInventory() == getInventory()) {
			event.setCancelled(true);
			ItemStack clicked = event.getCurrentItem();
			onMenuClick(player, event.getSlot(), isNull(clicked) ? null : clicked);
		} else if (event.isShiftClick()) {
			event.setCancelled(true);
		} //TODO is this enough?
	}
	
	protected abstract void onMenuClick(@NotNull Player player, int slot, @Nullable ItemStack item);
	
	@Override
	public final void onDrag(@NotNull Player player, @NotNull InventoryDragEvent event) {
		if (event.getInventory() == getInventory()) {
			event.setCancelled(true);
		} //TODO is this enough?
		//TODO you can drag across inventories, how does that work?
	}
}
