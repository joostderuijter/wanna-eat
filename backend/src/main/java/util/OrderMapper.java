package util;

import model.*;

public class OrderMapper {

    public static Customer mapOrderRequestToCustomer(OrderRequest orderRequest) {
        Customer customer = new Customer();
        customer.setName(orderRequest.getFullName());

        Address address = new Address();
        address.setStreet(orderRequest.getStreet());
        address.setCity(orderRequest.getCity());
        address.setNumber(orderRequest.getHouseNumber());
        address.setPostalCode(orderRequest.getPostalCode());

        customer.setAddress(address);
        customer.setMailAddress(orderRequest.getEmail());
        customer.setPhoneNumber(orderRequest.getPhoneNumber());

        //TODO: make amount to spend null safe
        customer.setWallet(new Wallet(Double.parseDouble(orderRequest.getAmountToSpend())));

        //TODO: to uppercase is a quick fix for something that should be fixed in the frontend
        Bank bank = Bank.valueOf(orderRequest.getBank().toUpperCase());
        customer.setBank(bank);
        return customer;
    }
}
