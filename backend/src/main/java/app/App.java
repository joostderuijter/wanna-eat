package app;

import exception.NoItemsAddedException;
import exception.NoRestaurantsFoundException;
import model.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import util.WebDriverFactory;
import util.Functions;
import util.Predicates;
import util.Suppliers;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class App {

    private final State state;
    private final WebDriver webDriver;
    private final Functions functions;

    public App() {
        this.state = new State();
        this.functions = new Functions();
        this.webDriver = WebDriverFactory.createChromeDriver();
    }

    public static void main(String[] args) throws NoRestaurantsFoundException, NoItemsAddedException {
        App app = new App();
        app.getState().setCustomer(Suppliers.generateTestCustomer().get());
        app.startSeleniumSession();
        app.fillInPostalCode();
        app.clearCookieBanner();
        app.clickRandomRestaurant();
        app.generateRandomOrder();
        //app.fillInAddressPopup();
        app.clickItemsFromOrder();
        //app.retryFailedItemClicks();
        app.clickCartOrderButton();
        app.fillInCustomerInformation();
    }

    private void startSeleniumSession() {
        getWebDriver().get(Constants.THUISBEZORGD_HOME_URL);
    }

    private void fillInPostalCode() {
        getWebDriver().findElement(By.id(Constants.WEB_ELEMENT_BY_ID_HOME_PAGE_POSTAL_CODE_INPUT)).click();
        getWebDriver().findElement(By.id(Constants.WEB_ELEMENT_BY_ID_HOME_PAGE_POSTAL_CODE_INPUT))
                .sendKeys(getState().getCustomer().getAddress().getPostalCode(), Keys.ENTER);
        timeOut(2000);
    }

    private void clearCookieBanner() {
        getWebDriver().findElement(By.className(Constants.WEB_ELEMENT_BY_CLASS_NAME_COOKIE_BANNER_OK_BUTTON)).click();
    }

    private void clickRandomRestaurant() throws NoRestaurantsFoundException {
        getState().setOrder(new Order());

        List<Restaurant> affordableRestaurants = getWebDriver().findElements(By.className(Constants.WEB_ELEMENT_BY_CLASS_NAME_OPEN_RESTAURANT))
                .stream()
                .map(webElement -> getFunctions().mapWebElementToRestaurant.apply(webElement))
                .filter(restaurant -> Predicates.restaurantTooExpensive.test(getState(), restaurant))
                .collect(Collectors.toList());

        if (affordableRestaurants.isEmpty()) throw new NoRestaurantsFoundException();

        getState().getOrder().setRestaurant(
                affordableRestaurants.get(getFunctions().generateRandomNumber.apply(affordableRestaurants.size())));
        getWebDriver().get(getState().getOrder().getRestaurant().getUrl());
    }

    private void generateRandomOrder() throws NoItemsAddedException {
        List<Item> items = getFunctions().randomizeMealWebElements
                .apply(getWebDriver().findElements(By.className(Constants.WEB_ELEMENT_BY_CLASS_NAME_MEAL_CONTAINER)))
                .stream()
                .map(webElement -> getFunctions().mapWebElementToItem.apply(webElement))
                .collect(Collectors.toList());

        Order order = new Order();
        order.setRestaurant(getState().getOrder().getRestaurant());

        double maxAmountToOrder = getFunctions().subtractDeliveryFee.apply(getState());
        if (maxAmountToOrder == 0.0) throw new NoItemsAddedException();
        double totalOrderPrice = 0;

        for (Item item : items) {
            if (totalOrderPrice + item.getPrice() < maxAmountToOrder) {
                order.getItems().add(item);
                order.setPrice(order.getPrice() + item.getPrice());
                maxAmountToOrder -= item.getPrice();
            }
        }

        if (order.getItems().isEmpty()) throw new NoItemsAddedException();
        getState().setOrder(order);
    }

    private void fillInAddressPopup() {
        getWebDriver().findElement(By.id(getState().getOrder().getItems().get(0).getId())).click();
        getWebDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        getWebDriver().findElement(By.id(Constants.WEB_ELEMENT_BY_ID_ADDRESS_POPUP_INPUT))
                .sendKeys(getFunctions().composeTotalAddress.apply(state.getCustomer().getAddress()));
        timeOut(1000);
        getWebDriver().findElement(By.id(Constants.WEB_ELEMENT_BY_ID_ADDRESS_POPUP_INPUT))
                .sendKeys(Keys.ENTER);
    }

    private void clickItemsFromOrder() {
        timeOut(2000);
        getState().getOrder().getItems().forEach(item -> {
            try {
                orderItem(item);
            } catch (ElementClickInterceptedException e) {
                System.out.println("Can't click item.");
                getState().incrementFailedClicks();
            }
        });
    }

    private void orderItem(Item item) throws ElementClickInterceptedException {
        if (!item.isSideDishes()) addItemToCart(item);
        if (item.isSideDishes()) addItemWithSidedishesToCart(item);
    }

    private void retryFailedItemClicks() {
        int numberOfFailedClicks = getState().getFailedClicks();
        for (int i = 0; i < numberOfFailedClicks ; i++) {
            try {
                orderItem(getState().getOrder().getItems().get(i));
            } catch (ElementClickInterceptedException e) {
                continue;
            }
            getState().decrementFailedClicks();
        }
        if (getState().getFailedClicks() > 0) {
            retryFailedItemClicks();
        }
    }

    private void clickCartOrderButton() {
        getWebDriver().findElement(By.className(Constants.WEB_ELEMENT_BY_CLASS_NAME_CART_ORDER_BUTTON)).click();
    }

    private void fillInCustomerInformation() {
        timeOut(2000);
        getWebDriver().findElement(By.id(Constants.WEB_ELEMENT_BY_ID_ORDER_SUBMIT_FORM_ADDRESS_STREETNAME)).clear();
        getWebDriver().findElement(By.id(Constants.WEB_ELEMENT_BY_ID_ORDER_SUBMIT_FORM_ADDRESS_STREETNAME))
                .sendKeys(getFunctions().composePartialAddress.apply(getState().getCustomer().getAddress()));
        getWebDriver().findElement(By.id(Constants.WEB_ELEMENT_BY_ID_ORDER_SUBMIT_FORM_ADDRESS_POSTAL_CODE)).clear();
        getWebDriver().findElement(By.id(Constants.WEB_ELEMENT_BY_ID_ORDER_SUBMIT_FORM_ADDRESS_POSTAL_CODE))
                .sendKeys(getState().getCustomer().getAddress().getPostalCode());
        getWebDriver().findElement(By.id(Constants.WEB_ELEMENT_BY_ID_ORDER_SUBMIT_FORM_ADDRESS_RESIDENCE)).clear();
        getWebDriver().findElement(By.id(Constants.WEB_ELEMENT_BY_ID_ORDER_SUBMIT_FORM_ADDRESS_RESIDENCE))
                .sendKeys(getState().getCustomer().getAddress().getResidence());
        getWebDriver().findElement(By.id(Constants.WEB_ELEMENT_BY_ID_ORDER_SUBMIT_FORM_CUSTOMER_NAME))
                .sendKeys(getState().getCustomer().getName());
        getWebDriver().findElement(By.id(Constants.WEB_ELEMENT_BY_ID_ORDER_SUBMIT_FORM_CUSTOMER_MAIL))
                .sendKeys(getState().getCustomer().getMailAddress());
        getWebDriver().findElement(By.id(Constants.WEB_ELEMENT_BY_ID_ORDER_SUBMIT_FORM_CUSTOMER_PHONE_NUMBER))
                .sendKeys(getState().getCustomer().getPhoneNumber());

        getWebDriver().findElement(By.className(Constants.WEB_ELEMENT_BY_CLASS_NAME_ORDER_SUBMIT_FORM_PAYMENTMETHOD_IDEAL_BUTTON))
                .click();

        Select bankOptionsDropDown = new Select(getWebDriver().findElement(
                By.id(Constants.WEB_ELEMENT_BY_ID_ORDER_SUBMIT_FORM_PAYMENTMETHOD_IDEAL_BANK_DROPDOWN)));
        bankOptionsDropDown.selectByVisibleText(getState().getCustomer().getBank().getBankName());

        getWebDriver().findElement(By.className(Constants.WEB_ELEMENT_BY_CLASS_NAME_ORDER_SUBMIT_FORM_SUBMIT_BUTTON)).click();
        System.out.println(getWebDriver().getCurrentUrl());
    }

    private void addItemToCart(Item item) throws ElementClickInterceptedException {
        timeOut(2000);
        getWebDriver().findElement(By.id(item.getId())).click();
    }

    private void addItemWithSidedishesToCart(Item item) throws ElementClickInterceptedException {
        getWebDriver().findElement(By.id(item.getId())).click();
        timeOut(2000);
        getWebDriver()
                .findElement(By.className(Constants.WEB_ELEMENT_BY_CLASS_NAME_ITEM_WITH_SIDEDISHES_ORDER_BUTTON)).click();
    }

    private void timeOut(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            System.out.println("oops");
        }
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public State getState() {
        return state;
    }

    public Functions getFunctions() {
        return functions;
    }
}
