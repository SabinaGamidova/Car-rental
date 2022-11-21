package exception;

public class ExceptionHandler {
    public static void handleException(ExceptionHandlerInterface exceptionHandlerInterface) {
        try {
            exceptionHandlerInterface.handle();
        } catch (CarRentalException exception) {
            System.out.println(exception.getMessage());
        } catch (NumberFormatException exception) {
            System.out.println("You entered invalid data, please try again");
        } catch (IllegalArgumentException exception) {
            System.out.println("You entered invalid date, please try again");
        } catch (IndexOutOfBoundsException exception) {
            System.out.println("You entered invalid position, please try again");
        }
    }
}
