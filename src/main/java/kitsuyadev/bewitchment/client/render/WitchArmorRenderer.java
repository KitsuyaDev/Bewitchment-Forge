/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client.render;

import kitsuyadev.bewitchment.client.BewitchmentClient;
import kitsuyadev.bewitchment.client.model.equipment.armor.WitchArmorModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class WitchArmorRenderer implements IClientItemExtensions {
	private static WitchArmorModel<LivingEntity> armorModel;
	private static WitchArmorModel<LivingEntity> bootsModel;

	private final boolean hat;

	public WitchArmorRenderer(boolean hat) {
		this.hat = hat;
	}

	@Override
	public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> originalModel) {
		if (armorModel == null) {
			armorModel = new WitchArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BewitchmentClient.WITCH_ARMOR_MODEL_LAYER));
			bootsModel = new WitchArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BewitchmentClient.WITCH_ARMOR_MODEL_LAYER));
		}
		if (slot == EquipmentSlot.FEET) {
			bootsModel.setAllVisible(false);
			bootsModel.leftLeg.visible = true;
			bootsModel.rightLeg.visible = true;
			return bootsModel;
		} else {
			armorModel.setAllVisible(false);
			armorModel.hood01.visible = !hat;
			armorModel.hat1.visible = hat;
			armorModel.head.visible = slot == EquipmentSlot.HEAD;
			armorModel.body.visible = slot == EquipmentSlot.CHEST;
			armorModel.leftArm.visible = slot == EquipmentSlot.CHEST;
			armorModel.rightArm.visible = slot == EquipmentSlot.CHEST;
			armorModel.leftLeg.visible = slot == EquipmentSlot.LEGS;
			armorModel.rightLeg.visible = slot == EquipmentSlot.LEGS;
			armorModel.lowerLeftSkirt.visible = entity.getItemBySlot(EquipmentSlot.FEET).isEmpty();
			armorModel.lowerRightSkirt.visible = armorModel.lowerLeftSkirt.visible;
			return armorModel;
		}
	}
}
