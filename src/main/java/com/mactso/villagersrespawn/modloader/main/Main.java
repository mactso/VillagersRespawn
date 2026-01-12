// 16.2 - 1.0.0.0 Villager Respawn
package com.mactso.villagersrespawn.modloader.main;


import com.mactso.villagersrespawn.modloader.config.MyConfig;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;



@Mod("villagersrespawn")
public class Main {

	    public static final String MODID = "villagersrespawn"; 
	    
	    public Main(IEventBus modEventBus, ModContainer modContainer)
	    {
	        modEventBus.register(this);
			modContainer.registerConfig(ModConfig.Type.COMMON, MyConfig.COMMON_SPEC);

	    }

		@SubscribeEvent 
		public void preInit (final FMLCommonSetupEvent event) {
//			NeoForge.EVENT_BUS.register(new EventHandler());
		}       

}
