package dev.covector.menga;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Menga implements ModInitializer {
	public static final String MODID = "menga";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static final Item MENGA_ITEM = new MengaItem();
	public static final StatusEffect BONER = new BonerStatusEffect();
	public static EdgingCriterion EDGING = Criteria.register(new EdgingCriterion());

	@Override
	public void onInitialize() {
		Registry.register(Registries.ITEM, new Identifier(MODID, "menga"), MENGA_ITEM);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
			content.add(MENGA_ITEM);
		});
		Registry.register(Registries.STATUS_EFFECT, new Identifier(MODID, "boner"), BONER);

		EdgeCallback.EVENT.register((player, edgedCount) -> {
			// player.sendMessage(Text.of("You edged " + String.valueOf(edgedCount) + " times! congrats"));
			if (edgedCount >= 3) {
				// LOGGER.info("Advancement!!!");
				EDGING.trigger((ServerPlayerEntity) player);
			}
			return ActionResult.PASS;
		});
	}
}