package com.tom.cpm.shared;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MinecraftObjectHolder {
	public static final Gson gson = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().create();
	protected static MinecraftClientAccess clientObject;
	protected static MinecraftCommonAccess commonObject;
	protected static MinecraftServerAccess serverAccess;

	public static final boolean DEBUGGING = true;
	public static final String NETWORK_ID = "cpm_net";
	public static final String VERSION_CHECK_URL = "https://raw.githubusercontent.com/tom5454/CustomPlayerModels/master/version-check.json";

	public static void setClientObject(MinecraftClientAccess clientObject) {
		MinecraftObjectHolder.clientObject = clientObject;
//		commonObject.getVersionCheck();//Begin version check
	}

	public static void setCommonObject(MinecraftCommonAccess commonObject) {
		MinecraftObjectHolder.commonObject = commonObject;
	}

	public static void setServerObject(MinecraftServerAccess serverObject) {
		MinecraftObjectHolder.serverAccess = serverObject;
	}
}
