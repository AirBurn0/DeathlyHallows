package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.item.ItemLeonardsUrn;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class CardsGUI extends GuiContainer {
	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("witchery", "textures/gui/urn.png");
	private final IInventory upperInventory;
	private final IInventory lowerInventory;


	public CardsGUI(IInventory inventoryPlayer, IInventory inventoryBag) {
		super(new ItemLeonardsUrn.ContainerLeonardsUrn(inventoryPlayer, inventoryBag, null));
		this.upperInventory = inventoryBag;
		this.lowerInventory = inventoryPlayer;
		super.ySize = 184;
	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.fontRendererObj.drawString(StatCollector.translateToLocal(this.upperInventory.getInventoryName()), 8, 6, 4210752);
		super.fontRendererObj.drawString(StatCollector.translateToLocal(this.lowerInventory.getInventoryName()), 8, super.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		super.mc.renderEngine.bindTexture(TEXTURE_LOCATION);
		int left = (super.width - super.xSize) / 2;
		int top = (super.height - super.ySize) / 2;
		this.drawTexturedModalRect(left, top, 0, 0, super.xSize, super.ySize);

		for(int i = 0; i < this.upperInventory.getSizeInventory(); ++i) {
			Slot slot = super.inventorySlots.getSlotFromInventory(this.upperInventory, i);
			this.drawTexturedModalRect(left + slot.xDisplayPosition - 1, top + slot.yDisplayPosition - 1, super.xSize, 0, 18, 18);
		}

	}
}
