package com.mactso.villagersrespawn.modloader.events;

import com.mactso.villagersrespawn.common.logic.VillagerDeathLogic;
import com.mactso.villagersrespawn.modloader.main.Main;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(modid = Main.MODID)
public class VillagerDeathEvent {

    @SubscribeEvent
    public static void handleVillagerDeath(LivingDeathEvent event) {

        Entity entity = event.getEntity();
        if (!(entity instanceof Villager villager)) {
            return;
        }

        if (!(entity.level() instanceof ServerLevel level)) {
            return;
        }

        boolean respawned =
                VillagerDeathLogic.doVillagerRespawnLogic(level, villager);

        if (respawned) {
            event.setCanceled(true);
        }
    }
}