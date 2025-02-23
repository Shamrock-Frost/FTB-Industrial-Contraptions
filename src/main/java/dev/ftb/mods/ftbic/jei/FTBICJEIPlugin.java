package dev.ftb.mods.ftbic.jei;

import dev.ftb.mods.ftbic.FTBIC;
import dev.ftb.mods.ftbic.block.FTBICElectricBlocks;
import dev.ftb.mods.ftbic.item.FTBICItems;
import dev.ftb.mods.ftbic.item.FluidCellItem;
import dev.ftb.mods.ftbic.recipe.FTBICRecipes;
import dev.ftb.mods.ftbic.recipe.RecipeCache;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * @author LatvianModder
 */
@JeiPlugin
public class FTBICJEIPlugin implements IModPlugin {
	public static final ResourceLocation ID = new ResourceLocation(FTBIC.MOD_ID, "jei");

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration r) {
		r.registerSubtypeInterpreter(FTBICItems.FLUID_CELL.get(), FluidCellItem::getSubtype);

		r.useNbtForSubtypes(
				FTBICItems.MECHANICAL_ELYTRA.get(),
				FTBICItems.CARBON_CHESTPLATE.get(),
				FTBICItems.QUANTUM_CHESTPLATE.get()
		);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration r) {
		r.addRecipeTransferHandler(new PoweredCraftingTableTransferHandler(), VanillaRecipeCategoryUid.CRAFTING);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration r) {
		r.addRecipeCatalyst(new ItemStack(FTBICItems.IRON_FURNACE.get()), VanillaRecipeCategoryUid.FURNACE);
		r.addRecipeCatalyst(new ItemStack(FTBICItems.IRON_FURNACE.get()), VanillaRecipeCategoryUid.FUEL);
		r.addRecipeCatalyst(new ItemStack(FTBICElectricBlocks.POWERED_FURNACE.item.get()), VanillaRecipeCategoryUid.FURNACE);
		r.addRecipeCatalyst(new ItemStack(FTBICElectricBlocks.POWERED_FURNACE.item.get()), VanillaRecipeCategoryUid.FUEL);
		r.addRecipeCatalyst(new ItemStack(FTBICElectricBlocks.POWERED_FURNACE.item.get()), FTBICRecipes.SMELTING.get().getRegistryName());
		r.addRecipeCatalyst(new ItemStack(FTBICElectricBlocks.MACERATOR.item.get()), FTBICRecipes.MACERATING.get().getRegistryName());
		r.addRecipeCatalyst(new ItemStack(FTBICElectricBlocks.CENTRIFUGE.item.get()), FTBICRecipes.SEPARATING.get().getRegistryName());
		r.addRecipeCatalyst(new ItemStack(FTBICElectricBlocks.COMPRESSOR.item.get()), FTBICRecipes.COMPRESSING.get().getRegistryName());
		r.addRecipeCatalyst(new ItemStack(FTBICElectricBlocks.REPROCESSOR.item.get()), FTBICRecipes.REPROCESSING.get().getRegistryName());
		r.addRecipeCatalyst(new ItemStack(FTBICElectricBlocks.CANNING_MACHINE.item.get()), FTBICRecipes.CANNING.get().getRegistryName());
		r.addRecipeCatalyst(new ItemStack(FTBICElectricBlocks.ROLLER.item.get()), FTBICRecipes.ROLLING.get().getRegistryName());
		r.addRecipeCatalyst(new ItemStack(FTBICElectricBlocks.EXTRUDER.item.get()), FTBICRecipes.EXTRUDING.get().getRegistryName());
		r.addRecipeCatalyst(new ItemStack(FTBICElectricBlocks.ADVANCED_POWERED_FURNACE.item.get()), FTBICRecipes.SMELTING.get().getRegistryName());
		r.addRecipeCatalyst(new ItemStack(FTBICElectricBlocks.ADVANCED_POWERED_FURNACE.item.get()), VanillaRecipeCategoryUid.FURNACE);
		r.addRecipeCatalyst(new ItemStack(FTBICElectricBlocks.ADVANCED_POWERED_FURNACE.item.get()), VanillaRecipeCategoryUid.FUEL);
		r.addRecipeCatalyst(new ItemStack(FTBICElectricBlocks.ADVANCED_MACERATOR.item.get()), FTBICRecipes.MACERATING.get().getRegistryName());
		r.addRecipeCatalyst(new ItemStack(FTBICElectricBlocks.ADVANCED_CENTRIFUGE.item.get()), FTBICRecipes.SEPARATING.get().getRegistryName());
		r.addRecipeCatalyst(new ItemStack(FTBICElectricBlocks.ADVANCED_COMPRESSOR.item.get()), FTBICRecipes.COMPRESSING.get().getRegistryName());
	}

	@Override
	public void registerRecipes(IRecipeRegistration r) {
		Level level = Minecraft.getInstance().level;
		RecipeCache cache = level == null ? null : RecipeCache.get(Minecraft.getInstance().level);

		if (cache == null) {
			FTBIC.LOGGER.error("Recipe cache not found for JEI plugin!");
			return;
		}

		r.addRecipes(cache.smelting.getAllRealAndVisibleRecipes(level), FTBICRecipes.SMELTING.get().getRegistryName());
		r.addRecipes(cache.macerating.getAllVisibleRecipes(level), FTBICRecipes.MACERATING.get().getRegistryName());
		r.addRecipes(cache.compressing.getAllVisibleRecipes(level), FTBICRecipes.COMPRESSING.get().getRegistryName());
		r.addRecipes(cache.separating.getAllVisibleRecipes(level), FTBICRecipes.SEPARATING.get().getRegistryName());
		r.addRecipes(cache.reprocessing.getAllVisibleRecipes(level), FTBICRecipes.REPROCESSING.get().getRegistryName());
		r.addRecipes(cache.canning.getAllVisibleRecipes(level), FTBICRecipes.CANNING.get().getRegistryName());
		r.addRecipes(cache.rolling.getAllVisibleRecipes(level), FTBICRecipes.ROLLING.get().getRegistryName());
		r.addRecipes(cache.extruding.getAllVisibleRecipes(level), FTBICRecipes.EXTRUDING.get().getRegistryName());
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration r) {
		r.addRecipeCategories(new MachineRecipeCategory(FTBICRecipes.SMELTING, r.getJeiHelpers().getGuiHelper(), FTBICElectricBlocks.POWERED_FURNACE));
		r.addRecipeCategories(new MachineRecipeCategory(FTBICRecipes.MACERATING, r.getJeiHelpers().getGuiHelper(), FTBICElectricBlocks.MACERATOR));
		r.addRecipeCategories(new MachineRecipeCategory(FTBICRecipes.SEPARATING, r.getJeiHelpers().getGuiHelper(), FTBICElectricBlocks.CENTRIFUGE));
		r.addRecipeCategories(new MachineRecipeCategory(FTBICRecipes.COMPRESSING, r.getJeiHelpers().getGuiHelper(), FTBICElectricBlocks.COMPRESSOR));
		r.addRecipeCategories(new MachineRecipeCategory(FTBICRecipes.REPROCESSING, r.getJeiHelpers().getGuiHelper(), FTBICElectricBlocks.REPROCESSOR));
		r.addRecipeCategories(new MachineRecipeCategory(FTBICRecipes.CANNING, r.getJeiHelpers().getGuiHelper(), FTBICElectricBlocks.CANNING_MACHINE));
		r.addRecipeCategories(new MachineRecipeCategory(FTBICRecipes.ROLLING, r.getJeiHelpers().getGuiHelper(), FTBICElectricBlocks.ROLLER));
		r.addRecipeCategories(new MachineRecipeCategory(FTBICRecipes.EXTRUDING, r.getJeiHelpers().getGuiHelper(), FTBICElectricBlocks.EXTRUDER));
	}
}