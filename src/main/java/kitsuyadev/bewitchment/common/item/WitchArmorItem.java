/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.item;

import kitsuyadev.bewitchment.client.render.WitchArmorRenderer;
import kitsuyadev.bewitchment.common.Bewitchment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.Locale;
import java.util.function.Consumer;

public class WitchArmorItem extends ArmorItem {
	private final String texture;
	private final boolean hat;

	public WitchArmorItem(ArmorMaterial material, Type type, Properties properties, String set, boolean hat) {
		super(material, type, properties);
		this.texture = Bewitchment.id("textures/entity/armor/" + set + ".png").toString();
		this.hat = hat;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new WitchArmorRenderer(hat));
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return hat && stack.getHoverName().getString().toLowerCase(Locale.ROOT).contains("faith")
				? Bewitchment.id("textures/entity/armor/witch_hat_variant.png").toString() : texture;
	}
}
