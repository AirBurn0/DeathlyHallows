package com.pyding.deathlyhallows.events;

import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.util.Config;
import com.pyding.deathlyhallows.items.DHItems;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static codechicken.lib.gui.GuiDraw.drawTexturedModalRect;
import static codechicken.lib.gui.GuiDraw.getStringWidth;
import static com.emoniph.witchery.client.PlayerRender.drawString;

public final class DHPlayerRenderEvents {
	private static final Minecraft mc = Minecraft.getMinecraft();
	public static long ticksSinceActive = 0L;
	public static boolean moveCameraActive = false;
	private int lastY = 0;
	private static final int[] glyphOffsetX = new int[]{0, 0, 1, -1, 1, -1, -1, 1};
	private static final int[] glyphOffsetY = new int[]{-1, 1, 0, 0, -1, 1, -1, 1};
	private static final ResourceLocation TEXTURE_GRID = new ResourceLocation("witchery", "textures/gui/grid.png");

	private static final DHPlayerRenderEvents INSTANCE = new DHPlayerRenderEvents();

	private DHPlayerRenderEvents() {

	}

	public static void init() {
		FMLCommonHandler.instance().bus().register(INSTANCE);
	}

	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event) {
		EntityClientPlayerMP player;
		if(event.phase == TickEvent.Phase.START) {
			player = Minecraft.getMinecraft().thePlayer;
			if(player != null && mc.currentScreen == null) {
				if(Minecraft.getSystemTime() - ticksSinceActive > 3000L) {
					moveCameraActive = false;
				}
			}
		}
		else if(event.phase == TickEvent.Phase.END) {
			player = Minecraft.getMinecraft().thePlayer;
			if(player != null && Minecraft.getMinecraft().currentScreen == null) {
				Minecraft.getMinecraft().renderViewEntity = player;
				ItemStack var44 = player.getItemInUse();
				int x;
				int y;
				int color;
				ScaledResolution var30 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
				if(var44 != null && var44.getItem() == DHItems.elderWand) {
					byte[] var39 = player.getEntityData().getByteArray("Strokes");
					mc.getTextureManager().bindTexture(TEXTURE_GRID);
					GL11.glPushMatrix();
					byte var41 = 0;
					if(Config.instance().branchIconSet == 1) {
						var41 = 64;
					}

					try {
						x = var30.getScaledWidth() / 2 - 8;
						y = var30.getScaledHeight() / 2 - 8;
						boolean var45 = true;
						this.lastY = this.lastY == 120 ? 0 : this.lastY + 1;
						color = this.lastY / 8;
						int var46 = color > 7 ? 15 - color : color;

						for(byte b: var39) {
							x += glyphOffsetX[b] * 16;
							y += glyphOffsetY[b] * 16;
							drawTexturedModalRect(x, y, b * 16 + var41, var46 * 16, 16, 16);
						}

						SymbolEffect var47 = EffectRegistry.instance().getEffect(var39);
						if(var47 != null) {
							String var49 = var47.getLocalizedName();
							int tx = var30.getScaledWidth() / 2 - (int)(getStringWidth(var49) / 2.0D);
							int ty = var30.getScaledHeight() / 2 + 20;
							drawString(var49, tx, ty, 16777215);
						}
					}
					finally {
						GL11.glPopMatrix();
					}
				}
			}
		}
	}
}