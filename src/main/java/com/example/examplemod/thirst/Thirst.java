package com.example.examplemod.thirst;

import net.minecraft.util.Mth;

public class Thirst {
    private int thirst = 20;

    public int getThirst() { return thirst; }
    public void setThirst(int value) { this.thirst = Mth.clamp(value, 0, 20); }
}