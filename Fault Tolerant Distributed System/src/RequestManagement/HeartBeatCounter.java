package RequestManagement;

import java.util.concurrent.atomic.AtomicInteger;

public class HeartBeatCounter {
	private static volatile HeartBeatCounter uniqueInstance = null;
	private static volatile AtomicInteger count = new AtomicInteger(1);
	
	public static HeartBeatCounter getInstance() {
		if (HeartBeatCounter.uniqueInstance == null) {
			synchronized(HeartBeatCounter.class) {
				if (HeartBeatCounter.uniqueInstance == null) {
					HeartBeatCounter.uniqueInstance = new HeartBeatCounter();
				}
			}
		}
		return HeartBeatCounter.uniqueInstance;
	}
	
	private HeartBeatCounter() {
	}
	
	public int getNextInteger() {
		return HeartBeatCounter.count.incrementAndGet();
	}
}
