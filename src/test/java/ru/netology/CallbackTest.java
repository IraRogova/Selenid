package ru.netology;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CallbackTest {
        private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

        @AfterEach
        void tearDown() {
            driver.quit();
            driver = null;
        }

    @Test
    void shouldSearchByCssSelector() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Петрович Петр");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79951232524");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button[type='button']")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldSendWithNameDashName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Санк-Роговский Олег");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79651234567");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button[type='button']")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldNotSendWithInvalidName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Petr");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79651234567");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button[type='button']")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldNotSendWithInvalidPhoneNumber() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Петрович Петр");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("++790123");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button[type='button']")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void shouldNotSendWithLettersInsteadOfNumbers() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Петрович Петр");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("Плюс Семь Девятьсот Шесть");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button[type='button']")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void shouldNotSendWithoutCheckboxCheckColor() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Петрович Петр");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79012345678");
        String previousColor = driver.findElement(By.cssSelector("[data-test-id=agreement]")).getCssValue("color");
        driver.findElement(By.cssSelector("button[type='button']")).click();
        String invalidColor = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid")).getCssValue("color");
        assertNotEquals(previousColor, invalidColor);
    }
}
