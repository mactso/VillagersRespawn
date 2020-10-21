// 16.2 - 1.0.0.0 Villager Respawn
package com.mactso.villagersrespawn;


import com.mactso.villagersrespawn.config.MyConfig;
import com.mactso.villagersrespawn.events.VillagerDeathEvent;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("villagersrespawn")
public class Main {

	    public static final String MODID = "villagersrespawn"; 
	    
	    public Main()
	    {

			FMLJavaModLoadingContext.get().getModEventBus().register(this);
	        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER,MyConfig.SERVER_SPEC );
//			MinecraftForge.EVENT_BUS.register(this);
	    }

	    // Register ourselves for server and other game events we are interested in
		@SubscribeEvent 
		public void preInit (final FMLCommonSetupEvent event) {
			System.out.println("Villager Respawn: Registering Handler");
			MinecraftForge.EVENT_BUS.register(new VillagerDeathEvent());
			
		}       

//		// in 14.4 and later, config file loads when the server starts when the world starts.
//		@SubscribeEvent 
//		public void onServerStarting (FMLServerStartingEvent event) {
////			VillagerRespawnCommands.register(event.getCommandDispatcher());
//		}
}