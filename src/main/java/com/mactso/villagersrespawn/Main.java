// 16.2 - 1.0.0.0 Villager Respawn
package com.mactso.villagersrespawn;


import com.mactso.villagersrespawn.config.MyConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("villagersrespawn")
public class Main {

	    public static final String MODID = "villagersrespawn"; 
	    
	    public Main(FMLJavaModLoadingContext context)
	    {

			context.registerConfig(ModConfig.Type.COMMON, MyConfig.COMMON_SPEC);

	    }

    

}