package chunkblacklister.listener;

import chunkblacklister.ChunkBlackLister;
import chunkblacklister.chunk.ChunkManager;
import chunkblacklister.listener.comp.AssociatedPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.Plugin;

public class BlockListener implements Listener, AssociatedPlugin {

	/**
	 * The owning ChunkBlackLister.
	 */
	private ChunkBlackLister plugin;

	/**
	 * Constructs a new BlockListener, responsible for all block events.
	 *
	 * @param plugin - the owning plugin instance.
	 */
	public BlockListener(Plugin plugin) {
		this.plugin = (ChunkBlackLister) plugin;

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	/**
	 * Handles when a Block has been placed.
	 *
	 * @param event - the associated BlockPlaceEvent.
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();

		//Check if the Block is a Piston.
		if (block.getType() == Material.PISTON_BASE) {
			//Cache the ChunkManager.
			ChunkManager chunkManager = plugin.getChunkManager();
			//Store current amount of pistons.
			int pistonCount = fetchPistonCount(block);
			//Save new amount of pistons.
			chunkManager.saveChunk(block.getWorld().getName(), block.getChunk().getX(), block.getChunk().getZ(), pistonCount + 1);
		}
	}

	/**
	 * Handles when a Block has been broken.
	 *
	 * @param event - the associated BlockBreakEvent.
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();

		//Check if the Block is a Piston.
		if (block.getType() == Material.PISTON_BASE) {
			//Cache the ChunkManager.
			ChunkManager chunkManager = plugin.getChunkManager();
			//Store current amount of pistons.
			int pistonCount = fetchPistonCount(block);
			//Save new amount of pistons.
			chunkManager.saveChunk(block.getWorld().getName(), block.getChunk().getX(), block.getChunk().getZ(), pistonCount - 1);
		}
	}

	/**
	 * Handles when Redstone has had it's current changed.
	 *
	 * @param event - the associated BlockRedstoneEvent.
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onRedstoneCharge(BlockRedstoneEvent event) {
		ChunkManager chunkManager = plugin.getChunkManager();

		//Cache relevant data.
		Block block = event.getBlock();
		Chunk chunk = block.getChunk();

		//Check if the chunk exceeds 50 pistons.
		if (chunkManager.getPistonCount(chunk.getWorld().getName(), chunk.getX(), chunk.getZ()) > 50) {
			//Turn off Redstone, effectively canceling the event.
			event.setNewCurrent(0);
		}

	}

	/**
	 * Fetches the amount of Pistons in the specified Block's Chunk.
	 *
	 * @param block - the specified Block.
	 *
	 * @return The amount of pistons currently in the Chunk.
	 */

	private int fetchPistonCount(Block block) {
		//Cache the ChunkManager.
		ChunkManager chunkManager = plugin.getChunkManager();
		//Cache World.
		World world = block.getWorld();
		//Check if World is valid.
		if (world != null) {
			//Cache World's Name.
			String worldName = world.getName();
			//Cache Chunk.
			Chunk chunk = block.getChunk();
			//Cache the current amount of pistons.
			return chunkManager.getPistonCount(worldName, chunk.getX(), chunk.getZ());
		}
		return 0;
	}

	public Plugin getPlugin() {
		return this.plugin;
	}
}
