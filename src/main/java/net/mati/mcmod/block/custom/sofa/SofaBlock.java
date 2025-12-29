package net.mati.mcmod.block.custom.sofa;

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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.Map;

public class SofaBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<SofaPart> PART = EnumProperty.create("part", SofaPart.class);
    public static final MapCodec<SofaBlock> CODEC = simpleCodec(SofaBlock::new);

    //seat
    private static final VoxelShape SEAT = Block.box(0, 0, 0, 16, 5, 16);

    //back
    private static final VoxelShape BACK_NORTH = Block.box(0, 5, 13, 16, 16, 16);
    private static final VoxelShape BACK_SOUTH = Block.box(0, 5, 0, 16, 16, 3);
    private static final VoxelShape BACK_EAST  = Block.box(0, 5, 0, 3, 16, 16);
    private static final VoxelShape BACK_WEST  = Block.box(13, 5, 0, 16, 16, 16);

    //armrest
    private static final VoxelShape ARM_NORTH = Block.box(13, 5, 0, 16, 10, 16);
    private static final VoxelShape ARM_SOUTH = Block.box(0, 5, 0, 3, 10, 16);
    private static final VoxelShape ARM_EAST  = Block.box(0, 5, 13, 16, 10, 16);
    private static final VoxelShape ARM_WEST  = Block.box(0, 5, 0, 16, 10, 3);

    //LEFT_SHAPES
    private static final Map<Direction, VoxelShape> LEFT_SHAPES = Map.of(
            Direction.NORTH, Shapes.or(SEAT, BACK_NORTH, ARM_NORTH),
            Direction.SOUTH, Shapes.or(SEAT, BACK_SOUTH, ARM_SOUTH),
            Direction.EAST,  Shapes.or(SEAT, BACK_EAST,  ARM_EAST),
            Direction.WEST,  Shapes.or(SEAT, BACK_WEST,  ARM_WEST)
    );

    //RIGHT_SHAPES
    private static final Map<Direction, VoxelShape> RIGHT_SHAPES = Map.of(
            Direction.NORTH, Shapes.or(SEAT, BACK_NORTH),
            Direction.SOUTH, Shapes.or(SEAT, BACK_SOUTH),
            Direction.EAST,  Shapes.or(SEAT, BACK_EAST),
            Direction.WEST,  Shapes.or(SEAT, BACK_WEST)
    );

    public SofaBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PART, SofaPart.LEFT));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return state.getValue(PART) == SofaPart.LEFT ? LEFT_SHAPES.get(facing) : RIGHT_SHAPES.get(facing);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction facing = ctx.getHorizontalDirection().getOpposite();
        Direction front = facing.getOpposite();
        BlockPos pos = ctx.getClickedPos();
        BlockPos secondPos = pos.relative(front.getClockWise());

        if (ctx.getLevel().getBlockState(secondPos).canBeReplaced(ctx) && ctx.getLevel().getWorldBorder().isWithinBounds(secondPos)) {
            return this.defaultBlockState().setValue(FACING, facing).setValue(PART, SofaPart.LEFT);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide) {
            Direction facing = state.getValue(FACING);
            Direction front = facing.getOpposite();
            BlockPos secondPos = pos.relative(front.getClockWise());
            level.setBlock(secondPos, state.setValue(PART, SofaPart.RIGHT), 3);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        Direction facing = state.getValue(FACING);
        Direction front = facing.getOpposite();
        Direction neighborDir = (state.getValue(PART) == SofaPart.LEFT)
                ? front.getClockWise()
                : front.getCounterClockWise();

        if (direction == neighborDir && !neighborState.is(this)) {
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