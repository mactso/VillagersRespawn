package com.mactso.villagersrespawn.events;

import java.util.Optional;

import com.mactso.villagersrespawn.config.MyConfig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class VillagerDeathEvent {

	@SubscribeEvent
    public void doVillagerRespawn(LivingDeathEvent event) { 
		Entity eventEntity = event.getEntityLiving();
		if (event.getEntity() == null) {
			return;
		}
		
		if (!(eventEntity.world instanceof ServerWorld)) {
			return;
		}
		
		if (eventEntity instanceof VillagerEntity) {
			VillagerEntity ve = (VillagerEntity) eventEntity;
			Brain<VillagerEntity> vb =  ve.getBrain();
			Optional<GlobalPos> villagerHome = vb.getMemory(MemoryModuleType.HOME);
			if (villagerHome.isPresent()) {
				GlobalPos gVHP = villagerHome.get();
				BlockPos villagerHomePos = gVHP.getPos();
				eventEntity.setPosition(villagerHomePos.getX(), villagerHomePos.getY(), villagerHomePos.getZ() );
				ve.extinguish();
				ve.setHealth(MyConfig.respawnHealth);
				event.setCanceled(true);
			}
		}
	}
}
 