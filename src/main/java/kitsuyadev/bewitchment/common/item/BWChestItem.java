/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.item;

import com.mojang.blaze3d.vertex.PoseStack;
import kitsuyadev.bewitchment.common.block.BWChestBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class BWChestItem extends BlockItem {
	public BWChestItem(BWChestBlock block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			private BlockEntityWithoutLevelRenderer renderer;

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				if (renderer == null) {
					Minecraft client = Minecraft.getInstance();
					renderer = new BlockEntityWithoutLevelRenderer(client.getBlockEntityRenderDispatcher(), client.getEntityModels()) {
						@Override
						public void renderByItem(ItemStack stack, ItemDisplayContext display,
												 PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
							BWChestBlock chest = (BWChestBlock) getBlock();
							client.getBlockEntityRenderDispatcher().renderItem(
									chest.newBlockEntity(BlockPos.ZERO, chest.defaultBlockState()), pose, buffers, light, overlay);
						}
					};
				}
				return renderer;
			}
		});
	}
}
