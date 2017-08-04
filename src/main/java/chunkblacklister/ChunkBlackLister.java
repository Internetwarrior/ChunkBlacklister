package chunkblacklister;

import chunkblacklister.chunk.ChunkManager;
import chunkblacklister.listener.BlockListener;
import chunkblacklister.listener.ChunkListener;
import org.bukkit.plugin.java.JavaPlugin;

public class ChunkBlackLister extends JavaPlugin {

	/**
	 * The ChunkManager, used to store chunks and how many pistons are within them.
	 */
	private ChunkManager chunkManager;

	@Override
	public void onEnable() {
		this.chunkManager = new ChunkManager();
		registerListeners();
		getLogger().info("ChunkBlackLister enabled.");
	}

	@Override
	public void onDisable() {
		getLogger().info("ChunkBlackLister disabled.");
	}

	/**
	 * Registers the Listeners for this Plugin.
	 */
	public void registerListeners() {
		new ChunkListener(this);
		new BlockListener(this);
	}

	/**
	 * Gets the associated ChunkManager.
	 *
	 * @return The stored ChunkManager.
	 */
	public ChunkManager getChunkManager() {
		return chunkManager;
	}
}
