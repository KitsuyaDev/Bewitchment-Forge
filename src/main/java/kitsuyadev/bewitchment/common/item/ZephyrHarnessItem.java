/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.item;

import com.google.common.collect.Multimap;
import top.theillusivec4.curios.api.SlotContext;
import java.util.UUID;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class ZephyrHarnessItem extends BWAccessoryItem {
	public ZephyrHarnessItem(Properties settings) {
		super(settings);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slot, UUID uuid, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, uuid, stack);
		modifiers.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Trinket modifier", 0.1, AttributeModifier.Operation.ADDITION));
		return modifiers;
	}
}
