package net.lingmu.bloodmechanical.registry;

import net.lingmu.bloodmechanical.BloodMechanical;
import net.lingmu.bloodmechanical.block.MechanicalAltarBlock;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.block.Block;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BloodMechanical.MOD_ID);
    public static final RegistryObject<Block> MECHANICAL_ALTAR = BLOCKS.register("mechanical_altar", MechanicalAltarBlock::new);

    private ModBlocks() { }
}
