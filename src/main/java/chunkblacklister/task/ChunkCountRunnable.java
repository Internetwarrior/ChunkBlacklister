package chunkblacklister.task;

import chunkblacklister.chunk.ChunkManager;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class ChunkCountRunnable extends BukkitRunnable {

	/**
	 * The ChunkManager to store the amount of Pistons.
	 */
	private ChunkManager chunkManager;

	/**
	 * The ChunkSnapshot to dictate which chunk data to use.
	 */
	private ChunkSnapshot chunkSnapshot;

	/**
	 * Constructs a new ChunkCountRunnable.
	 *
	 * @param chunkManager  - the owning ChunkManager.
	 * @param chunkSnapshot - the specified ChunkSnapshot to use.
	 */
	public ChunkCountRunnable(ChunkManager chunkManager, ChunkSnapshot chunkSnapshot) {
		this.chunkManager = chunkManager;
		this.chunkSnapshot = chunkSnapshot;
	}

	@Override
	public void run() {
		int pistonCount = 0;
		//Iterate through each stored Block ID.
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 256; y++) {
				for (int z = 0; z < 16; z++) {
					//Check if the BlockType is equal to a Piston.
					if (Material.getMaterial(chunkSnapshot.getBlockTypeId(x, y, z)) == Material.PISTON_BASE) {
						//Increment the total pistons in the Chunk.
						pistonCount++;
					}
				}
			}
		}

		//All Pistons have been counted.
		//Save to the ChunkManager.
		chunkManager.saveChunk(chunkSnapshot.getWorldName(), chunkSnapshot.getX(), chunkSnapshot.getZ(), pistonCount);
	}

}
