package net.lingmu.bloodmechanical.registry;

import net.lingmu.bloodmechanical.BloodMechanical;
import net.lingmu.bloodmechanical.menu.MechanicalAltarMenu;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.inventory.MenuType;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, BloodMechanical.MOD_ID);
    public static final RegistryObject<MenuType<MechanicalAltarMenu>> MECHANICAL_ALTAR = MENUS.register(
            "mechanical_altar", () -> IForgeMenuType.create(MechanicalAltarMenu::new));

    private ModMenus() { }
}
