package com.pyding.deathlyhallows.items;

import com.pyding.deathlyhallows.utils.properties.ExtendedPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class MonsterBook extends Item {

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		ExtendedPlayer props = ExtendedPlayer.get(player);
		float damage = 2 + props.getMonstersCount();
		if(props.getElfLvl() > 0) {
			damage = 2 + props.getMonstersCount() * (props.getElfLvl() * 10);
		}
		entity.attackEntityFrom(DamageSource.causePlayerDamage(player).setDamageIsAbsolute(), damage);
		return false;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
		ExtendedPlayer props = ExtendedPlayer.get(player);
		float damage = props.getMonstersCount();
		list.add(StatCollector.translateToLocal("dh.desc.book1"));
		list.add(StatCollector.translateToLocalFormatted("dh.desc.book2", damage));
		if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			return;
		}
		list.add(StatCollector.translateToLocal("dh.desc.book3"));
		if(props.getElfLvl() > 0) {
			list.add(StatCollector.translateToLocal("dh.desc.book4"));
		}
	}
}
