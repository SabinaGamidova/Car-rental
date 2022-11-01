package exception;

public class CarRentalException extends RuntimeException{
    public CarRentalException(String message, Object...params){
        super(String.format(message, params));
    }
}