package com.example.examplemod.thirst;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ThirstData extends SavedData {
    private final Map<UUID, Integer> thirstMap = new HashMap<>();

    // 获取或创建数据
    public static ThirstData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                ThirstData::load,
                ThirstData::new,
                "thirst_data"
        );
    }

    // 从NBT加载
    public static ThirstData load(CompoundTag nbt) {
        ThirstData data = new ThirstData();
        ListTag list = nbt.getList("thirst", Tag.TAG_COMPOUND);
        for (Tag tag : list) {
            CompoundTag entry = (CompoundTag) tag;
            data.thirstMap.put(
                    entry.getUUID("uuid"),
                    entry.getInt("value")
            );
        }
        return data;
    }

    // 保存到NBT
    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag list = new ListTag();
        thirstMap.forEach((uuid, value) -> {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("uuid", uuid);
            entry.putInt("value", value);
            list.add(entry);
        });
        nbt.put("thirst", list);
        return nbt;
    }

    // 数据操作方法
    public Map<UUID, Integer> getThirstData() {
        return new HashMap<>(thirstMap);
    }

    public void setThirstData(Map<UUID, Integer> data) {
        thirstMap.clear();
        thirstMap.putAll(data);
        setDirty(); // 标记需要保存
    }
}