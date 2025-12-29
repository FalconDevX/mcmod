package net.mati.mcmod.block.custom.sofa;

import net.minecraft.util.StringRepresentable;

public enum SofaPart implements StringRepresentable {
    LEFT,
    RIGHT;

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}
