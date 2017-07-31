package ReadingList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServerWebTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    private static ChromeDriver chromeDriver;

    @BeforeClass
    public static void openBrowser() {
        System.setProperty("webdriver.chrome.driver", "./chromedriver");
        chromeDriver = new ChromeDriver();
        chromeDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void closeBrowser() {
        chromeDriver.quit();
    }

    @Test
    public void pageNotFound() {
        try {
            testRestTemplate.getForObject("http://localhost:{port}/readingList/mrlonelyjtr", String.class, port);
        }catch (HttpClientErrorException e){
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void addBookToEmptyList() {
        String baseUrl = "http://localhost:" + port;
        chromeDriver.get(baseUrl);
        String currentUrl = chromeDriver.getCurrentUrl();

        assertEquals(baseUrl + "/login", currentUrl);

        chromeDriver.findElementByName("username").sendKeys("mrlonelyjtr");
        chromeDriver.findElementByName("password").sendKeys("1234");
        chromeDriver.findElementByTagName("form").submit();
        currentUrl = chromeDriver.getCurrentUrl();

        assertEquals(baseUrl + "/readingList", currentUrl);

        assertEquals("You have no books in your book list", chromeDriver.findElementByTagName("div").getText());

        chromeDriver.findElementByName("title").sendKeys("And Then There Were None");
        chromeDriver.findElementByName("author").sendKeys("Agatha Christie");
        chromeDriver.findElementByName("isbn").sendKeys("0007136838");
        chromeDriver.findElementByName("description").sendKeys("无人生还");
        chromeDriver.findElementByTagName("form").submit();

        WebElement dt = chromeDriver.findElementByCssSelector("dt.bookHeadline");
        WebElement dd = chromeDriver.findElementByCssSelector("dd.bookDescription");

        assertEquals("And Then There Were None by Agatha Christie (ISBN: 0007136838)", dt.getText());
        assertEquals("无人生还", dd.getText());
    }

}
