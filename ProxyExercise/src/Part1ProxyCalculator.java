import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Part1ProxyCalculator implements Part1Calculator {

    private Part1RealCalculator realCalculator;
    private List<DayOfWeek> allowedDays;
    private LocalTime firstTime;
    private LocalTime lastTime;

    public Part1ProxyCalculator(List<DayOfWeek> allowedDays, LocalTime firstTime, LocalTime lastTime){
        realCalculator = new Part1RealCalculator();
        this.allowedDays = allowedDays;
        this.firstTime = firstTime;
        this.lastTime = lastTime;
    }

    private boolean callAllowed(){
        LocalDateTime time = LocalDateTime.now();
        DayOfWeek currentDay = time.getDayOfWeek();
        LocalTime currentTime = time.toLocalTime();

        if(allowedDays.contains(currentDay)){
            if(currentTime.isAfter(firstTime) && currentTime.isBefore(lastTime)){
                return true;
            }
        }
        return false;
    }

    @Override
    public float add(float num1, float num2) throws Part1WrongTimeException {
        if(callAllowed()){
            return realCalculator.add(num1, num2);
        }
        else{
            throw new Part1WrongTimeException();
        }
    }

    @Override
    public float subtract(float num1, float num2) throws Part1WrongTimeException {
        if(callAllowed()){
            return realCalculator.subtract(num1, num2);
        }
        else{
            throw new Part1WrongTimeException();
        }
    }

    @Override
    public float multiply(float num1, float num2) throws Part1WrongTimeException {
        if(callAllowed()){
            return realCalculator.multiply(num1, num2);
        }
        else{
            throw new Part1WrongTimeException();
        }
    }

    @Override
    public float divide(float num1, float num2) throws Part1WrongTimeException {
        if(callAllowed()){
            return realCalculator.divide(num1, num2);
        }
        else{
            throw new Part1WrongTimeException();
        }
    }
}
