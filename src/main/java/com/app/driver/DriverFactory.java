package com.app.driver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import com.app.config.Configuration;
import com.app.config.ConfigurationManager;
import com.app.driver.manager.AndroidDriverManager;
import com.app.driver.manager.IOSDriverManager;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;

public class DriverFactory {

	public AppiumDriver<MobileElement> createInstance(String platform, String udid, String platformVersion,
			String browser) {
		AppiumDriver<MobileElement> driver = null;

		Platform mobilePlatform = Platform.valueOf(platform.toUpperCase());

		try {

			switch (mobilePlatform) {
			case IOS: {
				driver = new IOSDriverManager().createInstance(udid, platformVersion);
				break;
			}

			case ANDROID: {
				driver = new AndroidDriverManager().createInstance(udid, platformVersion);

				break;
			}

			default: {
				throw new Exception("Platform not supported! Check if you set ios or android on the parameter.");
			}
			}

		} catch (SessionNotCreatedException e) {
			System.out.println("[Exception] Unable to initialize Driver.");
			e.printStackTrace();
		} catch (Exception e) {

			System.out.println("[Exception] Unable to initialize Driver.");
			e.printStackTrace();
		}

		return driver;
	}

	@SuppressWarnings("deprecation")
	public RemoteWebDriver createInstanceofWeb(String platform, String browser, String udid, String platformVersion)
			throws IOException, Exception {
		RemoteWebDriver Webdriver = null;
		Platform mobilePlatform = Platform.valueOf(platform.toUpperCase());
		Browser browserName = Browser.valueOf(browser.toUpperCase());

		try {
			switch (mobilePlatform) {

			case ANDROID: {
				break;
			}

			case WEB: {
				System.setProperty("webdriver.chrome.silentOutput", "true");
				java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
				BrowserDriver bDriver = new BrowserDriver();

				switch (browserName) {
				case MACFIREFOX: {
					try {
						WebDriverManager.firefoxdriver().setup();
						FirefoxOptions ffoptions = new FirefoxOptions();
						ffoptions.addArguments("--start-maximized");
						Webdriver = new FirefoxDriver(ffoptions);
						bDriver.set(Webdriver);
						bDriver.get().manage().deleteAllCookies();
						bDriver.get().manage().window().maximize();
					} catch (WebDriverException e) {
						e.printStackTrace();
					} catch (Exception es) {
						es.printStackTrace();
					}
					break;
				}

				case WINFIREFOX: {
					try {
						WebDriverManager.firefoxdriver().setup();
						FirefoxOptions ffoptions = new FirefoxOptions();
						ffoptions.addArguments("--start-maximized");
						ProfilesIni profile = new ProfilesIni();
						FirefoxProfile testprofile = profile.getProfile("automation");
						testprofile.setPreference("media.eme.enabled", true);
						testprofile.setPreference("media.gmp-manager.updateEnabled", true);
						ffoptions.setProfile(testprofile);
						Webdriver = new FirefoxDriver(ffoptions);
						bDriver.set(Webdriver);
						bDriver.get().manage().deleteAllCookies();
						bDriver.get().manage().window().maximize();
					} catch (WebDriverException e) {
						e.printStackTrace();
					}
					break;
				}

				case MACCHROME:
				case WINCHROME: {
					WebDriverManager.chromedriver().setup();
					ChromeOptions options = new ChromeOptions();
					options.addArguments("--disable-site-isolation-trials");
					options.addArguments("--start-maximized");
					// disable the microphone popup
					Map<String, Object> prefs = new HashMap<>();
					prefs.put("profile.default_content_setting_values.media_stream_mic", 2);
					options.setExperimentalOption("prefs", prefs);

//					options.addArguments("--headless");
					// options.addArguments("--window-size=1920,1080");
					Webdriver = new ChromeDriver(options);
					bDriver.set(Webdriver);
					bDriver.get().manage().deleteAllCookies();
					break;
				}

				case MACSAFARI: {
					try {
						Runtime.getRuntime().exec("killall safaridriver");
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						DriverManagerType safari = DriverManagerType.SAFARI;
						WebDriverManager.getInstance(safari).setup();
						Class<?> safariClass = Class.forName(safari.browserClass());
						Webdriver = (RemoteWebDriver) safariClass.getDeclaredConstructor().newInstance();
						bDriver.set(Webdriver);
						// bDriver.get().manage().deleteAllCookies();
						bDriver.get().manage().window().maximize();
						// bDriver.get().manage().window().fullscreen();
					} catch (Exception a) {
						System.out.println("Error ::: " + a);
					}
					break;
				}

				default: {
					System.out.println("[INFO] Browser is not in scope for Automation as of now: " + browserName);
					break;
				}

				}

				break;
			}

			case PWA: {

				switch (browserName) {
				case ANDROIDCHROME: {
					DesiredCapabilities capabilities = new DesiredCapabilities();
					ChromeOptions options = new ChromeOptions();
					options.addArguments("--disable-notifications");
					options.addArguments("--disable-infobars");
					options.addArguments("androidPackage", "com.android.chrome");
					// options.addArguments("--enable-popup-blocking");
					// options.addArguments("--disable-popup-blocking");
					options.setExperimentalOption("w3c", false);
					// disable the microphone popup
					// Map<String, Object> prefs = new HashMap<>();
					// prefs.put("profile.default_content_setting_values.media_stream_mic", 2);
					options.addArguments("use-fake-ui-for-media-stream");
					capabilities.setCapability(MobileCapabilityType.PLATFORM, platform);
					capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
					// capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,
					// VootConstants.DEVICE_NAME);
					capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, browser);
					capabilities.setCapability(MobileCapabilityType.UDID, udid);
					capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
					capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
					capabilities.setCapability("deviceOrientation", "portrait");
					capabilities.setCapability("unlockType", "pin");
					capabilities.setCapability("unlockKey", "1111");
					capabilities.setCapability("autoAcceptAlerts", true);
					capabilities.setCapability(ChromeOptions.CAPABILITY, options);
					capabilities.setCapability(MobileCapabilityType.SUPPORTS_APPLICATION_CACHE, true);
					capabilities.setCapability(MobileCapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, false);
					// capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,
					// "UiAutomator1");
					capabilities.setCapability("newCommandTimeout", 60 * 5);
					System.setProperty("webdriver.chrome.driver", ".//drivers//chromedriver.exe");

					try {
						Webdriver = new AndroidDriver<MobileElement>(new URL(gridUrl()), capabilities);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					break;
				}

				case MACANDROIDCHROME: {

					DesiredCapabilities capabilities = new DesiredCapabilities();
					ChromeOptions options = new ChromeOptions();
					options.addArguments("--disable-notifications");
					options.addArguments("--disable-infobars");
					options.addArguments("androidPackage", "com.android.chrome");
					// options.addArguments("--enable-popup-blocking");
					options.addArguments("--disable-popup-blocking");
					options.setExperimentalOption("w3c", false);
					capabilities.setCapability(MobileCapabilityType.PLATFORM, platform);
					capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
					// capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,
					// VootConstants.DEVICE_NAME);
					capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, browser);
					capabilities.setCapability(MobileCapabilityType.UDID, udid);
					capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
					capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
					capabilities.setCapability("deviceOrientation", "portrait");
					capabilities.setCapability("unlockType", "pin");
					capabilities.setCapability("unlockKey", "1111");
					capabilities.setCapability("autoAcceptAlerts", true);
					capabilities.setCapability("chromedriverUseSystemExecutable", false);

					capabilities.setCapability(ChromeOptions.CAPABILITY, options);
					capabilities.setCapability(MobileCapabilityType.SUPPORTS_APPLICATION_CACHE, true);
					capabilities.setCapability(MobileCapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, false);
					// capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,
					// "UiAutomator1");
					capabilities.setCapability("newCommandTimeout", 60 * 5);

					try {
						Webdriver = new AndroidDriver<MobileElement>(new URL(gridUrl()), capabilities);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (Exception w) {
						w.printStackTrace();
					}
					break;
				}

				case IOSSAFARI: {
					DesiredCapabilities capabilities = new DesiredCapabilities();
					SafariOptions options = new SafariOptions();
					capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iphone12");
					capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "ios");
					capabilities.setCapability(MobileCapabilityType.UDID, udid);
					capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "safari");
					capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
					capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
					capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
					options.setCapability("W3C", false);
					try {
						Webdriver = new IOSDriver<MobileElement>(new URL(gridUrl()), capabilities);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (Exception w) {
						w.printStackTrace();
					}
					break;
				}
				default: {
					System.out.println("[INFO] Browser is not in scope for Automation as of now: " + browserName);
					break;
				}
				}
				break;
			}

			default: {
				throw new Exception("Platform not supported! Check if you set ios or android on the parameter.");
			}
			}

		} catch (SessionNotCreatedException e) {
			System.out.println("[Exception] Unable to initialize Driver.");
		}
		return Webdriver;

	}

	private String gridUrl() {
		Configuration configuration = ConfigurationManager.getConfiguration();
		return String.format("http://%s:%s/wd/hub", configuration.serverIp(), configuration.serverPort());
	}
}
