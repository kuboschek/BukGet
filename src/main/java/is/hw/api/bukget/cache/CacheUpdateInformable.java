package is.hw.api.bukget.cache;

import is.hw.api.bukget.BukGetData;

/**
 * Defines methods needed to inform about cache updates
 * @author Leonhard
 *
 */
public interface CacheUpdateInformable {
	/**
	 * Informs attached objects of an updated object in the cache
	 * 
	 * @param updatedData The cached object that was just updated
	 */
	public void onCacheUpdated(BukGetData updatedData);
}
