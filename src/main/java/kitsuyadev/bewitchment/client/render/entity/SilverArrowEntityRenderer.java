/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client.render.entity;

import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.entity.projectile.SilverArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SilverArrowEntityRenderer extends ArrowRenderer<SilverArrowEntity> {
	private static final ResourceLocation TEXTURE = Bewitchment.id("textures/entity/projectiles/silver_arrow.png");

	public SilverArrowEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(SilverArrowEntity entity) {
		return TEXTURE;
	}
}
