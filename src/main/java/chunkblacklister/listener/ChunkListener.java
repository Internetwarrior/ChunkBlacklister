package chunkblacklister.listener;

import chunkblacklister.ChunkBlackLister;
import chunkblacklister.listener.comp.AssociatedPlugin;
import chunkblacklister.task.ChunkCountRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.Plugin;

public class ChunkListener implements Listener, AssociatedPlugin {

	/**
	 * The owning Plugin.
	 */
	private ChunkBlackLister plugin;

	/**
	 * Constructs a new ChunkListener, responsible for all Chunk events.
	 *
	 * @param plugin - the owning ChunkBlackLister instance.
	 */
	public ChunkListener(Plugin plugin) {
		this.plugin = (ChunkBlackLister) plugin;

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	/**
	 * Handles when a Chunk has been loaded.
	 *
	 * @param event - the associated ChunkLoadEvent.
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onChunkLoad(ChunkLoadEvent event) {
		//Cache null safe Chunk.
		Chunk chunk = event.getChunk();
		ChunkSnapshot chunkSnapshot = chunk.getChunkSnapshot();

		//Not null safe.
		if (chunkSnapshot != null) {
			//Execute counting asynchronously.
			Bukkit.getScheduler().runTaskAsynchronously(plugin, new ChunkCountRunnable(plugin.getChunkManager(), chunkSnapshot));
		}
	}

	public Plugin getPlugin() {
		return plugin;
	}
}
