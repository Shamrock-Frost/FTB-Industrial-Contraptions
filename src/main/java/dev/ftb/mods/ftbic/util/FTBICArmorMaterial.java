package dev.ftb.mods.ftbic.util;

import dev.ftb.mods.ftbic.FTBIC;
import dev.ftb.mods.ftbic.FTBICConfig;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class FTBICArmorMaterial implements ArmorMaterial {
	public static final FTBICArmorMaterial CARBON = new FTBICArmorMaterial(FTBIC.MOD_ID + ":carbon", FTBICConfig.CARBON_ARMOR_CAPACITY);
	public static final FTBICArmorMaterial QUANTUM = new FTBICArmorMaterial(FTBIC.MOD_ID + ":quantum", FTBICConfig.QUANTUM_ARMOR_CAPACITY);

	private final String name;
	public final double capacity;

	public FTBICArmorMaterial(String n, double c) {
		name = n;
		capacity = c;
	}

	@Override
	public int getDurabilityForSlot(EquipmentSlot arg) {
		return 1;
	}

	@Override
	public int getDefenseForSlot(EquipmentSlot arg) {
		return 0;
	}

	@Override
	public int getEnchantmentValue() {
		return 0;
	}

	@Override
	public SoundEvent getEquipSound() {
		return SoundEvents.ARMOR_EQUIP_NETHERITE;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.EMPTY;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public float getToughness() {
		return 0F;
	}

	@Override
	public float getKnockbackResistance() {
		return 0F;
	}
}
