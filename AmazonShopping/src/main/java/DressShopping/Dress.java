package DressShopping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Dress {
	public static void main(String[] args) throws InterruptedException {
		
		//To invoke chrome browser
		WebDriverManager.chromedriver().setup();			
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		WebDriver driver = new ChromeDriver(options);
		
		//To pass URL into chrome browser
		driver.get("https://www.amazon.in/");
		WebElement SearchBox = driver.findElement(By.id("twotabsearchtextbox"));
		SearchBox.sendKeys("dress");
		driver.findElement(By.xpath("//*[@id=\"nav-search-bar-form\"]/div[3]/div")).click();
		driver.findElement(By.xpath("//*[@id=\"search\"]/div[1]/div[1]/div/span[1]/div[1]/div[4]")).click();
		
		// switched to new window
		String currentwindowHandle = driver.getWindowHandle();
		Set<String> windowhandles = driver.getWindowHandles();
		for (String windowHandle : windowhandles) {
			if (!windowHandle.equals(currentwindowHandle)) {
				driver.switchTo().window(windowHandle);
				break;
			}
		}
		Thread.sleep(4000);
		
		//stored title and price to strings and parsed it to integer
		String dressTitle = driver.findElement(By.xpath("//*[@id=\"productTitle\"]")).getText();
		WebElement markedprice = driver.findElement(By.cssSelector("span[class='a-price aok-align-center reinventPricePriceToPayMargin priceToPay'] span[class='a-price-whole']"));
		//int markedPriceint = Integer.parseInt(markedprice);
		System.out.println("dress selected is " + dressTitle);
		System.out.println("marked price shown is " + markedprice);
		
		//selected size from the dropdown
		WebElement selectElement = driver.findElement(By.id("native_dropdown_selected_size_name"));
		Select select = new Select(selectElement);
		select.selectByIndex(0);
		
		//added the product to cart
		driver.findElement(By.id("add-to-cart-button")).click();
		driver.findElement(By.xpath("//*[@id=\"nav-cart-count-container\"]")).click();
		
		//validating the title and price in cart
		String cartdressTitle = driver.findElement(By.className("a-truncate-cut")).getText();
		Thread.sleep(2000);
		String cartprice = driver.findElement(By.xpath("(//span[@class='a-price-whole'])[1]")).getText();
		int cartPriceint = (int) (Double.parseDouble(cartprice.trim()));
		System.out.println("dress in cart is " + cartdressTitle);
		System.out.println("price of item in cart is " + cartPriceint);
		//Assert.assertEquals(markedPriceint, cartPriceint);
		
		//checkout
		driver.findElement(By.name("proceedToRetailCheckout")).click();

		//screenshot of signin page
		File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		Path screenshotPath = Paths.get("screenshot.png");

		try {

			Files.copy(screenshotFile.toPath(), screenshotPath);
			System.out.println("Screenshot saved to " + screenshotPath.toAbsolutePath().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
