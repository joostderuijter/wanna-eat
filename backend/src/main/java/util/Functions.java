package util;

import model.*;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Functions {

    public final Function<List<WebElement>, List<WebElement>> randomizeMealWebElements = webElements -> {
        Collections.shuffle(webElements);
        return webElements;
    };

    public final Function<Address, String> composeTotalAddress = address ->
            address.getStreet() + " " + address.getNumber() + " " + address.getResidence();

    public final Function<Address, String> composePartialAddress = address ->
            address.getStreet() + " " + address.getNumber();

    public final Function<Integer, Integer> generateRandomNumber = RandomSingleton.getInstance()::nextInt;

    public final Function<String, String> stripAllNonValidCharactersForParsing = string -> string.replace("Min. € ", "")
            .replace("min", "")
            .replace("GRATIS", "")
            .replace("€ ", "")
            .replace(",", ".");

    public final Function<String, Integer> deliveryTimeStringToInt = deliveryTimeString ->
            Integer.parseInt(stripAllNonValidCharactersForParsing.apply(deliveryTimeString));

    public final Function<String, Double> priceStringToDouble = priceString ->
            Double.parseDouble(stripAllNonValidCharactersForParsing.apply(priceString));

    public final Function<String, Double> deliveryFeeStringToDouble = deliveryFeeString ->
            priceStringToDouble.apply(
                    Optional.ofNullable(stripAllNonValidCharactersForParsing.apply(deliveryFeeString))
                            .filter(StringUtils::isNotEmpty)
                            .orElseGet(() -> "0"));

    public final Function<String, String> constructUrlFromBreadcrumb = url -> Constants.THUISBEZORGD_HOME_URL +
            url.replace("document.location='", "")
                    .replace("';return false;","");

    public final Function<WebElement, Restaurant> mapWebElementToRestaurant = webElement -> {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(webElement.getAttribute(Constants.WEB_ELEMENT_BY_ATTRIBUTE_ID));
        restaurant.setName(webElement.findElement(By.className(Constants.WEB_ELEMENT_BY_CLASS_NAME_RESTAURANT_NAME)).getText());
        restaurant.setDeliveryTime(deliveryTimeStringToInt.apply(
                webElement.findElement(By.className(Constants.WEB_ELEMENT_BY_CLASS_NAME_AVG_DELIVERY_TIME)).getText()));
        restaurant.setDeliveryFee(deliveryFeeStringToDouble.apply(
                webElement.findElement(By.className(Constants.WEB_ELEMENT_BY_CLASS_NAME_DELIVERY_FEE)).getText()));
        restaurant.setMinimumOrderAmount(priceStringToDouble.apply(
                webElement.findElement(By.className(Constants.WEB_ELEMENT_BY_CLASS_NAME_MINIMUM_ORDER_AMOUNT)).getText()));
        restaurant.setUrl(constructUrlFromBreadcrumb.apply(
                webElement.getAttribute(Constants.WEB_ELEMENT_BY_ATTRIBUTE_NAME_ONCLICK)));
        return restaurant;
    };

    public final Function<WebElement, Item> mapWebElementToItem = webElement -> {
        Item item = new Item();
        item.setId(webElement.getAttribute(Constants.WEB_ELEMENT_BY_ATTRIBUTE_ID));
        item.setName(webElement.findElement(By.className(Constants.WEB_ELEMENT_BY_CLASS_NAME_MEAL_NAME)).getText());
        item.setPrice(priceStringToDouble.apply(webElement.findElement(By.className(Constants.WEB_ELEMENT_BY_CLASS_NAME_MEAL_PRICE)).getText()));
        try {
            webElement.findElement(By.className(Constants.WEB_ELEMENT_BY_CLASS_NAME_ITEM_WITH_SIDEDISHES));
            item.setSideDishes(true);
        } catch (NoSuchElementException nse) {
            item.setSideDishes(false);
        }
        return item;
    };

    public final Function<State, Double> subtractDeliveryFee = state ->
            state.getCustomer().getWallet().getAmountOfMoney() - state.getOrder().getRestaurant().getDeliveryFee();
}
