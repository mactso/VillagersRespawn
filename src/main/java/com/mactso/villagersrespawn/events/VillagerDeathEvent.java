package com.mactso.villagersrespawn.events;

import java.util.Optional;

import com.mactso.villagersrespawn.config.MyConfig;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.Difficulty;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class VillagerDeathEvent {
	private static final int[] xpLevels = new int[]{0, 10, 70, 150, 250};
	@SubscribeEvent
    public void doVillagerRespawn(LivingDeathEvent event) { 
		
		Entity eventEntity = event.getEntity();
		if (event.getEntity() == null) {
			return;
		}
		
		if (!(eventEntity.level instanceof ServerLevel)) {
			return;
		}
		
		if (eventEntity instanceof Villager) {
			// chance villager will really die
			double randomD100Roll = eventEntity.level.random.nextDouble() * 100;
			randomD100Roll = Math.ceil(randomD100Roll); 
			Difficulty difficulty = eventEntity.level.getDifficulty();
			if (difficulty == Difficulty.NORMAL) {
				randomD100Roll += 5.0;
			}
			if (difficulty == Difficulty.HARD) {
				randomD100Roll += 10.0;
			}
			if (randomD100Roll > MyConfig.respawnPercentage) {
				return;
			}

			Villager ve = (Villager) eventEntity;
			
			// if zombie deaths true, will still become zombies in HARD mode
			String deathMessage="Died for unknown reasons.";
			int deathX, deathY, deathZ;
			if (ve.getLastDamageSource() != null) {
				deathMessage = ve.getLastDamageSource().getLocalizedDeathMessage(ve).toString();
				if (ve.getLastDamageSource().getEntity() instanceof Zombie) {
					if ((ve.level.getDifficulty()== Difficulty.HARD ) && (MyConfig.hardModeZombieDeaths))
					return;
				}
			}
			//villager will respawn if they have a bed
			Brain<Villager> vb =  ve.getBrain();
			Optional<GlobalPos> villagerHome = vb.getMemory(MemoryModuleType.HOME);
			if (villagerHome.isPresent()) {
				GlobalPos gVHP = villagerHome.get();
				BlockPos villagerHomePos = gVHP.pos();
				deathX = (int) ve.getX(); deathY = (int) ve.getY(); deathZ = (int) ve.getZ();
				eventEntity.setPos(villagerHomePos.getX(), villagerHomePos.getY(), villagerHomePos.getZ() );
				ve.clearFire();
				ve.setHealth(MyConfig.respawnHealth);

				if (MyConfig.respawnXpLoss) {

					int level = ve.getVillagerData().getLevel();
					ve.overrideXp(xpLevels[level-1]);

				}
				
				if (MyConfig.debugLevel > 0) {
					
					System.out.println("VillagersRespawn: Villager " 
								+ deathMessage );
					System.out.println(  
							" at " + deathX +", " + deathY +", " + deathZ + ".");

					System.out.println(
							" Respawned at " + ve.getX() +", " + ve.getY() +", " + ve.getZ() + ".");

				}
				event.setCanceled(true);
			}
		}
	}
}
 