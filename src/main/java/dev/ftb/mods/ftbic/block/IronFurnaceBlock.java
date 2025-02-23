package dev.ftb.mods.ftbic.block;

import dev.ftb.mods.ftbic.block.entity.IronFurnaceBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Material;

public class IronFurnaceBlock extends FurnaceBlock {
	public IronFurnaceBlock() {
		super(Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).lightLevel(value -> value.getValue(LIT) ? 13 : 0));
	}

	@Override
	public BlockEntity newBlockEntity(BlockGetter level) {
		return new IronFurnaceBlockEntity();
	}
}
