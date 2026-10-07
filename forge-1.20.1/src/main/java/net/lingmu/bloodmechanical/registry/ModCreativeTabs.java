package net.lingmu.bloodmechanical.registry;

import net.lingmu.bloodmechanical.BloodMechanical;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BloodMechanical.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BLOOD_MECHANICAL = TABS.register("blood_mechanical",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.bloodmechanical"))
                    .icon(() -> new ItemStack(ModItems.MECHANICAL_ALTAR.get()))
                    .displayItems((parameters, output) -> output.accept(ModItems.MECHANICAL_ALTAR.get()))
                    .build());

    private ModCreativeTabs() { }
}
