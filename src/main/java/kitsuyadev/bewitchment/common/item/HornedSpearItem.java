/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.item;

import net.minecraftforge.common.ForgeMod;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import kitsuyadev.bewitchment.client.packet.SyncHornedSpearPacket;
import kitsuyadev.bewitchment.common.entity.projectile.HornedSpearEntity;
import kitsuyadev.bewitchment.common.registry.BWEntityTypes;
import kitsuyadev.bewitchment.common.registry.BWSoundEvents;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import java.util.UUID;

public class HornedSpearItem extends SwordItem {
	private static final AttributeModifier REACH_MODIFIER = new AttributeModifier(UUID.fromString("1d25c6a0-3f2c-4d4e-8f5f-941f51aab1b7"), "Weapon modifier", 2, AttributeModifier.Operation.ADDITION);

	public HornedSpearItem(Tier toolMaterial, int attackDamage, float attackSpeed, Properties settings) {
		super(toolMaterial, attackDamage, attackSpeed, settings);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create(super.getDefaultAttributeModifiers(slot));
		if (slot == EquipmentSlot.MAINHAND) {
			map.put(ForgeMod.ENTITY_REACH.get(), REACH_MODIFIER);
		}
		return map;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack stack = user.getItemInHand(hand);
		if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
			return InteractionResultHolder.fail(stack);
		}
		return ItemUtils.startUsingInstantly(world, user, hand);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
		int timer = getUseDuration(stack) - remainingUseTicks;
		if (timer >= 10) {
			if (!world.isClientSide) {
				spawnEntity(world, user, stack);
			}
			if (user instanceof Player player) {
				player.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.SPEAR;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	public static void spawnEntity(Level world, LivingEntity owner, ItemStack stack) {
		stack.hurtAndBreak(1, owner, stackUser -> stackUser.broadcastBreakEvent(stackUser.getUsedItemHand()));
		HornedSpearEntity spear = new HornedSpearEntity(BWEntityTypes.HORNED_SPEAR.get(), owner, world, stack.copy());
		spear.shootFromRotation(owner, owner.getXRot(), owner.getYRot(), 0, 3, 1);
		if (owner instanceof Player player) {
			if (player.isCreative()) {
				spear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
			}
		} else {
			spear.pickup = AbstractArrow.Pickup.DISALLOWED;
		}
		world.addFreshEntity(spear);
		SyncHornedSpearPacket.send(PacketDistributor.TRACKING_ENTITY.with(() -> spear), spear);
		world.playSound(null, spear, BWSoundEvents.ITEM_HORNED_SPEAR_USE.get(), SoundSource.PLAYERS, 1, 1);
		if (owner instanceof Player player && !player.isCreative()) {
			stack.shrink(1);
		}
	}
}
