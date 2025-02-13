package com.mactso.villagersrespawn.events;

import java.util.Optional;

import com.mactso.villagersrespawn.config.MyConfigs;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class VillagerDeathEvent {
	private static final int[] xpLevels = new int[] { 0, 10, 70, 150, 250 };

	public static boolean doVillagerRespawn(LivingEntity eventEntity, DamageSource d) {
		
		
		if (!(eventEntity instanceof Villager)) {
			return false;
		}
		
		if ((eventEntity.level().isClientSide())) {
			return false;
		}
		// chance villager will really die
		
		double randomD100Roll = eventEntity.level().getRandom().nextDouble() * 100;
		randomD100Roll = Math.ceil(randomD100Roll);
		Difficulty difficulty = eventEntity.level().getDifficulty();
		if (difficulty == Difficulty.NORMAL) {
			randomD100Roll += 5.0;
		}
		if (difficulty == Difficulty.HARD) {
			randomD100Roll += 10.0;
		}
		if (randomD100Roll > MyConfigs.getRespawnPercentage()) {
			return false;
		}

		Villager ve = (Villager) eventEntity;

		// if zombie deaths true, will still become zombies in HARD mode
		String deathMessage = "Died for unknown reasons.";
		int deathX, deathY, deathZ;
		
		if (ve.getLastDamageSource() != null) {
			deathMessage = ve.getLastDamageSource().getLocalizedDeathMessage(ve).toString();
			
			if (d.getEntity()   instanceof Zombie) {
				if ((ve.level().getDifficulty() == Difficulty.HARD) && (MyConfigs.getHardModeZombieDeaths()))
					return false;
			}
		}
		// villager will respawn if they have a bed


		Brain<Villager> vBrain = ve.getBrain();
		Optional<GlobalPos> villagerHome = vBrain.getMemory(MemoryModuleType.HOME);
		if (villagerHome.isPresent()) {
			GlobalPos gVHP = villagerHome.get();
			BlockPos villagerHomePos = gVHP.pos();
			BlockState bs = ve.level().getBlockState(villagerHomePos);
			if (bs.getBlock() instanceof BedBlock) {
				Optional<Vec3> standupPos = BedBlock.findStandUpPosition(ve.getType(), ve.level(), villagerHomePos, bs.getValue(BedBlock.FACING), 0);
				if (standupPos.isPresent()) {
					eventEntity.setPos(villagerHomePos.getX(), villagerHomePos.getY(), villagerHomePos.getZ());
					ve.clearFire();
					ve.removeAllEffects();
					ve.setHealth(MyConfigs.getRespawnHealth());

					doRespawnXpLoss(ve);
					return true;
					
				}
			}
		}
		return false;
	}

	private static void doRespawnXpLoss(Villager ve) {
		if (MyConfigs.getRespawnXpLoss()) {
			int level = ve.getVillagerData().getLevel();
			ve.overrideXp(xpLevels[level - 1]);
		}
	}
}
