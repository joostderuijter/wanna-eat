package model;

public enum Bank {
    ABN_AMRO("ABN AMRO"),
    ING("ING"),
    RABOBANK("Rabobank");

    private final String bankName;

    Bank(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }
}
