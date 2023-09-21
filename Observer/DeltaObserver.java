public class DeltaObserver implements Subject.Observer {
    private Flight oldFlight;
    private Flight newFlight;

    DeltaObserver(){
        oldFlight = null;
        newFlight = null;
    }
    @Override
    public void update(Flight flight) {
        if (newFlight != null) {
            oldFlight = newFlight;
        }
        newFlight = flight;

        if(oldFlight != null && newFlight != null){
            System.out.println("Delta: longitude: " + (newFlight.longitude - oldFlight.longitude) + " latitude: " + (newFlight.latitude - oldFlight.latitude) + " velocity: " +
                    (newFlight.velocity - oldFlight.velocity) + " altitude: " + (newFlight.geo_altitude - oldFlight.geo_altitude));
        }
    }
}
