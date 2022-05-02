package io.github.frqnny.darkenchanting.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;

//Due to horrible vanilla methods, these should be used instead when dealing with xp
public class PlayerUtils {

    public static void syncExperience(PlayerEntity player) {
        syncExperience(player, getTotalExperience(player));
    }

    public static void syncExperience(PlayerEntity player, int totalExperience) {
        player.addExperience(0);
        player.addExperienceLevels(0);

        player.totalExperience = totalExperience;

        if (player instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.addExperience(0);
        }

    }

    public static int syncAndGetTotalExperience(PlayerEntity player) {
        int totalExperience = getTotalExperience(player);
        syncExperience(player, totalExperience);

        return totalExperience;
    }

    public static int getTotalExperience(PlayerEntity player) {
        int experienceLevel = player.experienceLevel;
        int experienceFromFullLevels = 0;

        //calculate all full level experience
        for (int currentLevel = (experienceLevel -1); currentLevel >= 0; currentLevel--) {
            if (currentLevel >= 30) {
                experienceFromFullLevels += 112 + (currentLevel - 30) * 9;
            } else {
                experienceFromFullLevels += currentLevel >= 15 ? 37 + (currentLevel - 15) * 5 : 7 + currentLevel * 2;
            }
        }

        //calculate the last level, which is partial
        float nextLevelExperience = player.getNextLevelExperience();
        nextLevelExperience *= player.experienceProgress;
        experienceFromFullLevels += nextLevelExperience;

        //this now contains what should be an accurate measure of total experience
        //based on experienceProgress and experienceLevel rather than desynced
        //totalExperience which tends to be bad measure of xp and leads to bugs
        return experienceFromFullLevels;
    }

    //supports negative and positive operations
    //PlayerEntity#experience only supports positive
    public static void modifyExperience(PlayerEntity player, int experience) {

        player.addScore(Math.abs(experience));
        player.totalExperience = MathHelper.clamp(player.totalExperience + experience, 0, Integer.MAX_VALUE);

        //recalculate player level and experience progress
        int playerLevel = 0;
        int countExperience = player.totalExperience;
        int currentLevelExperience = getExperienceNeededForLevel(0);
        while (countExperience > currentLevelExperience) {
            playerLevel++;
            countExperience -= currentLevelExperience;
            currentLevelExperience = getExperienceNeededForLevel(playerLevel);
        }

        player.experienceLevel = playerLevel;
        player.experienceProgress = (float) countExperience / (float) getExperienceNeededForLevel(playerLevel);

        if (player instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.addExperience(0);
        }
    }

    //returns the xp needed to go up by 1 level from a current level, assuming experienceProgress is 0.0
    public static int getExperienceNeededForLevel(int experienceLevel) {
        if (experienceLevel >= 30) {
            return 112 + (experienceLevel - 30) * 9;
        } else {
            return experienceLevel >= 15 ? 37 + (experienceLevel - 15) * 5 : 7 + experienceLevel * 2;
        }
    }
}
