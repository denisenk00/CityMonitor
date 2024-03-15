package ui;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UserCreationTest {
    private static WebDriver driver;
    private static String baseUrl;
    private static boolean acceptNextAlert = true;
    private static StringBuffer verificationErrors = new StringBuffer();
    static JavascriptExecutor js;
    @BeforeAll
    public static void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "..\\chromedriver.exe");
        driver = new ChromeDriver();
        baseUrl = "https://www.google.com/";
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testCityMonitorUserCreationRecord() throws Exception {
        driver.get("http://localhost:8088/login");
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("sysadm");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("sysadm");
        driver.findElement(By.xpath("//input[@value='Увійти']")).click();
        driver.get("http://localhost:8088/");
        driver.findElement(By.linkText("Управління користувачами")).click();
        driver.findElement(By.id("create-user-btn")).click();
        driver.findElement(By.id("new-username")).click();
        driver.findElement(By.id("new-username")).clear();
        driver.findElement(By.id("new-username")).sendKeys("NewUser");
        driver.findElement(By.id("new-role")).click();
        new Select(driver.findElement(By.id("new-role"))).selectByVisibleText("ADMIN");
        driver.findElement(By.id("create-user-submit")).click();
        String alertText = closeAlertAndGetItsText();
        assertEquals("Користувача створено успішно. Пароль для входу в аккаунт ", alertText.split("-")[0]);
    }

    @AfterAll
    public static void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
    private String closeAlertAndGetItsText() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
