package com.pyding.deathlyhallows.client.render.entity;

import com.emoniph.witchery.client.model.ModelBroom;
import com.emoniph.witchery.entity.EntityBroom;
import com.pyding.deathlyhallows.entity.Nimbus;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderNimbus extends Render {

	private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/entities/Broom.png");
	protected ModelBase modelBroom;


	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return this.func_110832_a((EntityBroom)par1Entity);
	}

	public RenderNimbus() {
		super.shadowSize = 0.5F;
		this.modelBroom = new ModelBroom();
	}

	protected ResourceLocation func_110832_a(EntityBroom par1Entity) {
		return TEXTURE_URL;
	}

	public void renderBroom(Nimbus par1EntityBoat, double par2, double par4, double par6, float par8, float par9) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)par2, (float)par4 + 1.0F, (float)par6);
		GL11.glRotatef(90.0F - par8, 0.0F, 1.0F, 0.0F);
		float f4 = 0.75F;
		GL11.glScalef(f4, f4, f4);
		GL11.glScalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
		this.bindEntityTexture(par1EntityBoat);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		this.modelBroom.render(par1EntityBoat, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}

	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.renderBroom((Nimbus)par1Entity, par2, par4, par6, par8, par9);
	}

}
