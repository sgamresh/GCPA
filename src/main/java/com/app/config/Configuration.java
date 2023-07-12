package com.app.config;

import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;

import org.aeonbits.owner.Config;

@LoadPolicy(LoadType.MERGE)
@Config.Sources({ "system:properties", "classpath:general.properties" })
public interface Configuration extends Config {

	@Key("run.ip")
	String serverIp();

	@Key("run.port")
	String serverPort();

	@Key("install.app")
	Boolean installApp();

	@Key("app.ios.path")
	String iosAppPath();

	@Key("app.ios.appName")
	String iosAppName();

	@Key("app.android.path")
	String androidAppPath();

	@Key("app.android.appPackage")
	String androidAppPackage();

	@Key("app.android.appActivity")
	String androidAppActivity();

}
