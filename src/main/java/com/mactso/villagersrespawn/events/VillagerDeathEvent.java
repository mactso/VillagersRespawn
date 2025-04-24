package com.mactso.villagersrespawn.events;

import java.util.Optional;

import com.mactso.villagersrespawn.config.MyConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class VillagerDeathEvent {
	private static final int[] xpLevels = new int[] { 0, 10, 70, 150, 250 };

	@SubscribeEvent
	public void doVillagerRespawn(LivingDeathEvent event) {

		Entity eventEntity = event.getEntity();
		if (event.getEntity() == null) {
			return;
		}

		if (!(eventEntity.level() instanceof ServerLevel)) {
			return;
		}

		if (eventEntity instanceof Villager) {
			// chance villager will really die
			@SuppressWarnings("resource")  // this is an eclipse issue, not a java issue so suppress warning.
			double randomD100Roll = eventEntity.level().random.nextDouble() * 100;
			randomD100Roll = Math.ceil(randomD100Roll);
			Difficulty difficulty = eventEntity.level().getDifficulty();
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

			if (ve.getLastDamageSource() != null) {
				if (ve.getLastDamageSource().getEntity() instanceof Zombie) {
					if ((ve.level().getDifficulty() == Difficulty.HARD) && (MyConfig.hardModeZombieDeaths))
						return;
				}
			}
			// villager will respawn if they have a bed
			Brain<Villager> vb = ve.getBrain();
			Optional<GlobalPos> villagerHome = vb.getMemory(MemoryModuleType.HOME);
			if (villagerHome.isPresent()) {
				GlobalPos gVHP = villagerHome.get();
				BlockPos villagerHomePos = gVHP.pos();
				BlockState bs = ve.level().getBlockState(villagerHomePos);
				if (bs.getBlock() instanceof BedBlock) {
					Optional<Vec3> standupPos = BedBlock.findStandUpPosition(ve.getType(), ve.level(),
							villagerHomePos, bs.getValue(BedBlock.FACING) , 0);

					if (standupPos.isPresent()) {
						eventEntity.setPos(standupPos.get().x, standupPos.get().y, standupPos.get().z);
						ve.clearFire();
						ve.removeAllEffects();
						ve.setHealth(MyConfig.respawnHealth);
						doRespawnXpLoss(ve);
						event.setCanceled(true);
					}
				}
			}
		}
	}

	private void doRespawnXpLoss(Villager ve) {
		if (MyConfig.respawnXpLoss) {
			int level = ve.getVillagerData().level();
			if (level >= 1) {
				ve.overrideXp(xpLevels[level - 1]);
			}
		}
	}
}
