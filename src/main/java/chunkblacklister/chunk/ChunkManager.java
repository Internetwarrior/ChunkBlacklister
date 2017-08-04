package chunkblacklister.chunk;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class ChunkManager {

	/**
	 * World Name to Chunk X,Z to Piston count.
	 */
	private final Map<String, Map<String, Integer>> worldToChunkMap;

	/**
	 * The delimiter used to concatenate the X and Z coordinates.
	 */
	private final static String COORDINATE_DELIMITER = ",";

	/**
	 * Constructs a new ChunkManager.
	 */
	public ChunkManager() {
		this.worldToChunkMap = new HashMap<>();
	}

	/**
	 * Saves the Chunk and it's amount of pistons.
	 *
	 * @param worldName   - the Chunk's World.
	 * @param x           - the Chunk's X position.
	 * @param z           - the Chunk's Z position.
	 * @param pistonCount - the amount of Pistons in the Chunk.
	 *
	 * @return The previously stored amount of pistons stored. 0 if none.
	 */
	public synchronized int saveChunk(String worldName, int x, int z, int pistonCount) {
		String parsedCoordinate = parseCoordinate(x, z);

		//If our world isn't currently stored, let's make a mapping for it's chunks.
		if (!worldToChunkMap.containsKey(worldName)) {
			worldToChunkMap.put(worldName, new HashMap<String, Integer>());
		}

		Map<String, Integer> chunkToPistonMap = worldToChunkMap.get(worldName);
		if (chunkToPistonMap != null) {
			if (chunkToPistonMap.containsKey(parsedCoordinate)) {
				return chunkToPistonMap.put(parseCoordinate(x, z), pistonCount);
			}
			else {
				chunkToPistonMap.put(parseCoordinate(x, z), pistonCount);
			}
		}
		return 0;
	}

	/**
	 * Removes the specified World from this Manager.
	 *
	 * @param worldName - the World's name.
	 *
	 * @return The stored Map of Chunks and their amount of pistons, <code>null</code> if the world wasn't removed.
	 */
	public synchronized Map<String, Integer> removeWorld(String worldName) {
		return worldToChunkMap.remove(worldName);
	}

	/**
	 * Removes the specified Chunk from this Manager.
	 *
	 * @param chunk - the Chunk's concatenated coordinates.
	 *
	 * @return The amount of pistons which was stored.
	 */
	public synchronized int removeChunk(String worldName, String chunk) {
		if (worldToChunkMap.containsKey(worldName)) {
			return worldToChunkMap.get(worldName).remove(chunk);
		}
		return 0;
	}

	/**
	 * Removes the specified Chunk from this Manager.
	 *
	 * @param x - the Chunk's X coordinate.
	 * @param z - the Chunk's Z coordinate.
	 *
	 * @return The amount of pistons which was stored.
	 */
	public synchronized int removeChunk(String worldName, int x, int z) {
		if (worldToChunkMap.containsKey(worldName)) {
			return worldToChunkMap.get(worldName).remove(parseCoordinate(x, z));
		}
		return 0;
	}

	/**
	 * Concatenates the specified coordinates using the delimiter.
	 *
	 * @param x - the specified X coordinate.
	 * @param z - the specified Z coordinate.
	 *
	 * @return A concatenated product of the params, a such as "1,2".
	 */
	public synchronized String parseCoordinate(int x, int z) {
		return String.valueOf(x) + "," + String.valueOf(z);
	}

	/**
	 * Parses the specified coordinates using the delimiter.
	 *
	 * @param coordinate - the specified concatenated coordinate.
	 *
	 * @return A concatenated product of the params, a such as "1,2".
	 */
	public synchronized int[] parseCoordinate(String coordinate) {
		//Cache for the parsed coordinates.
		int[] coordinates = new int[2];

		//Split coordinates into multiple Strings.
		String[] splitChars = coordinate.split(COORDINATE_DELIMITER);

		//Populate parsed coordinates, as long as the coordinates were successfully split.
		if (splitChars.length == 2) {
			coordinates[0] = Integer.valueOf(splitChars[0]);
			coordinates[1] = Integer.valueOf(splitChars[1]);
		}
		return coordinates;
	}

	/**
	 * Gets the Piston Count for the stored Chunk.
	 *
	 * @param worldName  - the World's name.
	 * @param chunkCoord - the Chunk's concatenated coordinate.
	 *
	 * @return The amount of Pistons currently in this stored Chunk.
	 */
	public synchronized int getPistonCount(String worldName, String chunkCoord) {
		//Cache the Map of Chunks and the number of pistons.
		Map<String, Integer> chunkToPistonMap = getChunkMap(worldName);

		//Check if map is valid.
		if (chunkToPistonMap != null) {
			//Check if the map contains our chunk.
			if (chunkToPistonMap.containsKey(chunkCoord)) {
				int pistonCount = chunkToPistonMap.get(chunkCoord);
				return pistonCount;
			}
		}

		//We have no stored number for this Chunk.
		return 0;
	}

	/**
	 * Gets the Piston Count for the stored Chunk.
	 *
	 * @param worldName - the World's name.
	 * @param x         - the Chunk's X coordinate.
	 * @param z         - the Chunk's Z coordinate.
	 *
	 * @return The amount of Pistons currently in this stored Chunk.
	 */
	public synchronized int getPistonCount(String worldName, int x, int z) {
		//Cache the Map of Chunks and the number of pistons.
		Map<String, Integer> chunkToPistonMap = getChunkMap(worldName);

		//Check if map is valid.
		if (chunkToPistonMap != null) {
			String parsedCoordinate = parseCoordinate(x, z);
			//Check if the map contains our chunk.
			if (chunkToPistonMap.containsKey(parsedCoordinate)) {
				int pistonCount = chunkToPistonMap.get(parsedCoordinate);
				return pistonCount;
			}
		}

		//We have no stored number for this Chunk.
		return 0;
	}

	/**
	 * Gets the stored Chunks for this World.
	 *
	 * @param worldName - the World's name we want to get the Chunks for.
	 *
	 * @return A Map of Chunk to Piston count.
	 */
	public synchronized Map<String, Integer> getChunkMap(String worldName) {
		if (worldToChunkMap.containsKey(worldName)) {
			return worldToChunkMap.get(worldName);
		}
		return null;
	}
}
