package util;

import model.Address;
import model.Bank;
import model.Customer;
import model.Wallet;

import java.util.function.Supplier;

public class Suppliers {
    public static Supplier<Customer> generateTestCustomer() {
        Customer customer = new Customer();
        customer.setName("");
        customer.setMailAddress("");
        customer.setPhoneNumber("");
        customer.setWallet(generateTestWallet().get());
        customer.setAddress(generateTestAddress().get());
        customer.setBank(Bank.RABOBANK);
        return () -> customer;
    }

    private static Supplier<Address> generateTestAddress() {
        Address address = new Address();
        address.setNumber("");
        address.setPostalCode("");
        address.setCity("");
        address.setStreet("");
        return () -> address;
    }

    private static Supplier<Wallet> generateTestWallet() {
        return () -> new Wallet(60);
    }
}
