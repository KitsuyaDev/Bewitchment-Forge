/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client.render.entity;

import com.mojang.datafixers.util.Pair;
import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.registry.BWBoatTypes;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

public class BWBoatRenderer extends BoatRenderer {
	private final Pair<ResourceLocation, ListModel<Boat>> model;

	public BWBoatRenderer(EntityRendererProvider.Context context, BWBoatTypes.Wood wood, boolean chest) {
		super(context, chest);
		ListModel<Boat> boatModel = chest
				? new ChestBoatModel(context.bakeLayer(ModelLayers.createChestBoatModelName(Boat.Type.OAK)))
				: new BoatModel(context.bakeLayer(ModelLayers.createBoatModelName(Boat.Type.OAK)));
		model = Pair.of(Bewitchment.id("textures/entity/" + (chest ? "chest_boat/" : "boat/") + wood.woodName() + "_boat.png"), boatModel);
	}

	@Override
	public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
		return model;
	}
}
