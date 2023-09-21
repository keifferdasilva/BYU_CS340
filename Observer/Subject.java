import java.util.ArrayList;

public abstract class Subject {

    private ArrayList<Observer> observers = new ArrayList<>();

    public interface Observer{

        void update(Flight flight);
    }

    public void register(Observer observer){
        observers.add(observer);
    }

    protected void notify(Flight flight){
        for(Observer observer : observers){
            observer.update(flight);
        }
    }
}
