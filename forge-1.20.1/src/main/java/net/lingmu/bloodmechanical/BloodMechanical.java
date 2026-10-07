package net.lingmu.bloodmechanical;

import net.lingmu.bloodmechanical.registry.ModBlockEntities;
import net.lingmu.bloodmechanical.registry.ModBlocks;
import net.lingmu.bloodmechanical.registry.ModCreativeTabs;
import net.lingmu.bloodmechanical.registry.ModItems;
import net.lingmu.bloodmechanical.registry.ModMenus;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BloodMechanical.MOD_ID)
public final class BloodMechanical {
    public static final String MOD_ID = "bloodmechanical";

    public BloodMechanical() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModCreativeTabs.TABS.register(modBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modBus);
        ModMenus.MENUS.register(modBus);
    }
}
