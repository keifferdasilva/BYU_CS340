import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

public class Part1Main {
    public static void main(String[] args) {
        ArrayList<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.FRIDAY);
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        Part1ProxyCalculator proxyCalculator = new Part1ProxyCalculator(days, LocalTime.now().minusHours(2), LocalTime.now().plusHours(3));

        try {
            System.out.println(proxyCalculator.add(2, 4));
            System.out.println(proxyCalculator.divide(80, 20));
        }
        catch(Part1WrongTimeException ex){
            System.out.println(ex.getMessage());
        }
    }
}