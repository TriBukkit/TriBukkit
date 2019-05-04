package hu.trigary.tribukkit;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;

public class NameIdCache implements Listener { //https://wiki.vg/Mojang_API
	private static final String NAME_TO_ID_URL = "https://api.mojang.com/users/profiles/minecraft/";
	private static final String ID_TO_NAME_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
	private final HashMap<String, UUID> nameToId = new HashMap<>();
	private final HashMap<UUID, String> idToName = new HashMap<>();
	private final JavaPlugin plugin;
	private final HttpClient httpClient = HttpClient.newHttpClient();
	
	NameIdCache(JavaPlugin plugin) {
		this.plugin = plugin;
		for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
			if (player.getName() != null) {
				store(player.getName(), player.getUniqueId());
			}
		}
	}
	
	
	
	@Nullable
	@Contract(pure = true)
	public UUID getId(@NotNull String name) {
		return nameToId.get(name.toLowerCase());
	}
	
	@Nullable
	public UUID getIdOrLookupBlocking(@NotNull String name) {
		UUID cached = getId(name);
		if (cached != null) {
			return cached;
		}
		
		try {
			HttpRequest request = HttpRequest.newBuilder(URI.create(NAME_TO_ID_URL + name)).GET().build();
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			return handleIdLookup(response, name);
		} catch (IOException | InterruptedException e) {
			log(Level.SEVERE, "UUID lookup failed for name: " + name);
			e.printStackTrace();
			return null;
		}
	}
	
	public void getIdOrLookupAsync(@NotNull String name, @NotNull Consumer<Optional<UUID>> consumer) {
		UUID cached = getId(name);
		if (cached != null) {
			consumer.accept(Optional.of(cached));
		}
		
		HttpRequest request = HttpRequest.newBuilder(URI.create(NAME_TO_ID_URL + name)).GET().build();
		httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenAccept(response -> consumer.accept(Optional.ofNullable(handleIdLookup(response, name))));
	}
	
	public void getIdOrLookupNonBlocking(@NotNull String name, @NotNull Consumer<Optional<UUID>> consumer) {
		getIdOrLookupAsync(name, id -> Bukkit.getScheduler().runTask(plugin, () -> consumer.accept(id)));
	}
	
	
	
	@Nullable
	@Contract(pure = true)
	public String getName(@NotNull UUID id) {
		return idToName.get(id);
	}
	
	@Nullable
	public String getNameOrLookupBlocking(@NotNull UUID id) {
		String cached = getName(id);
		if (cached != null) {
			return cached;
		}
		
		try {
			HttpRequest request = HttpRequest.newBuilder(URI.create(ID_TO_NAME_URL + toString(id))).GET().build();
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			return handleNameLookup(response, id);
		} catch (IOException | InterruptedException e) {
			log(Level.SEVERE, "Name lookup failed for UUID: " + id);
			e.printStackTrace();
			return null;
		}
	}
	
	public void getNameOrLookupAsync(@NotNull UUID id, @NotNull Consumer<Optional<String>> consumer) {
		String cached = getName(id);
		if (cached != null) {
			consumer.accept(Optional.of(cached));
		}
		
		HttpRequest request = HttpRequest.newBuilder(URI.create(ID_TO_NAME_URL + toString(id))).GET().build();
		httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenAccept(response -> consumer.accept(Optional.ofNullable(handleNameLookup(response, id))));
	}
	
	public void getNameOrLookupNonBlocking(@NotNull UUID id, @NotNull Consumer<Optional<String>> consumer) {
		getNameOrLookupAsync(id, name -> Bukkit.getScheduler().runTask(plugin, () -> consumer.accept(name)));
	}
	
	
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		store(event.getName(), event.getUniqueId());
	}
	
	private synchronized void store(String name, UUID id) {
		nameToId.put(name.toLowerCase(), id);
		idToName.put(id, name);
	}
	
	
	
	private UUID handleIdLookup(HttpResponse<String> response, String name) {
		try {
			if (response.statusCode() == 200) {
				UUID result = UUID.fromString(response.body().substring(7, 40));
				store(name, result);
				return result;
			} else if (response.statusCode() == 204) {
				return null;
			} else {
				log(Level.WARNING, "UUID lookup failed (status code: "
						+ response.statusCode() + ") for name: " + name);
				return null;
			}
		} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
			log(Level.SEVERE, "UUID lookup failed for name: " + name);
			e.printStackTrace();
			return null;
		}
	}
	
	private String handleNameLookup(HttpResponse<String> response, UUID id) {
		try {
			if (response.statusCode() == 200) {
				String result = response.body().substring(49, response.body().indexOf('"', 50));
				store(result, id);
				return result;
			} else if (response.statusCode() == 204) {
				return null;
			} else {
				log(Level.WARNING, "Name lookup failed (status code: "
						+ response.statusCode() + ") for UUID: " + id);
				return null;
			}
		} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
			log(Level.SEVERE, "Name lookup failed for UUID: " + id);
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	private String toString(UUID id) {
		return StringUtils.replace(id.toString(), "-", "", 4);
	}
	
	private void log(Level level, String message) {
		TriJavaPlugin.getInstance().getLogger().log(level, "[NameIdCache] " + message);
	}
}
