package model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderRequest {
    private String fullName;
    private String street;
    private String houseNumber;
    private String city;
    private String postalCode;
    private String email;
    private String phoneNumber;
    private String amountToSpend;
    private String bank;
}
