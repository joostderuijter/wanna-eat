package util;

import model.Restaurant;
import model.State;

import java.util.function.BiPredicate;

public class Predicates {

    public static BiPredicate<State, Restaurant> restaurantTooExpensive = (state, restaurant) ->
            restaurant.getDeliveryFee() + restaurant.getMinimumOrderAmount() <= state.getCustomer().getWallet().getAmountOfMoney();
}
