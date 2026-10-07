package net.lingmu.bloodmechanical.registry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class ModCreativeTabs {
    public static final CreativeModeTab BLOOD_MECHANICAL = new CreativeModeTab("bloodmechanical") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.MECHANICAL_ALTAR.get());
        }
    };

    private ModCreativeTabs() { }
}
