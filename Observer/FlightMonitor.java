
public class FlightMonitor {
	
	public static void main(String[] args) {
	
		FlightFeed feed = new FlightFeed();
		StatusObserver statusObserver = new StatusObserver();
		feed.register(statusObserver);
		DeltaObserver deltaObserver = new DeltaObserver();
		feed.register(deltaObserver);
		feed.start();
	}
	
}