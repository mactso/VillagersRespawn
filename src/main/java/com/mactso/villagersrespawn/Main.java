// 16.2 - 1.0.0.0 Villager Respawn
package com.mactso.villagersrespawn;


import com.mactso.villagersrespawn.config.ModConfigs;

import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {

	    public static final String MOD_ID = "villagersrespawn"; 
	    
		@Override
		public void onInitialize() {

			ModConfigs.registerConfigs();


		}
}