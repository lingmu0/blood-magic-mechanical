package net.lingmu.bloodmechanical.client;

import net.lingmu.bloodmechanical.BloodMechanical;
import net.lingmu.bloodmechanical.registry.ModMenus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.gui.screens.MenuScreens;

@Mod.EventBusSubscriber(modid = BloodMechanical.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    private ClientModEvents() { }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(ModMenus.MECHANICAL_ALTAR.get(), MechanicalAltarScreen::new));
    }
}
