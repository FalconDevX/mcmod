package net.mati.mcmod.datagen;

import net.mati.mcmod.block.ModBlocks;
import net.mati.mcmod.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BISMUTH_ORE.get())
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .define('B', ModItems.RAW_BISMUTH.get())
                .unlockedBy("has_raw_bismuth", has(ModItems.RAW_BISMUTH.get()))
                .save(recipeOutput);

        // Recipe: 1 bismuth ore -> 9 raw bismuth
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.RAW_BISMUTH.get(), 9)
                .requires(ModBlocks.BISMUTH_ORE.get())
                .unlockedBy("has_bismuth_ore", has(ModBlocks.BISMUTH_ORE.get()))
                .save(recipeOutput);

    }
}
