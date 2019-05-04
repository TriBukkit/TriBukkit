package hu.trigary.tribukkit.copysave;

import com.google.gson.Gson;
import hu.trigary.tribukkit.json.JsonIO;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class CopyingSaver<T> {
	private final JavaPlugin plugin;
	private final File file;
	private final Gson gson;
	private final CopyingSaveable<T> toSave;
	private final Object savingLock = new Object();
	private boolean saving;
	private T saveNextAsync;
	
	public CopyingSaver(JavaPlugin plugin, File file, Gson gson, CopyingSaveable<T> toSave) {
		this.plugin = plugin;
		this.file = file;
		this.gson = gson;
		this.toSave = toSave;
	}
	
	public CopyingSaver(JavaPlugin plugin, String fileName, Gson gson, CopyingSaveable<T> toSave) {
		this(plugin, new File(plugin.getDataFolder(), fileName), gson, toSave);
	}
	
	
	
	public void saveAsync() {
		T copy = toSave.createAsyncSaveCopy();
		synchronized (savingLock) {
			if (saving) {
				saveNextAsync = copy;
				return;
			} else {
				saving = true;
			}
		}
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> saveAsyncImpl(copy));
	}
	
	private void saveAsyncImpl(T data) {
		JsonIO.save(gson, file, data);
		T temp;
		synchronized (savingLock) {
			if (saveNextAsync == null) {
				saving = false;
				savingLock.notifyAll();
				return;
			} else {
				temp = saveNextAsync;
				saveNextAsync = null;
			}
		}
		saveAsyncImpl(temp);
	}
	
	
	
	public void saveSync() {
		T copy = toSave.createSyncSaveCopy();
		synchronized (savingLock) {
			if (saving) {
				try {
					savingLock.wait();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			saving = true;
		}
		saveSyncImpl(copy);
	}
	
	private void saveSyncImpl(T data) {
		JsonIO.save(gson, file, data);
		synchronized (savingLock) {
			saving = false;
			savingLock.notifyAll();
		}
	}
}
