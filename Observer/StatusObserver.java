public class StatusObserver implements Subject.Observer {

    private Flight flight;

    @Override
    public void update(Flight flight) {
        this.flight = flight;
        if(flight == null){
            System.out.println("Flight Over");
        }
        else {
            System.out.println(this.flight);
        }
    }
}

