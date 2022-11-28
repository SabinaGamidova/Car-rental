package exception;

public class ExceptionHandler {
    public static void handleException(ExceptionHandlerInterface exceptionHandlerInterface) {
        try {
            exceptionHandlerInterface.handle();
        } catch (CarRentalException exception) {
            System.out.println(exception.getMessage());
        } catch (NumberFormatException exception) {
            System.out.println("\nYou entered invalid data, please try again\n");
        } catch (IllegalArgumentException exception) {
            System.out.println("\nYou entered invalid date, please try again\n");
        } catch (IndexOutOfBoundsException exception) {
            System.out.println("\nYou entered invalid position, please try again\n");
        }
    }
}
