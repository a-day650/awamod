package com.example.examplemod.thirst;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ThirstManager {
    private static final Map<UUID, Integer> PLAYER_THIRST = new HashMap<>();
    private static final int MAX_THIRST = 20;

    // 获取玩家口渴值
    public static int getThirst(Player player) {
        return PLAYER_THIRST.getOrDefault(player.getUUID(), MAX_THIRST);
    }

    // 设置玩家口渴值 (自动限制在0-MAX_THIRST之间)
    public static void setThirst(Player player, int amount) {
        PLAYER_THIRST.put(player.getUUID(), Math.max(0, Math.min(amount, MAX_THIRST)));
    }

    // 增加口渴值
    public static void addThirst(Player player, int amount) {
        setThirst(player, getThirst(player) + amount);
    }

    // 减少口渴值
    public static void subtractThirst(Player player, int amount) {
        setThirst(player, getThirst(player) - amount);
    }

    // 重置所有数据 (用于玩家退出时)
    public static void reset(UUID playerId) {
        PLAYER_THIRST.remove(playerId);
    }

    // 在ThirstManager中添加
    public static void save(ServerLevel level) {
        ThirstData data = ThirstData.get(level);
        data.setThirstData(PLAYER_THIRST);
        data.setDirty();
    }

    public static void load(ServerLevel level) {
        PLAYER_THIRST.clear();
        PLAYER_THIRST.putAll(ThirstData.get(level).getThirstData());
    }

    public static void saveToWorld(ServerLevel level) {
        ThirstData data = ThirstData.get(level);
        data.setThirstData(PLAYER_THIRST);
    }

    // 从世界加载数据
    public static void loadFromWorld(ServerLevel level) {
        PLAYER_THIRST.clear();
        PLAYER_THIRST.putAll(ThirstData.get(level).getThirstData());
    }
}