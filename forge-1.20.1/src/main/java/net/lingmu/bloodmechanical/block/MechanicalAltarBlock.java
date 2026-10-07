package net.lingmu.bloodmechanical.block;

import net.lingmu.bloodmechanical.blockentity.MechanicalAltarBlockEntity;
import net.lingmu.bloodmechanical.menu.MechanicalAltarMenu;
import net.lingmu.bloodmechanical.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import wayoftime.bloodmagic.common.block.BlockAltar;

public class MechanicalAltarBlock extends BlockAltar {
    public MechanicalAltarBlock() { super(); }

    @Override
    public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level,
                               BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return Shapes.block();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MechanicalAltarBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModBlockEntities.MECHANICAL_ALTAR.get() ? MechanicalAltarBlockEntity::tick : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer,
                    new SimpleMenuProvider((id, inventory, user) -> new MechanicalAltarMenu(id, inventory, pos),
                            Component.translatable("container.bloodmechanical.mechanical_altar")),
                    buffer -> buffer.writeBlockPos(pos));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
