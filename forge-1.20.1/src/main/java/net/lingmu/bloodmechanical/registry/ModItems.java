package net.lingmu.bloodmechanical.registry;

import net.lingmu.bloodmechanical.BloodMechanical;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BloodMechanical.MOD_ID);
    public static final RegistryObject<Item> MECHANICAL_ALTAR = ITEMS.register("mechanical_altar",
            () -> new BlockItem(ModBlocks.MECHANICAL_ALTAR.get(), new Item.Properties()));

    private ModItems() { }
}
