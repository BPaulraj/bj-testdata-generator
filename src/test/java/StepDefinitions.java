import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class StepDefinitions {
    private WebDriver driver;

    @Given("I open Google homepage")
    public void i_open_google_homepage() {
        driver = new ChromeDriver();
        driver.get("https://www.google.com");
    }

    @When("I search for {string}")
    public void i_search_for(String query) {
        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys(query);
        searchBox.submit();
    }

    @Then("the results page should show results for {string}")
    public void the_results_page_should_show_results_for(String query) {
        Assert.assertTrue(driver.getTitle().toLowerCase().contains(query.toLowerCase()));
        driver.quit();
    }
}
