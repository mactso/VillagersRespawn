package com.mactso.villagersrespawn.world;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.LegacySingleJigsawPiece;
import net.minecraft.world.gen.feature.structure.DesertVillagePools;
import net.minecraft.world.gen.feature.structure.PlainsVillagePools;
import net.minecraft.world.gen.feature.structure.SavannaVillagePools;
import net.minecraft.world.gen.feature.structure.SnowyVillagePools;
import net.minecraft.world.gen.feature.structure.TaigaVillagePools;
import net.minecraftforge.coremod.api.ASMAPI;

public class ModVillage
{
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static void updatePools()
	{
		String[] biomeList = {"plains", "snowy", "savanna", "desert", "taiga"};
		PlainsVillagePools.init();
		SnowyVillagePools.init();
		SavannaVillagePools.init();
		DesertVillagePools.init();
		TaigaVillagePools.init();

		try {
			String name = ASMAPI.mapField("field_214953_e"); // jigsawPieces
			Field field = JigsawPattern.class.getDeclaredField(name);
			field.setAccessible(true);
			for (String biomeName : biomeList)
			{
				String baseName = "village/" + biomeName + "/houses";
				LegacySingleJigsawPiece piece = new LegacySingleJigsawPiece("oldgods:" + baseName + "/" + biomeName + "_ender_god");

				System.out.println ("Adding oldgods:" + baseName + "/" + biomeName + "_ender_god");
				
				JigsawPattern pattern = JigsawManager.REGISTRY.get(new ResourceLocation("minecraft:" + baseName));
				List<JigsawPiece> list = (List<JigsawPiece>) field.get(pattern);
				list.add(piece);
				list.add(piece);
				list.add(piece);
				list.add(piece);
				list.add(piece);
				list.add(piece);
				list.add(piece);
				list.add(piece);
				list.add(piece);
				list.add(piece);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
