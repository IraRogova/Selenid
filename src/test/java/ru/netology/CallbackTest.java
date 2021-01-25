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
        driver.findElement(By.cssSelector("[type ='text']")).sendKeys("Петрович Петр");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79951232524");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldSendWithNameDashName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type ='text']")).sendKeys("Петр-Петрович Петр");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79951232524");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldNotSendWithInvalidName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type ='text']")).sendKeys("Petr Petrovich");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79951232524");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldNotSendWithInvalidPhoneNumber() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type ='text']")).sendKeys("Петрович Петр");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("++790123");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void shouldNotSendWithLettersInsteadOfNumbers() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type ='text']")).sendKeys("Петрович Петр");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("Плюс Семь Девятьсот Шесть");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void shouldNotSendWithoutCheckboxCheckColor() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[type ='text']")).sendKeys("Петрович Петр");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79012345678");
        String previousColor = driver.findElement(By.cssSelector("[data-test-id=agreement]")).getCssValue("color");
        driver.findElement(By.tagName("button")).click();
        String invalidColor = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid")).getCssValue("color");
        assertNotEquals(previousColor, invalidColor);
    }
}
