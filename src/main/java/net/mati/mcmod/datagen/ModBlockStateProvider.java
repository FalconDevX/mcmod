package net.mati.mcmod.datagen;

import net.mati.mcmod.McMod;
import net.mati.mcmod.block.ModBlocks;
import net.mati.mcmod.block.custom.sofa.SofaBlock;
import net.mati.mcmod.block.custom.sofa.SofaPart;
import net.mati.mcmod.block.custom.desk.DeskPart;
import net.mati.mcmod.block.custom.desk.DeskBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, McMod.MODID, exFileHelper);
    }

    private void sofaBlock() {
        getVariantBuilder(ModBlocks.SOFA.get())
                .forAllStates(state -> {
                    Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
                    SofaPart part = state.getValue(SofaBlock.PART);

                    String model = part == SofaPart.LEFT
                            ? "block/sofa_left"
                            : "block/sofa_right";

                    return ConfiguredModel.builder()
                            .modelFile(models().getExistingFile(modLoc(model)))
                            .rotationY(((int) facing.toYRot()) % 360)
                            .build();
                });
    }
    
    //left right middle desk
    private void deskBlock() {
        getVariantBuilder(ModBlocks.DESK.get())
                .forAllStates(state -> {
                    Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
                    DeskPart part = state.getValue(DeskBlock.PART);

                    String model = part == DeskPart.LEFT
                            ? "block/desk_left"
                            : part == DeskPart.MIDDLE
                            ? "block/desk_middle"
                            : "block/desk_right";

                    return ConfiguredModel.builder()
                            .modelFile(models().getExistingFile(modLoc(model)))
                            .rotationY(((int) facing.toYRot()) % 360)
                            .build();
                });
    }


    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.BISMUTH_ORE);

        horizontalBlock(ModBlocks.CABINET.get(),
                models().getExistingFile(modLoc("block/cabinet")));

        sofaBlock();
        deskBlock();
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock){
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
