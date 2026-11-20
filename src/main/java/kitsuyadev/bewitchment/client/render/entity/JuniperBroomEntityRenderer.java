/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client.render.entity;

import kitsuyadev.bewitchment.api.client.renderer.BroomEntityRenderer;
import kitsuyadev.bewitchment.api.entity.BroomEntity;
import kitsuyadev.bewitchment.common.Bewitchment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class JuniperBroomEntityRenderer extends BroomEntityRenderer<BroomEntity> {
	private static final ResourceLocation TEXTURE = Bewitchment.id("textures/entity/broom/juniper.png");

	public JuniperBroomEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(BroomEntity entity) {
		return TEXTURE;
	}
}
