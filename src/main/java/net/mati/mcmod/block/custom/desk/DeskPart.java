package net.mati.mcmod.block.custom.desk;

import net.minecraft.util.StringRepresentable;

public enum DeskPart implements StringRepresentable {
    LEFT,
    MIDDLE,
    RIGHT;

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}
