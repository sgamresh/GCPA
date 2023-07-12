package com.app.driver;

import org.openqa.selenium.remote.RemoteWebDriver;

public class BrowserDriver {

	protected static ThreadLocal<RemoteWebDriver> browserDriver = new ThreadLocal<RemoteWebDriver>();

	public void set(RemoteWebDriver driverInstance) {
		browserDriver.set(driverInstance);
	}

	public RemoteWebDriver get() {
		return browserDriver.get();
	}
}
