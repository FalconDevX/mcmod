package net.mati.mcmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SofaBlock extends HorizontalDirectionalBlock {

    // WYMAGANE przez NeoForge 1.21+
    public static final MapCodec<SofaBlock> CODEC =
            Block.simpleCodec(SofaBlock::new);

    private static final VoxelShape SEAT =
            Block.box(0, 0, 0, 16, 5, 16);

    // NORTH (oparcie z tyłu)
    private static final VoxelShape BACK_NORTH =
            Block.box(0, 5, 13, 16, 17, 16);

    // SOUTH
    private static final VoxelShape BACK_SOUTH =
            Block.box(0, 5, 0, 16, 17, 3);

    // WEST
    private static final VoxelShape BACK_WEST =
            Block.box(13, 5, 0, 16, 17, 16);

    // EAST
    private static final VoxelShape BACK_EAST =
            Block.box(0, 5, 0, 3, 17, 16);

    private static final VoxelShape NORTH =
            Shapes.or(SEAT, BACK_NORTH);

    private static final VoxelShape SOUTH =
            Shapes.or(SEAT, BACK_SOUTH);

    private static final VoxelShape WEST =
            Shapes.or(SEAT, BACK_WEST);

    private static final VoxelShape EAST =
            Shapes.or(SEAT, BACK_EAST);


    public SofaBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH));
    }

    // IMPLEMENTACJA WYMAGANA
    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level,
                               BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH;
            case WEST  -> WEST;
            case EAST  -> EAST;
            default    -> NORTH;
        };
    }
}
