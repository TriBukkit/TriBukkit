package hu.trigary.tribukkit.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;
import java.util.UUID;

public enum HeadItemBuilder {
	ARROW_LEFT("MHF_ArrowLeft"),
	ARROW_RIGHT("MHF_ArrowRight"),
	ARROW_UP("MHF_ArrowUp"),
	ARROW_DOWN("MHF_ArrowDown"),
	QUESTION("MHF_Question"),
	EXCLAMATION("MHF_Exclamation"),
	CAMERA("FHG_Cam"),
	ZOMBIE_PIGMAN("MHF_PigZombie"),
	PIG("MHF_Pig"),
	SHEEP("MHF_Sheep"),
	BLAZE("MHF_Blaze"),
	CHICKEN("MHF_Chicken"),
	COW("MHF_Cow"),
	SLIME("MHF_Slime"),
	SPIDER("MHF_Spider"),
	SQUID("MHF_Squid"),
	VILLAGER("MHF_Villager"),
	OCELOT("MHF_Ocelot"),
	HEROBRINE("MHF_Herobrine"),
	LAVA_SLIME("MHF_LavaSlime"),
	MOOSHROOM("MHF_MushroomCow"),
	GOLEM("MHF_Golem"),
	GHAST("MHF_Ghast"),
	ENDERMAN("MHF_Enderman"),
	CAVE_SPIDER("MHF_CaveSpider"),
	CACTUS("MHF_Cactus"),
	CAKE("MHF_Cake"),
	CHEST("MHF_Chest"),
	MELON("MHF_Melon"),
	LOG("MHF_OakLog"),
	PUMPKIN("MHF_Pumpkin"),
	TNT("MHF_TNT"),
	TNT2("MHF_TNT2");
	
	private final String name;
	
	HeadItemBuilder(String name) {
		this.name = name;
	}
	
	//TODO allow URLs in the enum constants
	
	//TODO letters: https://github.com/WhoIsAlphaHelix/AlphaLibary/blob/master/src/main/java/io/github/alphahelixdev/alpary/utils/SkullUtil.java
	
	
	
	@Contract(pure = true)
	@NotNull
	public ItemBuilder get() {
		ItemBuilder builder = new ItemBuilder(Material.PLAYER_HEAD);
		//noinspection deprecation
		builder.applyCustomMeta(SkullMeta.class, meta -> meta.setOwner(name));
		return builder;
	}
	
	
	
	@Contract(pure = true)
	@NotNull
	public static ItemBuilder getPlayer(@NotNull String name) {
		ItemBuilder builder = new ItemBuilder(Material.PLAYER_HEAD);
		//noinspection deprecation
		builder.applyCustomMeta(SkullMeta.class, meta -> meta.setOwner(name));
		return builder;
	}
	
	@Contract(pure = true)
	@NotNull
	public static ItemBuilder getPlayer(@NotNull OfflinePlayer player) {
		ItemBuilder builder = new ItemBuilder(Material.PLAYER_HEAD);
		builder.applyCustomMeta(SkullMeta.class, meta -> meta.setOwningPlayer(player));
		return builder;
	}
	
	
	
	@Contract(pure = true)
	@NotNull
	public static ItemBuilder getBase64(@NotNull String base64) {
		//noinspection deprecation
		return new ItemBuilder(Bukkit.getUnsafe().modifyItemStack(new ItemStack(Material.PLAYER_HEAD),
				"{SkullOwner:{Id:\"" + UUID.randomUUID() + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}"));
	}
	
	@Contract(pure = true)
	@NotNull
	public static ItemBuilder getUrl(@NotNull String url) {
		String source = "{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/" + url + "\"}}}";
		return getBase64(Base64.getEncoder().encodeToString(source.getBytes()));
	}
}
