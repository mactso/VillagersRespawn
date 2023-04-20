package com.mactso.villagersrespawn.events;

import java.util.Optional;

import com.mactso.villagersrespawn.config.MyConfigs;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;

public class VillagerDeathEvent {
	private static final int[] xpLevels = new int[] { 0, 10, 70, 150, 250 };

	public static boolean doVillagerRespawn(LivingEntity eventEntity, DamageSource d) {

		if (!(eventEntity.world instanceof ServerWorld)) {
			return false;
		}

		if (!(eventEntity instanceof VillagerEntity)) {
			return false;
		}

		// chance villager will really die
		double randomD100Roll = eventEntity.world.random.nextDouble() * 100;
		randomD100Roll = Math.ceil(randomD100Roll);
		Difficulty difficulty = eventEntity.world.getDifficulty();
		if (difficulty == Difficulty.NORMAL) {
			randomD100Roll += 5.0;
		}
		if (difficulty == Difficulty.HARD) {
			randomD100Roll += 10.0;
		}
		if (randomD100Roll > MyConfigs.getRespawnPercentage()) {
			return false;
		}

		VillagerEntity ve = (VillagerEntity) eventEntity;

		// if zombie deaths true, will still become zombies in HARD mode
		String deathMessage = "Died for unknown reasons.";
		int deathX, deathY, deathZ;
		if (ve.getRecentDamageSource() != null) {
			deathMessage = ve.getRecentDamageSource().getDeathMessage(ve).toString();
			if (ve.getRecentDamageSource().getAttacker() instanceof ZombieEntity) {
				if ((ve.world.getDifficulty() == Difficulty.HARD) && (MyConfigs.getHardModeZombieDeaths()))
					return false;
			}
		}
		// villager will respawn if they have a bed

		Optional<GlobalPos> vhome;
		if ((ve.getBrain().getOptionalMemory(MemoryModuleType.HOME)).isPresent()) {
			vhome = ve.getBrain().getOptionalMemory(MemoryModuleType.HOME); 
			GlobalPos gVHP = vhome.get();
			BlockPos villagerHomePos = gVHP.getPos();
			BlockState bs = ve.getWorld().getBlockState(villagerHomePos);
			if (bs.getBlock() instanceof BedBlock) {
				Optional<Vec3d> standupPos = BedBlock.findWakeUpPosition(ve.getType(), ve.getWorld(), villagerHomePos, bs.get(BedBlock.FACING), 0);
				if (standupPos.isPresent()) {
					eventEntity.setPos(villagerHomePos.getX(), villagerHomePos.getY(), villagerHomePos.getZ());
					ve.extinguish();
					ve.clearStatusEffects();
					ve.setHealth(MyConfigs.getRespawnHealth());

					doRespawnXpLoss(ve);
					return true;
					
				}
			}
		}
		return false;
	}

	private static void doRespawnXpLoss(VillagerEntity ve) {
		if (MyConfigs.getRespawnXpLoss()) {
			int level = ve.getVillagerData().getLevel();
			ve.setExperience(xpLevels[level - 1]);
		}
	}
}
