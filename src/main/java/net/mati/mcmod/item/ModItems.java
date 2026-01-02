package net.mati.mcmod.item;

import net.mati.mcmod.McMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(McMod.MODID);

    public static final DeferredItem<Item> RAW_BISMUTH = ITEMS.register("raw_bismuth", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CABINET = ITEMS.register("cabinet",
            () -> new BlockItem(net.mati.mcmod.block.ModBlocks.CABINET.get(), new Item.Properties()));

    public static final DeferredItem<Item> SOFA = ITEMS.register("sofa",
            () -> new BlockItem(net.mati.mcmod.block.ModBlocks.SOFA.get(), new Item.Properties()));

    public static final DeferredItem<Item> DESK = ITEMS.register("desk",
            () -> new BlockItem(net.mati.mcmod.block.ModBlocks.DESK.get(), new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
