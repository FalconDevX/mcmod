package net.mati.mcmod.item;

import net.mati.mcmod.McMod;
import net.mati.mcmod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.awt.*;
import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, McMod.MODID);

    public static final Supplier<CreativeModeTab> BISMUTH_ITEMS_TAB = CREATIVE_MODE_TAB.register("bismuth_items_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModItems.RAW_BISMUTH.get()))
                    .title(Component.translatable("creativetab.mcmod.bismuth_items"))
                    .displayItems((itemDisplayParameters,output) -> {
                        output.accept(ModItems.RAW_BISMUTH);
                    }).build());

    public static final Supplier<CreativeModeTab> BISMUTH_BLOCKS_TAB = CREATIVE_MODE_TAB.register("bismuth_blocks_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModBlocks.BISMUTH_ORE))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(McMod.MODID, "bismuth_items_tab"))
                    .title(Component.translatable("creativetab.mcmod.bismuth_blocks"))
                    .displayItems((itemDisplayParameters,output) -> {
                        output.accept(ModBlocks.BISMUTH_ORE);
                        output.accept(ModBlocks.BISMUTH_DOOR);
                    }).build());

    public static final Supplier<CreativeModeTab> VINTAGE_FURNITURES = CREATIVE_MODE_TAB.register("vintage_furnitures",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.CABINET))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(McMod.MODID, "bismuth_blocks_tab"))
                    .title(Component.translatable("creativetab.mcmod.vintage_furnitures"))
                    .displayItems((itemDisplayParameters,output) -> {
                        output.accept(ModBlocks.CABINET);
                        output.accept(ModBlocks.SOFA);
                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
