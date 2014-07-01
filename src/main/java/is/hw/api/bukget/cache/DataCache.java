package is.hw.api.bukget.cache;

import is.hw.api.bukget.BukGetData;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Provides methods for easily creating periodically updating caches of BukGet information.
 * @author Leonhard
 *
 */
public class DataCache {
	private BukkitScheduler sched;
	private Map<Integer, BukGetData> caches;
	private Map<Integer, UpdateDataTask> tasks;
	private JavaPlugin plg;
	
	/**
	 * 
	 * @param sched	The scheduler that will be used to create new tasks.
	 * @param plg	The scheduler needs a plugin ref for some reason. Hence pass one.
	 */
	public DataCache(BukkitScheduler sched, JavaPlugin plg) {
		this.sched = sched;
		this.plg = plg;
		caches = new HashMap<Integer, BukGetData>();
		tasks = new HashMap<Integer, UpdateDataTask>();
	}
	
	/**
	 * Adds a periodically updating cache to this DataCache instance
	 * @param data	The data that is to be kept up-to-date
	 * @param intervalInSec	The interval(in seconds) in which to refresh the cache
	 * @return	Task ID of the newly created task
	 */
	public int addPeriodicUpdate(BukGetData data, long intervalInSec) {
		return addPeriodicUpdate(data, intervalInSec, 0L);
	}
	
	/**
	 * Adds a periodically updating cache to this DataCache instance
	 * @param data	The data that is to be kept up-to-date
	 * @param intervalInSec	The interval(in seconds) in which to refresh the cache
	 * @param delayInSec	The delay(in seconds) after which to start refreshing the cache
	 * @return	Task ID of the newly created task
	 */
	public int addPeriodicUpdate(BukGetData data, long intervalInSec, long delayInSec) {
		UpdateDataTask newTask = new UpdateDataTask(data);
		
		@SuppressWarnings("deprecation")
		int taskId = sched.scheduleAsyncRepeatingTask(plg, newTask, (long)delayInSec * 20L, (long)intervalInSec * 20L);
		caches.put(taskId, data);
		tasks.put(taskId, newTask);
		
		return taskId;
	}
	
	/**
	 * Adds an CacheUpdateInformable that is informed about a cache update
	 * 
	 */
	public DataCache addListener(int taskId, CacheUpdateInformable c) {
		if(tasks.containsKey(new Integer(taskId))) {
			UpdateDataTask task = tasks.get(new Integer(taskId));
			
			task.addListener(c);
		}
		return this;
	}
	
	/**
	 * Removes a task from the scheduler. Convenience method.
	 * @param id
	 * @return
	 */
	public int removeUpdate(int id) {
		sched.cancelTask(id);
		return id;
	}
	
}
