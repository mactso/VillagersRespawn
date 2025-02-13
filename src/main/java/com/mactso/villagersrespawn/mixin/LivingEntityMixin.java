package com.mactso.villagersrespawn.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mactso.villagersrespawn.events.VillagerDeathEvent;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;


@Mixin(LivingEntity.class)
	abstract class LivingEntityMixin {

	    @Inject(method = "die ", at = @At("HEAD"), cancellable = true)
	    private void onEntityKilled(DamageSource source, CallbackInfo ci) {
	        if (VillagerDeathEvent.doVillagerRespawn((LivingEntity) (Object) this, source)) {
	            ci.cancel();
	        }
	    }
	}
	

