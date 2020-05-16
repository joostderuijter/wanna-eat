package exception;

public class NoItemsAddedException extends Exception {
    public NoItemsAddedException() {
        super("No items added while generating an order. Perhaps too little money supplied?");
    }
}
