package net.lingmu.bloodmechanical.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.lingmu.bloodmechanical.menu.MechanicalAltarMenu;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MechanicalAltarScreen extends AbstractContainerScreen<MechanicalAltarMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/furnace.png");

    public MechanicalAltarScreen(MechanicalAltarMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = 72;
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float partialTick) {
        super.render(pose, mouseX, mouseY, partialTick);
        renderTooltip(pose, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack pose, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos;
        RenderSystem.setShaderTexture(0, TEXTURE);
        GuiComponent.blit(pose, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        // The altar has no furnace input/fuel area; move the visible input slot
        // down beside the output slot and cover the old slot and flame graphic.
        GuiComponent.fill(pose, x + 55, y + 16, x + 73, y + 34, 0xFFC6C6C6);
        GuiComponent.fill(pose, x + 55, y + 52, x + 73, y + 70, 0xFFC6C6C6);
        GuiComponent.blit(pose, x + 55, y + 34, 55, 16, 18, 18, 256, 256);

        int tankX = x + 18;
        int tankY = y + 19;
        int tankHeight = 40;
        GuiComponent.fill(pose, tankX, tankY, tankX + 16, tankY + tankHeight, 0xFF373737);
        GuiComponent.fill(pose, tankX + 1, tankY + 1, tankX + 15, tankY + tankHeight - 1, 0xFF8B8B8B);
        GuiComponent.fill(pose, tankX + 2, tankY + 2, tankX + 14, tankY + tankHeight - 2, 0xFF1D1D1D);
        int capacity = Math.max(1, menu.capacity());
        int fillHeight = (int) Math.min(tankHeight - 4, (long) Math.max(0, menu.blood()) * (tankHeight - 4) / capacity);
        if (fillHeight > 0) {
            GuiComponent.fill(pose, tankX + 3, tankY + tankHeight - 2 - fillHeight, tankX + 13, tankY + tankHeight - 2, 0xFF9F1427);
            GuiComponent.fill(pose, tankX + 4, tankY + tankHeight - 2 - fillHeight, tankX + 6, tankY + tankHeight - 2, 0xFFC63748);
        }

        int required = Math.max(1, menu.required());
        int progressWidth = (int) Math.min(24, (long) Math.max(0, menu.progress()) * 24 / required);
        if (progressWidth > 0) {
            GuiComponent.blit(pose, x + 79, y + 34, 176, 14, progressWidth, 16, 256, 256);
        }
    }

    @Override
    protected void renderLabels(PoseStack pose, int mouseX, int mouseY) {
        font.draw(pose, title, (imageWidth - font.width(title)) / 2.0F, 6, 0x404040);
        font.draw(pose, menu.blood() + " / " + menu.capacity() + " LP", 8, 62, 0x404040);
        font.draw(pose, new TranslatableComponent("container.inventory"), 8, 72, 0x404040);
    }
}
