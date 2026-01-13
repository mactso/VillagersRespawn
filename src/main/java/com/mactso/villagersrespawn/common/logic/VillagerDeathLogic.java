package com.mactso.villagersrespawn.common.logic;

import java.util.Optional;

import com.mactso.villagersrespawn.modloader.config.MyConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public final class VillagerDeathLogic {

    private static final int[] XP_LEVELS = { 0, 10, 70, 150, 250 };

    private VillagerDeathLogic() {}

    /**
     * @return true if the death should be cancelled (villager respawned)
     */
    public static boolean doVillagerRespawnLogic(ServerLevel level, Villager villager) {

        if (!passesDeathChance(level)) {
            return false;
        }

        if (isHardModeZombieDeath(level, villager)) {
            return false;
        }

        Optional<Vec3> respawnPos = findRespawnPosition(level, villager);
        if (respawnPos.isEmpty()) {
            return false;
        }

        Vec3 pos = respawnPos.get();
        villager.setPos(pos.x, pos.y, pos.z);
        villager.clearFire();
        villager.removeAllEffects();
        villager.setHealth(MyConfig.respawnHealth);

        applyXpLoss(villager);

        return true;
    }

    /* -------------------- helpers -------------------- */

    private static boolean passesDeathChance(ServerLevel level) {
        double roll = Math.ceil(level.random.nextDouble() * 100);
        Difficulty difficulty = level.getDifficulty();

        if (difficulty == Difficulty.NORMAL) roll += 5;
        if (difficulty == Difficulty.HARD) roll += 10;

        return roll <= MyConfig.respawnPercentage;
    }

    private static boolean isHardModeZombieDeath(ServerLevel level, Villager villager) {
        if (!MyConfig.hardModeZombieDeaths) return false;
        if (level.getDifficulty() != Difficulty.HARD) return false;

        return villager.getLastDamageSource() != null
            && villager.getLastDamageSource().getEntity() instanceof Zombie;
    }

    private static Optional<Vec3> findRespawnPosition(ServerLevel level, Villager villager) {
        Brain<Villager> brain = villager.getBrain();
        Optional<GlobalPos> home = brain.getMemory(MemoryModuleType.HOME);

        if (home.isEmpty()) return Optional.empty();

        BlockPos pos = home.get().pos();
        BlockState state = level.getBlockState(pos);

        if (!(state.getBlock() instanceof BedBlock bed)) return Optional.empty();

        return BedBlock.findStandUpPosition(
                villager.getType(),
                level,
                pos,
                state.getValue(BedBlock.FACING),
                0
        );
    }

    private static void applyXpLoss(Villager villager) {
        if (!MyConfig.respawnXpLoss) return;

        int level = villager.getVillagerData().level();
        if (level >= 1 && level <= XP_LEVELS.length) {
            villager.overrideXp(XP_LEVELS[level - 1]);
        }
    }
}
