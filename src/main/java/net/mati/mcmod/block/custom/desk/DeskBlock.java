package net.mati.mcmod.block.custom.desk;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DeskBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<DeskPart> PART = EnumProperty.create("part", DeskPart.class);
    public static final MapCodec<DeskBlock> CODEC = simpleCodec(DeskBlock::new);

    private static final VoxelShape SHAPE_LEFT = Block.box(0, 0, 0, 16, 16, 16);
    private static final VoxelShape SHAPE_MIDDLE = Block.box(0, 0, 0, 16, 16, 16);
    private static final VoxelShape SHAPE_RIGHT = Block.box(0, 0, 0, 16, 16, 16);

    public DeskBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, DeskPart.MIDDLE));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        DeskPart part = state.getValue(PART);
        return switch (part) {
            case LEFT -> SHAPE_LEFT;
            case MIDDLE -> SHAPE_MIDDLE;
            case RIGHT -> SHAPE_RIGHT;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction facing = ctx.getHorizontalDirection();

        Direction rightDir = facing.getClockWise();
        Direction leftDir = facing.getCounterClockWise();

        BlockPos pos = ctx.getClickedPos();
        BlockPos leftPos = pos.relative(leftDir);
        BlockPos rightPos = pos.relative(rightDir);

        if (canPlaceAt(ctx, leftPos) && canPlaceAt(ctx, rightPos)) {
            return this.defaultBlockState().setValue(FACING, facing).setValue(PART, DeskPart.MIDDLE);
        }
        return null;
    }

    private boolean canPlaceAt(BlockPlaceContext ctx, BlockPos pos) {
        return ctx.getLevel().getBlockState(pos).canBeReplaced(ctx)
                && ctx.getLevel().getWorldBorder().isWithinBounds(pos);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide) {
            Direction rightDir = state.getValue(FACING).getClockWise();
            Direction leftDir = state.getValue(FACING).getCounterClockWise();

            level.setBlock(pos.relative(leftDir), state.setValue(PART, DeskPart.LEFT), 3);
            level.setBlock(pos.relative(rightDir), state.setValue(PART, DeskPart.RIGHT), 3);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        Direction facing = state.getValue(FACING);
        DeskPart part = state.getValue(PART);
        Direction rightDir = facing.getClockWise();
        Direction leftDir = facing.getCounterClockWise();

        boolean shouldBreak = false;
        if (part == DeskPart.LEFT && direction == rightDir && !neighborState.is(this)) {
            shouldBreak = true;
        } else if (part == DeskPart.MIDDLE) {
            if ((direction == leftDir || direction == rightDir) && !neighborState.is(this)) {
                shouldBreak = true;
            }
        } else if (part == DeskPart.RIGHT && direction == leftDir && !neighborState.is(this)) {
            shouldBreak = true;
        }

        if (shouldBreak) {
            return Blocks.AIR.defaultBlockState();
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}