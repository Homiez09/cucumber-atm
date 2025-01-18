package ku.atm;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class StepDefATM {

    ATM atm;
    Bank bank;
    boolean validLogin;

    @Before
    public void init() {
        bank = new Bank("KU Bank");
        atm = new ATM(bank);
    }

    @Given("a customer with id {int} and pin {int} exists")
    public void a_customer_with_id_and_pin_exists(int id, int pin) {
        bank.openAccount(new Customer(id, pin));
    }

    @Given("a customer with id {int} and pin {int} with balance {float} exists")
    public void a_customer_with_id_and_pin_with_balance_exists(int id, int pin, double balance) {
        bank.openAccount(new Customer(id, pin, balance));
    }

    @When("I login to ATM with id {int} and pin {int}")
    public void i_login_to_ATM_with_id_and_pin(int id, int pin) {
        validLogin = atm.validateCustomer(id, pin);
    }

    @Then("I can login")
    public void i_can_login() {
        assertTrue(validLogin);
    }

    @Then("I cannot login")
    public void i_cannot_login() {
        assertFalse(validLogin);
    }

    @Then("Customer ID not found")
    public void that_id_does_not_exist() {
        assertFalse(validLogin);
    }

    @When("I withdraw {float} from ATM")
    public void i_withdraw_from_atm(double amount) throws NotEnoughBalanceException {
        atm.withdraw(amount);
    }

    @When("I overdraw {float} from ATM")
    public void i_withdraw_from_atm_more_than_balance(double amount) throws NotEnoughBalanceException {
        assertThrows(NotEnoughBalanceException.class,
                () -> atm.withdraw(amount));
    }

    @When("I deposit {float} to ATM")
    public void when_i_deposit_to_atm(double amount) {
        atm.deposit(amount);
    }

    @Then("my account balance is {float}")
    public void my_account_balance_is(double balance) {
        assertEquals(balance, atm.getBalance());
    }

    @When("I transfer {float} to customer id {int}")
    public void i_transfer_to_customer_id(double amount, int toId) throws NotEnoughBalanceException {
        atm.transfer(toId, amount);
    }

    @Then("customer id {int} account balance is {float}")
    public void customer_id_account_balance_is(int id, double balance) {
        assertEquals(balance,
                     bank.getCustomer(id).getAccount().getBalance());
    }
}
