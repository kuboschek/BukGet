package is.hw.api.bukget.cache;

import is.hw.api.bukget.BukGetData;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

/**
 * A Runnable that updates a BukGetData instance when it's run.
 * @author Leonhard
 *
 */
public class UpdateDataTask extends BukkitRunnable {

	private List<CacheUpdateInformable> updated;
	private BukGetData data;
	
	/**
	 * 
	 * @param data	The data that is to be kept up-to-date
	 */
	public UpdateDataTask(BukGetData data) {
		updated = new ArrayList<CacheUpdateInformable>();
		this.data = data;
	}
	
	public void addListener(CacheUpdateInformable c) {
		updated.add(c);
		if (data.executed) {
			c.onCacheUpdated(data);
		}
	}
	
	@Override
	public void run() {
		try {
			data.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(CacheUpdateInformable c : updated) {
			c.onCacheUpdated(data);
		}
	}

}