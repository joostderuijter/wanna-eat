package exception;

public class BankNotFoundException extends Exception {
    public BankNotFoundException(String bankName) {
        super("No bank found named " + bankName);
    }
}
