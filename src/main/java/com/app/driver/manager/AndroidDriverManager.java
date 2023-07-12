package com.app.driver.manager;

import static io.appium.java_client.remote.MobileCapabilityType.APP;
import static io.appium.java_client.remote.MobileCapabilityType.AUTOMATION_NAME;
import static io.appium.java_client.remote.MobileCapabilityType.DEVICE_NAME;
import static io.appium.java_client.remote.MobileCapabilityType.PLATFORM_VERSION;
import static io.appium.java_client.remote.MobileCapabilityType.UDID;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;
import com.app.config.Configuration;
import com.app.config.ConfigurationManager;
import com.app.driver.IDriver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobilePlatform;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.remote.DesiredCapabilities;

public class AndroidDriverManager implements IDriver {

	private AppiumDriver<MobileElement> driver;

	@Override
	public AppiumDriver<MobileElement> createInstance(String udid, String platformVersion) {
		try {
			Configuration configuration = ConfigurationManager.getConfiguration();
			DesiredCapabilities capabilities = new DesiredCapabilities();

			capabilities.setCapability(UDID, udid);
			capabilities.setCapability(PLATFORM_VERSION, platformVersion);
			capabilities.setCapability(DEVICE_NAME, "Android Emulator");
			capabilities.setCapability(PLATFORM_NAME, MobilePlatform.ANDROID);
			capabilities.setCapability(AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);
			capabilities.setCapability("autoGrantPermissions", true);
			capabilities.setCapability("unlockType", "pin");
//			capabilities.setCapability("unlockType", "pinWithKeyEvent");
			capabilities.setCapability("unlockKey", "1111");
			capabilities.setCapability("newCommandTimeout", 300 * 60);
			capabilities.setCapability("ignoreHiddenApiPolicyError", true);

			if (Boolean.TRUE.equals(configuration.installApp())) {
				capabilities.setCapability(APP, new File(configuration.androidAppPath()).getAbsolutePath());
			} else {
				capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, configuration.androidAppPackage());
				capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
						configuration.androidAppActivity());
			}

			driver = new AndroidDriver<>(new URL(gridUrl()), capabilities);
		} catch (MalformedURLException e) {
			System.out.println("Failed to initiate the tests for the Android device");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Failed to initiate the tests for the Android device");
			e.printStackTrace();
		}

		return driver;
	}

	public AppiumDriver<MobileElement> createEspressoInstance(String udid, String platformVersion) {
		Configuration configuration = ConfigurationManager.getConfiguration();

		DesiredCapabilities capabilities = new DesiredCapabilities();
		try {
			capabilities.setCapability(PLATFORM_NAME, MobilePlatform.ANDROID);
			capabilities.setCapability(DEVICE_NAME, "Android Emulator");
			capabilities.setCapability(AUTOMATION_NAME, AutomationName.ESPRESSO);
			capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, configuration.androidAppPackage());
			capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, configuration.androidAppActivity());
			capabilities.setCapability(UDID, udid);
			capabilities.setCapability("forceEspressoRebuild", true);
			capabilities.setCapability("showGradleLog", true);
			capabilities.setCapability("printPageSourceOnFindFailure", false);
			capabilities.setCapability("noSign", false);
			capabilities.setCapability("espressoBuildConfig",
					"{ \"additionalAppDependencies\": [ \"com.google.android.material:material:1.0.0\", \"androidx.lifecycle:lifecycle-extensions:2.1.0\",\"org.jetbrains.kotlin:kotlin-stdlib:1.4.20\"] }");

			if (Boolean.TRUE.equals(configuration.installApp())) {
				capabilities.setCapability(APP, new File(configuration.androidAppPath()).getAbsolutePath());
			} else {
				capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, configuration.androidAppPackage());
				capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
						configuration.androidAppActivity());
			}

			capabilities.setCapability("noReset", true);

			return new AndroidDriver<MobileElement>(new URL(gridUrl()), capabilities);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}

	}
}
