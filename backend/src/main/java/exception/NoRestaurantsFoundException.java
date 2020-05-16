package exception;

public class NoRestaurantsFoundException extends Exception {
    public NoRestaurantsFoundException() {
        super("No restaurants found. There are no open restaurants, or you haven't supplied enough money.");
    }
}
