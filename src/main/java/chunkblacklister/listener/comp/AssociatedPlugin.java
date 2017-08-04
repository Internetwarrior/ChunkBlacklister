package chunkblacklister.listener.comp;

import org.bukkit.plugin.Plugin;

public interface AssociatedPlugin {

	/**
	 * Gets the associated Plugin.
	 *
	 * @return The owning Plugin.
	 */
	Plugin getPlugin();

}
