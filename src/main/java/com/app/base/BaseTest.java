package com.app.base;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.app.config.Configuration;

import com.app.config.ConfigurationManager;
import com.app.driver.Browser;
import com.app.driver.DriverFactory;
import com.app.driver.Platform;
import com.app.utils.Event;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class BaseTest {
	protected AppiumDriverLocalService service;
	Configuration config = ConfigurationManager.getConfiguration();
	int Port = Integer.parseInt(config.serverPort());
	protected RemoteWebDriver Webdriver;
	protected AppiumDriver<MobileElement> driver;
	protected Event event = new Event();


	@BeforeSuite(alwaysRun = true)
	@Parameters({ "platform", "browser", "udid", "platformVersion" })
	public void startServer(@Optional("optional") String platformName, @Optional("optional") String browserName,
			@Optional("optional") String udidName, @Optional("optional") String platformVersionName) {
		Browser browser = Browser.valueOf(browserName.toUpperCase());

		switch (browser) {
		case MACANDROIDCHROME:
		case MAC: {
			try {
				System.out.println("[BEFORESUITE] Starting Appium in: " + browser);
				Map<String, String> env = new HashMap<>(System.getenv());
				env.put("PATH", "/usr/local/bin:" + env.get("PATH"));
				AppiumServiceBuilder builder = new AppiumServiceBuilder().withIPAddress(config.serverIp())
						.usingPort(Port).withArgument(() -> "--base-path", "/wd/hub")
						.withArgument(() -> "--allow-insecure", "chromedriver_autodownload").withEnvironment(env)
						.usingDriverExecutable(new File("/usr/local/bin/node"))
						.withArgument(GeneralServerFlag.LOG_LEVEL, "error").withAppiumJS(new File(
								"/Applications/Appium.app/Contents/Resources/app/node_modules/appium/build/lib/main.js"));

				service = AppiumDriverLocalService.buildService(builder);
				builder.withLogFile(new File("./appiumLogs/appiumLogs" + System.currentTimeMillis()));
				if (service.isRunning() == true) {
					service.stop();
				} else {
					service.start();
					service.clearOutPutStreams();
					System.out.println("[BEFORESUITE] Appium Server Started Sucessfully.");
				}

			} catch (AppiumServerHasNotBeenStartedLocallyException e) {
				e.printStackTrace();
			} catch (Exception es) {
				es.printStackTrace();
			}
			break;
		}
		case IOSSAFARI: {
			try {
				System.out.println("[BEFORESUITE] Starting Appium in: " + browser);
				Map<String, String> env = new HashMap<>(System.getenv());
				env.put("PATH", "/usr/local/bin:" + env.get("PATH"));
				AppiumServiceBuilder builder = new AppiumServiceBuilder().withIPAddress(config.serverIp())
						.usingPort(Port).withArgument(() -> "--base-path", "/wd/hub").withEnvironment(env)
						.usingDriverExecutable(new File("/usr/local/bin/node"))
						.withArgument(GeneralServerFlag.LOG_LEVEL, "error").withAppiumJS(new File(
								"/Applications/Appium.app/Contents/Resources/app/node_modules/appium/build/lib/main.js"));
				service = AppiumDriverLocalService.buildService(builder);
				builder.withLogFile(new File("./appiumLogs/appiumLogs" + System.currentTimeMillis()));
				if (service.isRunning() == true) {
					service.stop();
				} else {
					service.start();
					service.clearOutPutStreams();
					System.out.println("[BEFORESUITE] Appium Server Started Sucessfully.");
				}

			} catch (AppiumServerHasNotBeenStartedLocallyException e) {
				e.printStackTrace();
			} catch (Exception es) {
				es.printStackTrace();
			}
			break;
		}
		case WINDOWS:
		case ANDROIDCHROME: {
			try {
				System.out.println("[EVENT] Starting Appium in Windows Machine");
				service = AppiumDriverLocalService.
				// buildDefaultService();
						buildService(new AppiumServiceBuilder()
								.usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe"))
								.withArgument(() -> "--allow-insecure", "chromedriver_autodownload")
								.withAppiumJS(new File("C:\\Users\\" + System.getProperty("user.name")
										+ "\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"))
								.withIPAddress(config.serverIp()).usingPort(Port)
								.withArgument(GeneralServerFlag.LOG_LEVEL, "error"));// this is the flag to remove debug

				if (service.isRunning() == true) {
					service.stop();
				} else {
					service.start();
					System.out.println("[BEFORESUITE] Appium Server Started Sucessfully.");
				}
			} catch (AppiumServerHasNotBeenStartedLocallyException e) {
				e.printStackTrace();
			} catch (Exception es) {
				es.printStackTrace();
			}
			break;
		}

		case MACSAFARI:
		case MACCHROME:
		case WINFIREFOX:
		case MACFIREFOX:
		case WINCHROME: {
			System.out.println("[BEFORESUITE] For WEB PWA Automation Appium Server not is not Started: " + browser);
			break;

		}
		default: {
			System.out.println("[BEFORESUITE] Default Case Not implemented for: " + browser);
			break;
		}

		}

	}

	@BeforeMethod(alwaysRun = true)
	@Parameters({ "platform", "browser", "udid", "platformVersion", "username", "environment" })
	public void launchApp(String platform, @Optional("optional") String browser, String udid, String platformVersion,
			String mobileNumber, String environment) throws IOException, Exception {
		try {
			System.out.println("[BEFOREMETHOD] Parameters Received: " + browser + ", " + platform);

			Platform PlatformName = Platform.valueOf(platform.toUpperCase());

			switch (PlatformName) {
			case PWA:
			case WEB: {
				Webdriver = new DriverFactory().createInstanceofWeb(platform, browser, udid, platformVersion);
				break;
			}
			case ANDROID:
			case IOS: {
				driver = new DriverFactory().createInstance(platform, udid, platformVersion, browser);

				break;
			}

			default: {
				System.out.println("[BEFOREMETHOD] Default Case Not implemented for: " + PlatformName);
				break;
			}

			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterMethod
	@Parameters({ "platform", "browser", "udid", "platformVersion" })
	public void closeApp(String platform, @Optional("optional") String browser, @Optional("optional") String udid,
			@Optional("optional") String platformVersion) {
		try {
			System.out.println("[AFTERMETHOD] Parameters Received: " + browser + ", " + platform);

			Platform platformName = Platform.valueOf(platform.toUpperCase());
			switch (platformName) {
			case PWA:
			case WEB: {
				System.out.println("[AFTERMETHOD] Quiting Driver.");
				try {
					if (Webdriver != null) {
						Webdriver.quit();
					}
				} catch (Exception e) {

				}

				break;
			}

			case ANDROID: {
				if (driver != null) {
					driver.quit();
				}

				break;
			}

			default: {
				System.out.println("[AFTERMETHOD] Default Case Not implemented: " + platform.toUpperCase());
				break;
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterSuite
	@Parameters({ "platform", "browser", "udid", "platformVersion" })
	public void stopServer(String platform, @Optional("optional") String browser, @Optional("optional") String udid,
			@Optional("optional") String platformVersion) {
		System.out.println("[AFTERSUITE] Parameters Received: " + browser + ", " + platform);

		Platform platformName = Platform.valueOf(platform.toUpperCase());

		switch (platformName) {
		case PWA:
		case IOS:
		case MACANDROIDCHROME:
		case ANDROID:

		case WEB: {
			System.out.println("[AFTERSUITE] For Web/Pwa Appium Server is not Started.");
			break;
		}
		default: {
			System.out.println("[AFTERSUITE] Default Case Not implemented: " + platform.toUpperCase());
			break;
		}

		}
		
//		try {
//			Runtime.getRuntime().exec("/usr/local/bin/allure generate allure-results --clean");
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
