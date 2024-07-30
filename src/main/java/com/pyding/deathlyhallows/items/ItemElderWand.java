package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockPlacedItem;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.network.PacketSpellPrepared;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import com.pyding.deathlyhallows.spells.SpellRegistry;
import com.pyding.deathlyhallows.utils.DHKeys;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemElderWand extends ItemBase {
	private static final float THRESHOLD_ORTHOGONAL = 7.0F;
	private static final int MAX_STROKES = 15;

	public ItemElderWand() {
		super("elderWand", 1);
		setFull3D();
	}

	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.rare;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		l.add(StatCollector.translateToLocalFormatted("dh.desc.wand1", Keyboard.getKeyName(DHKeys.WAND.getKeyCode())));
		l.add(StatCollector.translateToLocal("dh.desc.wand2"));
		l.add(StatCollector.translateToLocal("dh.desc.wand3"));
		String owner = stack.hasTagCompound() ? stack.getTagCompound().getString("dhowner") : "";
		if(owner.equals("")) {
			owner = StatCollector.translateToLocal("dh.desc.defaultOwner");
		}
		l.add(StatCollector.translateToLocalFormatted("dh.desc.stone4", owner));
		l.add(StatCollector.translateToLocal("dh.desc.wand5"));
	}
	
	// TODO Oh shit...
	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		World world = entityLiving.worldObj;
		if(!(entityLiving instanceof EntityPlayer)) {
			return super.onEntitySwing(entityLiving, stack);
		}
		if(world.isRemote) {
			return super.onEntitySwing(entityLiving, stack);
		}
		NBTTagCompound nbt = new NBTTagCompound();
		SpellRegistry spellRegistry = new SpellRegistry();
		EntityPlayer player = (EntityPlayer)entityLiving;
		if(stack.getTagCompound() != null) {
			nbt = stack.getTagCompound();
		}
		if(!nbt.hasKey("spell1")) {
			nbt.setInteger("spell1", 1);
			stack.setTagCompound(nbt);
			player.addChatComponentMessage(new ChatComponentText(spellRegistry.getName(1)));
			return super.onEntitySwing(entityLiving, stack);
		}

		int spell1 = nbt.getInteger("spell1");
		if(spell1 != 0) {
			if(!entityLiving.isSneaking()) {
				if(spell1 < SpellRegistry.spellCount) {
					spell1++;
				}
				else {
					spell1 = 1;
				}
			}
			else {
				if(spell1 > 1) {
					spell1--;
				}
				else {
					spell1 = SpellRegistry.spellCount;
				}
			}
		}
		else {
			spell1 = 1;
		}
		while(spellRegistry.getName(spell1).equals("IdError")) {
			if(!entityLiving.isSneaking()) {
				if(spell1 < SpellRegistry.spellCount) {
					spell1++;
				}
				else {
					spell1 = 1;
				}
			}
			else {
				if(spell1 > 1) {
					spell1--;
				}
				else {
					spell1 = SpellRegistry.spellCount;
				}
			}
		}
		nbt.setInteger("spell1", spell1);
		stack.setTagCompound(nbt);

		stack.setTagCompound(nbt);
		player.addChatComponentMessage(new ChatComponentText(spellRegistry.getName(nbt.getInteger("spell1"))));

		return super.onEntitySwing(entityLiving, stack);
	}

	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.block;
	}

	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		return super.onDroppedByPlayer(item, player);
	}

	public int getMaxItemUseDuration(ItemStack stack) {
		return '負';
	}

	public boolean hasEffect(ItemStack par1ItemStack, int pass) {
		return true;
	}

	public void onUpdate(ItemStack stack, World world, Entity entity, int invSlot, boolean isHeld) {
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		NBTTagCompound nbtTag = player.getEntityData();
		if(!player.worldObj.isRemote) {
			nbtTag.removeTag("WITCSpellEffectID");
			nbtTag.removeTag("WITCSpellEffectEnhanced");
		}

		nbtTag.setByteArray("Strokes", new byte[0]);
		nbtTag.setFloat("startPitch", player.rotationPitch);
		nbtTag.setFloat("startYaw", player.rotationYawHead);
		player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
		return stack;
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(world.getBlock(x, y, z) == Witchery.Blocks.ALTAR && side == 1 && world.getBlock(x, y + 1, z) == Blocks.air) {
			BlockPlacedItem.placeItemInWorld(stack, player, world, x, y + 1, z);
			player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
			return !world.isRemote;
		}
		else {
			return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
		}
	}

	public void onUsingTick(ItemStack stack, EntityPlayer player, int countdown) {
		if(!player.worldObj.isRemote) {
			return;
		}
		NBTTagCompound tag = player.getEntityData();
		if(tag == null) {
			return;
		}

		float yawDiff = tag.getFloat("startYaw") - player.rotationYawHead;
		float pitchDiff = tag.getFloat("startPitch") - player.rotationPitch;
		byte[] strokes = tag.getByteArray("Strokes");
		int strokesStart = strokes.length;
		if(EffectRegistry.instance().contains(strokes) || strokesStart > MAX_STROKES) {
			return;
		}
		if(pitchDiff >= THRESHOLD_ORTHOGONAL) {
			strokes = this.addNewStroke(tag, strokes, (byte)0);
		}
		else if(pitchDiff <= -THRESHOLD_ORTHOGONAL) {
			strokes = this.addNewStroke(tag, strokes, (byte)1);
		}
		else if(yawDiff <= -THRESHOLD_ORTHOGONAL) {
			strokes = this.addNewStroke(tag, strokes, (byte)2);
		}
		else if(yawDiff >= THRESHOLD_ORTHOGONAL) {
			strokes = this.addNewStroke(tag, strokes, (byte)3);
		}

		if(strokes.length > strokesStart) {
			tag.setFloat("startPitch", player.rotationPitch);
			tag.setFloat("startYaw", player.rotationYawHead);
		}

		SymbolEffect effect = EffectRegistry.instance().getEffect(strokes);
		if(effect != null) {
			int level = EffectRegistry.instance().getLevel(strokes);
			Witchery.packetPipeline.sendToServer(new PacketSpellPrepared(effect, level));
		}

	}

	public byte[] addNewStroke(NBTTagCompound nbtTag, byte[] strokes, byte stroke) {
		byte[] newStrokes = new byte[strokes.length + 1];
		System.arraycopy(strokes, 0, newStrokes, 0, strokes.length);
		newStrokes[newStrokes.length - 1] = stroke;
		nbtTag.setByteArray("Strokes", newStrokes);
		return newStrokes;
	}

	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int countdown) {
		NBTTagCompound tag = player.getEntityData();
		if(tag == null) {
			return;
		}
		if(world.isRemote) {
			tag.removeTag("Strokes");
			tag.removeTag("startYaw");
			tag.removeTag("startPitch");
			return;
		}

		int effectID = tag.getInteger("WITCSpellEffectID");
		int level = 1;
		if(tag.hasKey("WITCSpellEffectEnhanced")) {
			level = tag.getInteger("WITCSpellEffectEnhanced");
			tag.removeTag("WITCSpellEffectEnhanced");
		}

		tag.removeTag("WITCSpellEffectID");
		SymbolEffect effect = EffectRegistry.instance().getEffect(effectID);
		NBTTagCompound nbtPerm = Infusion.getNBT(player);
		if(effect == null) {
			ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.unknownsymbol");
			SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
			return;
		}

		if(!player.capabilities.isCreativeMode && (nbtPerm == null || !nbtPerm.hasKey("witcheryInfusionID") || !nbtPerm.hasKey("witcheryInfusionCharges"))) {
			ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.infusionrequired");
			SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
		}
		else if(!effect.hasValidInfusion(player, nbtPerm.getInteger("witcheryInfusionID"))) {
			ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.infernalrequired");
			SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
		}
		else {
			if(!effect.hasValidKnowledge(player, nbtPerm)) {
				ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.unknowneffect");
				SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
				return;
			}
			long ticksRemaining = effect.cooldownRemaining(player, nbtPerm);
			if(ticksRemaining > 0L && !player.capabilities.isCreativeMode) {
				ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.effectoncooldown", Long.valueOf(TimeUtil.ticksToSecs(ticksRemaining))
																													   .toString());
				SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
				return;
			}
			if(level > 1) {
				int newLevel = 1;
				if(player.isPotionActive(Witchery.Potions.WORSHIP)
						&& level <= player.getActivePotionEffect(Witchery.Potions.WORSHIP).getAmplifier() + 2
				) {
					newLevel = level;
				}

				level = newLevel;
			}

			if(!player.capabilities.isCreativeMode && nbtPerm.getInteger("witcheryInfusionCharges") < effect.getChargeCost(world, player, level)) {
				ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.nocharges");
				SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
			}
			else {
				effect.perform(world, player, level);
				if(!player.capabilities.isCreativeMode) {
					Infusion.setCurrentEnergy(player, nbtPerm.getInteger("witcheryInfusionCharges") - effect.getChargeCost(world, player, level));
				}
			}
		}
	}
	
}
