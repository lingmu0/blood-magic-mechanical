package net.lingmu.bloodmechanical.blockentity;

import net.lingmu.bloodmechanical.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import wayoftime.bloodmagic.common.tile.TileAltar;
import wayoftime.bloodmagic.impl.BloodMagicAPI;
import wayoftime.bloodmagic.recipe.RecipeBloodAltar;

import javax.annotation.Nullable;

/** Blood Magic's own altar state machine with a separate automation input and output. */
public class MechanicalAltarBlockEntity extends TileAltar {
    private boolean processingSingleItem;
    private boolean restartPending;
    private LazyOptional<IItemHandler> mechanicalItems = LazyOptional.of(() -> new MechanicalItemHandler(this));

    public MechanicalAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MECHANICAL_ALTAR.get(), pos, state);
        this.inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public void clearContent() {
        this.inventory = NonNullList.withSize(2, ItemStack.EMPTY);
        setChanged();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot == 0 && super.canPlaceItem(0, stack);
    }

    @Override
    public ItemStack getItem(int slot) {
        ItemStack stack = super.getItem(slot);
        if (slot == 0 && processingSingleItem && stack.getCount() > 1) {
            ItemStack single = stack.copy();
            single.setCount(1);
            return single;
        }
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot == 0 && processingSingleItem) {
            ItemStack input = super.getItem(0).copy();
            input.shrink(1);
            super.setItem(0, input);

            if (!stack.isEmpty()) {
                ItemStack output = super.getItem(1);
                if (output.isEmpty()) {
                    super.setItem(1, stack.copy());
                } else if (ItemHandlerHelper.canItemStacksStack(output, stack)
                        && output.getCount() + stack.getCount() <= Math.min(output.getMaxStackSize(), getMaxStackSize())) {
                    ItemStack merged = output.copy();
                    merged.grow(stack.getCount());
                    super.setItem(1, merged);
                } else if (level != null && !level.isClientSide) {
                    // A crafted-event listener may replace the expected result.
                    Containers.dropItemStack(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.0,
                            worldPosition.getZ() + 0.5, stack.copy());
                }
            }
            restartPending = true;
            return;
        }
        super.setItem(slot, stack);
    }

    private boolean canStoreNextResult(Level level) {
        ItemStack output = super.getItem(1);
        ItemStack input = super.getItem(0);
        if (output.isEmpty() || input.isEmpty()) return true;
        ItemStack singleInput = input.copy();
        singleInput.setCount(1);
        RecipeBloodAltar recipe = BloodMagicAPI.INSTANCE.getRecipeRegistrar().getBloodAltar(level, singleInput);
        if (recipe == null || recipe.getOutput().isEmpty()) return false;
        return ItemHandlerHelper.canItemStacksStack(output, recipe.getOutput())
                && output.getCount() < Math.min(output.getMaxStackSize(), getMaxStackSize());
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        MechanicalAltarBlockEntity altar = (MechanicalAltarBlockEntity) blockEntity;
        if (!altar.canStoreNextResult(level)) return;
        altar.processingSingleItem = true;
        try {
            altar.restartCycleIfPending();
            TileAltar.tick(level, pos, state, altar);
            altar.restartCycleIfPending();
        } finally {
            altar.processingSingleItem = false;
        }
    }

    private void restartCycleIfPending() {
        if (!restartPending || level == null || level.isClientSide || super.getItem(0).isEmpty()) return;
        // Blood Magic normally waits 30 ticks after crafting. A mechanical altar
        // starts the next item immediately, or retries when more LP arrives.
        if (!isActive()) startCycle();
        if (isActive()) restartPending = false;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return mechanicalItems.cast();
        }
        return super.getCapability(capability, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        mechanicalItems.invalidate();
    }

    private static final class MechanicalItemHandler implements IItemHandler {
        private final MechanicalAltarBlockEntity altar;

        private MechanicalItemHandler(MechanicalAltarBlockEntity altar) {
            this.altar = altar;
        }

        @Override
        public int getSlots() { return 2; }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return slot >= 0 && slot < 2 ? altar.getItem(slot) : ItemStack.EMPTY;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot != 0 || stack.isEmpty() || !altar.canPlaceItem(0, stack)) return stack;
            ItemStack existing = altar.getItem(0);
            if (!existing.isEmpty() && !ItemHandlerHelper.canItemStacksStack(existing, stack)) return stack;
            int limit = Math.min(stack.getMaxStackSize(), altar.getMaxStackSize());
            int room = limit - (existing.isEmpty() ? 0 : existing.getCount());
            if (room <= 0) return stack;
            int moved = Math.min(room, stack.getCount());
            if (!simulate) {
                ItemStack next = existing.isEmpty() ? stack.copy() : existing.copy();
                if (existing.isEmpty()) next.setCount(moved);
                if (!existing.isEmpty()) next.grow(moved);
                altar.setItem(0, next);
            }
            int remaining = stack.getCount() - moved;
            if (remaining <= 0) return ItemStack.EMPTY;
            ItemStack remainder = stack.copy();
            remainder.setCount(remaining);
            return remainder;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot != 1 || amount <= 0 || altar.getItem(1).isEmpty()) return ItemStack.EMPTY;
            ItemStack present = altar.getItem(1);
            int taken = Math.min(amount, present.getCount());
            ItemStack result = present.copy();
            result.setCount(taken);
            if (!simulate) altar.removeItem(1, taken);
            return result;
        }

        @Override
        public int getSlotLimit(int slot) { return slot >= 0 && slot < 2 ? altar.getMaxStackSize() : 0; }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return slot == 0 && altar.canPlaceItem(0, stack);
        }
    }
}
