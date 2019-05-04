package hu.trigary.tribukkit.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.IntFunction;

public abstract class PagedMenuInventory<T> extends MenuInventory {
	private final T[] sources;
	
	protected PagedMenuInventory(@NotNull Collection<T> source, int itemsPerPage,
			int page, @NotNull IntFunction<T[]> sourceArrayConstructor) {
		sources = source.stream().skip((page - 1L) * itemsPerPage)
				.limit(itemsPerPage).toArray(sourceArrayConstructor);
	}
	
	
	
	protected abstract ItemStack sourceToItemStackMapper(T source);
	
	@Override
	protected void initialize(int size, @NotNull String title) {
		super.initialize(size, title);
		for (int i = 0; i < sources.length; i++) {
			getInventory().setItem(i, sourceToItemStackMapper(sources[i]));
		}
	}
	
	@Override
	protected final void onMenuClick(@NotNull Player player, int slot, @Nullable ItemStack item) {
		onPagedMenuClick(player, slot, item, sources.length > slot ? sources[slot] : null);
	}
	
	protected abstract void onPagedMenuClick(@NotNull Player player,
			int slot, @Nullable ItemStack item, @Nullable T source);
	
	
	
	@Contract(pure = true)
	public static int getPageCount(int itemsPerPage, int itemCount) {
		return (itemCount + itemsPerPage - 1) / itemsPerPage;
	}
}
