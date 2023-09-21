public class Part1WrongTimeException extends Exception{

    private String message;

    public String getMessage() {
        return message;
    }

    public Part1WrongTimeException(){
        message = "Access to this object is not allowed at this time";
    }
}
