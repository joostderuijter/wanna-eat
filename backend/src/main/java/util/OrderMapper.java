package util;

import model.*;

public class OrderMapper {

    public static State mapOrderToState(OrderRequest orderRequest) {
        State state = new State();
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

        Bank bank = Bank.valueOf(orderRequest.getBank());
        customer.setBank(bank);

        state.setCustomer(customer);
        return state;
    }
}
