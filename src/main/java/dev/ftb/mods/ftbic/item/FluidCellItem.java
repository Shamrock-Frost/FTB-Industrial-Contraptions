package dev.ftb.mods.ftbic.item;

import dev.ftb.mods.ftbic.FTBIC;
import dev.ftb.mods.ftbic.FTBICConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class FluidCellItem extends Item {
	private static final String TAG_FLUID = "Fluid";

	public FluidCellItem() {
		super(new Properties().stacksTo(16).tab(FTBIC.TAB));
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return getFluid(stack) != Fluids.EMPTY;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		Fluid fluid = getFluid(stack);
		if (fluid != Fluids.EMPTY) {
			return new ItemStack(this);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
		InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onBucketUse(player, level, stack, hit);

		if (ret != null) {
			return ret;
		} else if (hit.getType() == HitResult.Type.MISS) {
			return InteractionResultHolder.pass(stack);
		} else if (hit.getType() != HitResult.Type.BLOCK) {
			return InteractionResultHolder.pass(stack);
		} else if (getFluid(stack) != Fluids.EMPTY) {
			return InteractionResultHolder.fail(stack);
		}

		BlockPos pos = hit.getBlockPos();
		Direction direction = hit.getDirection();
		BlockPos pos1 = pos.relative(direction);

		if (level.mayInteract(player, pos) && player.mayUseItemAt(pos1, direction, stack)) {
			BlockState state = level.getBlockState(pos);

			if (state.getBlock() instanceof BucketPickup) {
				Fluid fluid = ((BucketPickup) state.getBlock()).takeLiquid(level, pos, state);

				if (fluid != Fluids.EMPTY) {
					player.awardStat(Stats.ITEM_USED.get(this));
					SoundEvent soundevent = fluid.getAttributes().getFillSound();

					if (soundevent == null) {
						soundevent = fluid.is(FluidTags.LAVA) ? SoundEvents.BUCKET_FILL_LAVA : SoundEvents.BUCKET_FILL;
					}

					player.playSound(soundevent, 1F, 1F);
					ItemStack itemstack1 = ItemUtils.createFilledResult(stack, player, setFluid(new ItemStack(this), fluid));

					if (!level.isClientSide()) {
						CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, new ItemStack(fluid.getBucket()));
					}

					return InteractionResultHolder.sidedSuccess(itemstack1, level.isClientSide());
				}
			}
		}

		return InteractionResultHolder.fail(stack);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new FluidCellHandler(stack);
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) {
		if (allowdedIn(tab)) {
			list.add(new ItemStack(this));

			for (Fluid fluid : ForgeRegistries.FLUIDS) {
				if (fluid != Fluids.EMPTY && fluid.isSource(fluid.defaultFluidState()) && (FTBICConfig.ADD_ALL_FLUID_CELLS || !fluid.getRegistryName().getPath().contains("molten"))) {
					list.add(setFluid(new ItemStack(this), fluid));
				}
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		Fluid fluid = getFluid(stack);

		if (fluid != Fluids.EMPTY) {
			list.add(new TextComponent("< " + FTBICConfig.FLUID_CELL_CAPACITY + " mB of ").append(new TranslatableComponent(fluid.getAttributes().getTranslationKey())).append(" >").withStyle(ChatFormatting.GRAY));
		}
	}

	public static ItemStack setFluid(ItemStack stack, Fluid fluid) {
		if (fluid == Fluids.EMPTY) {
			CompoundTag nbt = stack.getTag();

			if (nbt != null) {
				nbt.remove(TAG_FLUID);

				if (nbt.isEmpty()) {
					stack.setTag(null);
				}
			}
		} else {
			CompoundTag nbt = stack.getOrCreateTag();
			nbt.putString(TAG_FLUID, Objects.requireNonNull(fluid.getRegistryName()).toString());
		}

		return stack;
	}

	public static Fluid getFluid(ItemStack stack) {
		CompoundTag nbt = stack.getTag();

		if (nbt != null) {
			ResourceLocation location = ResourceLocation.tryParse(nbt.getString(TAG_FLUID));

			if (location != null && ForgeRegistries.FLUIDS.containsKey(location)) {
				Fluid fluid = ForgeRegistries.FLUIDS.getValue(location);

				if (fluid != null) {
					return fluid;
				}
			}
		}

		return Fluids.EMPTY;
	}

	public static String getSubtype(ItemStack stack) {
		CompoundTag nbt = stack.getTag();

		if (nbt != null) {
			return nbt.getString(TAG_FLUID);
		}

		return "";
	}
}
