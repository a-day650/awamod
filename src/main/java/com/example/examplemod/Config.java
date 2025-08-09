package com.example.examplemod;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    // 先声明为final但暂不初始化
    public static final ForgeConfigSpec SPEC;

    // 配置值声明
    public static final ForgeConfigSpec.IntValue THIRST_DECREMENT_INTERVAL;

    static {
        // 构建器必须在静态块内初始化
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        THIRST_DECREMENT_INTERVAL = builder
                .comment("Interval in ticks between thirst decrements (default: 600 = 30 seconds)")
                .defineInRange("thirstDecrementInterval", 600, 100, 24000);

        // 最后初始化SPEC
        SPEC = builder.build();
    }
}
