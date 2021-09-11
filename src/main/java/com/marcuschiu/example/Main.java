package com.marcuschiu.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws IOException {
        InputStream inputStream = Main.class.getResourceAsStream("/config.properties");
        Properties properties = new Properties();
        properties.load(inputStream);

        String chromeDriverPath = properties.getProperty("chrome-driver");
        String atlassianCloudDomain = properties.getProperty("atlassian-cloud-domain");
        String username = properties.getProperty("atlassian-username");
        String password = properties.getProperty("atlassian-password");

        backupConfluenceCloud(chromeDriverPath,
                atlassianCloudDomain,
                username,
                password);
    }

    private static void backupConfluenceCloud(final String chromeDriverPath,
                                              final String atlassianCloudDomain,
                                              final String username,
                                              final String password) {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(
//                "--headless",
//                "--disable-gpu",
//                "--disable-extensions"
        );
        WebDriver driver = new ChromeDriver(chromeOptions);

        List<String> logs = new ArrayList<>();
        try {
            driver.get(atlassianCloudDomain);
            logs.add("01 - Entered " + atlassianCloudDomain);
            driver.findElement(By.xpath("//span[@aria-label='Sign In']")).click();
            logs.add("02 - Clicked `Sign In`");
            driver.findElement(By.xpath("//input[@name='username']")).sendKeys(username);
            logs.add("03 - Entered Username: " + username);
            driver.findElement(By.xpath("//button[@id='login-submit']")).click();
            logs.add("04 - Clicked `username-submit`");
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.findElement(By.xpath("//input[@id='password']")).sendKeys(password);
            logs.add("05 - Entered Password: " + password);
            driver.findElement(By.xpath("//button[@id='login-submit']")).click();
            logs.add("06 - Clicked `password-submit`");
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            driver.findElement(By.xpath("//span[@aria-label='Settings Icon']")).click();
            logs.add("07 - Clicked `Settings Icon`");
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            driver.findElement(By.xpath("//button[@data-testid='grid-left-sidebar-resize-button']")).click();
            logs.add("08 - Clicked `left-sidebar-expand`");
            driver.findElement(By.xpath("//span[@data-test-id='backupmanager-menu-item']")).click();
            logs.add("09 - Clicked `backupmanager-menu-item`");
            driver.findElement(By.xpath("//span[@id='backupLocation']/a")).click();
            logs.add("10 - Clicked `download-link`");
//            driver.findElement(By.xpath("//input[@value='Create backup for cloud']")).click();
            logs.add("12 - Clicked `create backup for cloud`");
            Thread.sleep(100000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
            logs.forEach(System.out::println);
        }
    }
}