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
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * A cache responsible for storing player name-UUID pairs.
 * These values are loaded from {@link Bukkit#getOfflinePlayers()} on class instance creation,
 * and are updated on-the-fly when players log in ({@link AsyncPlayerPreLoginEvent})
 * or when a Mojang API lookup is executed through this class.
 * Data can also be manually added and removed.
 * All methods are thread-safe.
 * <br><br>
 * There are 4 ways to do get data: (A cached value is always returned if it is present.)
 * <ul>
 * <li>{@code getId/getName}: cached value, no lookups are involved</li>
 * <li>{@code LookUpBlocking}: cached value or query the value while blocking the current thread</li>
 * <li>{@code LookUpAsync}: cached value or query the value on a background thread,
 * also calling the callback on this background thread</li>
 * <li>{@code LookUpNonBlocking}: cached value or query the value on a background thread,
 * but calling the callback on the main Bukkit thread</li>
 * </ul>
 */
public class NameIdCache implements Listener { //https://wiki.vg/Mojang_API
	private static final String NAME_TO_ID_URL = "https://api.mojang.com/users/profiles/minecraft/";
	private static final String ID_TO_NAME_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
	private final Map<String, UUID> nameToId = new HashMap<>();
	private final Map<UUID, String> idToName = new HashMap<>();
	private final JavaPlugin plugin;
	private final HttpClient httpClient = HttpClient.newHttpClient();
	
	NameIdCache(JavaPlugin plugin) {
		this.plugin = plugin;
		for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
			if (player.getName() != null) {
				putData(player.getName(), player.getUniqueId());
			}
		}
	}
	
	
	
	/**
	 * Adds the specified player name-UUID pair to the cache.
	 *
	 * @param name the name of the player
	 * @param id the UUID of the player
	 */
	public synchronized void putData(@NotNull String name, @NotNull UUID id) {
		nameToId.put(name.toLowerCase(), id);
		idToName.put(id, name);
	}
	
	/**
	 * Removes the specified player name from the cache.
	 * This method does <b>not</b> remove the UUID associated with the name.
	 *
	 * @param name the player name to remove
	 */
	public synchronized void removeData(@NotNull String name) {
		nameToId.remove(name.toLowerCase());
	}
	
	/**
	 * Removes the specified player name from the cache.
	 * This method does <b>not</b> remove the name associated with the UUID.
	 *
	 * @param id the player UUID to remove
	 */
	public synchronized void removeData(@NotNull UUID id) {
		idToName.remove(id);
	}
	
	
	
	/**
	 * Gets the cached UUID for the given name.
	 * Returns null if no cached value is present.
	 *
	 * @param name the player's name
	 * @return the cached UUID or null, if none is present
	 */
	@Nullable
	@Contract(pure = true)
	public UUID getId(@NotNull String name) {
		return nameToId.get(name.toLowerCase());
	}
	
	/**
	 * Gets the cached UUID for the given name,
	 * or queries it from the Mojang API if none is present.
	 * Returns null only if the query failed.
	 * This method blocks the thread it is called from.
	 *
	 * @param name the player's name
	 * @return the cached UUID or null, if the HTTP query failed
	 */
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
	
	/**
	 * Gets the cached UUID for the given name,
	 * or queries it from the Mojang API if none is present.
	 * Returns null only if the query failed.
	 * This method does not block, it uses a background thread.
	 * The callback is also called from this background thread.
	 *
	 * @param name the player's name
	 * @param consumer the callback which handles the result
	 */
	public void getIdOrLookupAsync(@NotNull String name, @NotNull Consumer<Optional<UUID>> consumer) {
		UUID cached = getId(name);
		if (cached != null) {
			consumer.accept(Optional.of(cached));
		}
		
		HttpRequest request = HttpRequest.newBuilder(URI.create(NAME_TO_ID_URL + name)).GET().build();
		httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenAccept(response -> consumer.accept(Optional.ofNullable(handleIdLookup(response, name))));
	}
	
	/**
	 * Gets the cached UUID for the given name,
	 * or queries it from the Mojang API if none is present.
	 * Returns null only if the query failed.
	 * This method does not block, it uses a background thread.
	 * The callback is called from the main Bukkit thread.
	 *
	 * @param name the player's name
	 * @param consumer the callback which handles the result
	 */
	public void getIdOrLookupNonBlocking(@NotNull String name, @NotNull Consumer<Optional<UUID>> consumer) {
		getIdOrLookupAsync(name, id -> Bukkit.getScheduler().runTask(plugin, () -> consumer.accept(id)));
	}
	
	
	
	/**
	 * Gets the cached name for the given UUID.
	 * Returns null if no cached value is present.
	 *
	 * @param id the player's UUID
	 * @return the cached name or null, if none is present
	 */
	@Nullable
	@Contract(pure = true)
	public String getName(@NotNull UUID id) {
		return idToName.get(id);
	}
	
	/**
	 * Gets the cached name for the given UUID,
	 * or queries it from the Mojang API if none is present.
	 * Returns null only if the query failed.
	 * This method blocks the thread it is called from.
	 *
	 * @param id the player's UUID
	 * @return the cached name or null, if the HTTP query failed
	 */
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
	
	/**
	 * Gets the cached name for the given UUID,
	 * or queries it from the Mojang API if none is present.
	 * Returns null only if the query failed.
	 * This method does not block, it uses a background thread.
	 * The callback is also called from this background thread.
	 *
	 * @param id the player's UUID
	 * @param consumer the callback which handles the result
	 */
	public void getNameOrLookupAsync(@NotNull UUID id, @NotNull Consumer<Optional<String>> consumer) {
		String cached = getName(id);
		if (cached != null) {
			consumer.accept(Optional.of(cached));
		}
		
		HttpRequest request = HttpRequest.newBuilder(URI.create(ID_TO_NAME_URL + toString(id))).GET().build();
		httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenAccept(response -> consumer.accept(Optional.ofNullable(handleNameLookup(response, id))));
	}
	
	/**
	 * Gets the cached name for the given UUID,
	 * or queries it from the Mojang API if none is present.
	 * Returns null only if the query failed.
	 * This method does not block, it uses a background thread.
	 * The callback is called from the main Bukkit thread.
	 *
	 * @param id the player's UUID
	 * @param consumer the callback which handles the result
	 */
	public void getNameOrLookupNonBlocking(@NotNull UUID id, @NotNull Consumer<Optional<String>> consumer) {
		getNameOrLookupAsync(id, name -> Bukkit.getScheduler().runTask(plugin, () -> consumer.accept(name)));
	}
	
	
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		putData(event.getName(), event.getUniqueId());
	}
	
	
	
	private UUID handleIdLookup(HttpResponse<String> response, String name) {
		try {
			if (response.statusCode() == 200) {
				UUID result = UUID.fromString(response.body().substring(7, 40));
				putData(name, result);
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
				putData(result, id);
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
		TriJavaPlugin.log(level, "[NameIdCache] " + message);
	}
}
