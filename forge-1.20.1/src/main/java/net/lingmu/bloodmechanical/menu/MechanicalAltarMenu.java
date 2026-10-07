package net.lingmu.bloodmechanical.menu;

import net.lingmu.bloodmechanical.blockentity.MechanicalAltarBlockEntity;
import net.lingmu.bloodmechanical.registry.ModBlocks;
import net.lingmu.bloodmechanical.registry.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerLevelAccess;

public class MechanicalAltarMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final ContainerData data;

    public MechanicalAltarMenu(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(id, playerInventory, buffer.readBlockPos());
    }

    public MechanicalAltarMenu(int id, Inventory playerInventory, BlockPos pos) {
        this(id, playerInventory, pos, findAltar(playerInventory, pos));
    }

    private MechanicalAltarMenu(int id, Inventory playerInventory, BlockPos pos, MechanicalAltarBlockEntity altar) {
        super(ModMenus.MECHANICAL_ALTAR.get(), id);
        this.access = ContainerLevelAccess.create(playerInventory.player.level(), pos);
        Container machine = altar != null ? altar : new SimpleContainer(2);
        this.data = createData(altar, playerInventory.player.level().isClientSide);

        addSlot(new Slot(machine, 0, 56, 35));
        addSlot(new Slot(machine, 1, 116, 35) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }
        });

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, 8 + column * 18, 142));
        }
        addDataSlots(data);
    }

    private static MechanicalAltarBlockEntity findAltar(Inventory inventory, BlockPos pos) {
        BlockEntity blockEntity = inventory.player.level().getBlockEntity(pos);
        return blockEntity instanceof MechanicalAltarBlockEntity altar ? altar : null;
    }

    private static ContainerData createData(MechanicalAltarBlockEntity altar, boolean clientSide) {
        int[] clientValues = new int[8];
        return new ContainerData() {
            @Override public int get(int index) {
                if (clientSide || altar == null) return clientValues[index];
                int value = switch (index / 2) {
                    case 0 -> altar.getCurrentBlood();
                    case 1 -> altar.getCapacity();
                    case 2 -> altar.getProgress();
                    case 3 -> altar.getLiquidRequired();
                    default -> 0;
                };
                return (index & 1) == 0 ? value & 0xFFFF : (value >>> 16) & 0xFFFF;
            }
            @Override public void set(int index, int value) { clientValues[index] = value; }
            @Override public int getCount() { return 8; }
        };
    }

    private int value(int lowWordIndex) {
        return (data.get(lowWordIndex) & 0xFFFF) | ((data.get(lowWordIndex + 1) & 0xFFFF) << 16);
    }
    public int blood() { return value(0); }
    public int capacity() { return value(2); }
    public int progress() { return value(4); }
    public int required() { return value(6); }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot == null || !slot.hasItem()) return result;
        ItemStack original = slot.getItem();
        result = original.copy();
        if (index < 2) {
            if (!moveItemStackTo(original, 2, slots.size(), true)) return ItemStack.EMPTY;
        } else if (!moveItemStackTo(original, 0, 1, false)) {
            return ItemStack.EMPTY;
        }
        if (original.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, ModBlocks.MECHANICAL_ALTAR.get());
    }
}
