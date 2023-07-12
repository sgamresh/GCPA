package com.app.driver;

import com.app.config.Configuration;
import com.app.config.ConfigurationManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public interface IDriver {

	AppiumDriver<MobileElement> createInstance(String udid, String platformVersion);

	default String gridUrl() {
		Configuration configuration = ConfigurationManager.getConfiguration();
		return String.format("http://%s:%s/wd/hub", configuration.serverIp(), configuration.serverPort());
	}
}
