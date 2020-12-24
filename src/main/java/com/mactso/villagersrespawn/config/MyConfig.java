package com.mactso.villagersrespawn.config;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mactso.villagersrespawn.Main;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = Main.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
// @Mod.EventBusSubscriber(modid = Main.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MyConfig
{
	private static final Logger LOGGER = LogManager.getLogger();
	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;
	static
	{
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static int debugLevel;
	public static int respawnHealth;
	public static boolean respawnXpLoss;
	public static int respawnPercentage;
	public static boolean hardModeZombieDeaths;
	
	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent)
	{
		if (configEvent.getConfig().getSpec() == MyConfig.COMMON_SPEC)
		{
			bakeConfig();
		}
	}

	public static void bakeConfig()
	{
		debugLevel = COMMON.debugLevel.get();
		respawnHealth = COMMON.respawnHealth.get();
		respawnXpLoss = COMMON.respawnXpLoss.get();
		respawnPercentage = COMMON.respawnPercentage.get();
		hardModeZombieDeaths = COMMON.hardModeZombieDeaths .get();

	}


	public static class Common
	{

		public final IntValue debugLevel;
		public final IntValue respawnHealth;
		public final BooleanValue respawnXpLoss;	
		public final IntValue respawnPercentage;
		public final BooleanValue hardModeZombieDeaths;	
		
		public Common(ForgeConfigSpec.Builder builder)
		{
			builder.push("Villager Respawn Control Values");

			debugLevel = builder
					.comment("Debug Level: 0 = Off, 1 = Log, 2 = Chat+Log")
					.translation(Main.MODID + ".config." + "debugLevel")
					.defineInRange("debugLevel", () -> 0, 0, 2);
			
			respawnHealth = builder
					.comment("Respawn Health")
					.translation(Main.MODID + ".config." + "respawnHealth")
					.defineInRange("respawnHealth", () -> 20, 1, 25);

			respawnXpLoss = builder
					.comment("Respawn XpLoss")
					.translation(Main.MODID + ".config." + "respawnXpLoss")
					.define ("respawnXpLoss", () -> true);
			
			respawnPercentage = builder
					.comment("Respawn Percentage")
					.translation(Main.MODID + ".config." + "respawnPercentage")
					.defineInRange("respawnPercentage", () -> 104, 1, 110);

			hardModeZombieDeaths = builder
					.comment("Zombie Deaths In Hard Mode")
					.translation(Main.MODID + ".config." + "hardModeZombieDeaths")
					.define ("hardModeZombieDeaths", () -> true);
			
			builder.pop();
		}
	}
}
