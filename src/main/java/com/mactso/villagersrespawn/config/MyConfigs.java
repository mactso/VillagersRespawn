package com.mactso.villagersrespawn.config;
import java.util.HashSet;

import com.mactso.villagersrespawn.Main;
import com.mojang.datafixers.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyConfigs {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;

	public static int debugLevel;
	public static int respawnHealth;
	public static String respawnXpLoss;	
	public static int respawnPercentage;
	public static String hardModeZombieDeaths;	
	
	public static int getDebugLevel() {
		return debugLevel;
	}

	public static void setDebugLevel(int debugLevel) {
		MyConfigs.debugLevel = debugLevel;
	}

	public static int getRespawnHealth() {
		return respawnHealth;
	}

	public static boolean getRespawnXpLoss() {
		if (respawnXpLoss.equals("true")) {
			return true;
		}
		return false;
	}

	public static int getRespawnPercentage() {
		return respawnPercentage;
	}

	public static boolean getHardModeZombieDeaths() {
		if (hardModeZombieDeaths.equals("true")) {
			return true;
		}
		return false;
	}

	public static HashSet<String> getModStringSet (String[] values) {
		HashSet<String> returnset = new HashSet<>();
		// Collection<ModContainer> loadedMods= FabricLoader.getAllMods();  error static calling non-static.
		HashSet<String> loadedset = new HashSet<>();
		loadedset.add("respawnvillager");
		loadedset.add("test");

		for (String s : loadedset) {
			String s2 = s.trim().toLowerCase();
			if (!s2.isEmpty()) {
				if (!returnset.contains(s2)) {
					returnset.add(s2);
				} else {
					LOGGER.warn("spawnbalanceutility includedReportModsSet entry : " +s2 + " is not a valid current loaded forge mod.");
				} 
			}
		}
		return returnset;
	}
	
	public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(Main.MOD_ID + "config").provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair<>("key.debugLevel", 0), "int");	
        configs.addKeyValuePair(new Pair<>("key.respawnHealth", 20), "int");	
        configs.addKeyValuePair(new Pair<>("key.respawnXpLoss", "true"), "String");
        configs.addKeyValuePair(new Pair<>("key.respawnPercentage", 104), "int");
        configs.addKeyValuePair(new Pair<>("key.hardModeZombieDeaths", "true"), "String");
    }

    private static void assignConfigs() {
    	debugLevel = CONFIG.getOrDefault("key.debugLevel", 0);
    	respawnHealth = CONFIG.getOrDefault("key.respawnHealth", 20);
    	respawnXpLoss = CONFIG.getOrDefault("key.respawnXpLoss", "true");	
    	respawnPercentage = CONFIG.getOrDefault("key.respawnPercentage", 104);
    	hardModeZombieDeaths = CONFIG.getOrDefault("key.hardModeZombieDeaths", "true");	
        LOGGER.info("All " + configs.getConfigsList().size() + " have been set properly");
    }
}
