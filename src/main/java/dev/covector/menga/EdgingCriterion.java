package dev.covector.menga;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class EdgingCriterion extends AbstractCriterion<EdgingCriterion.Conditions> {
    static final Identifier ID = new Identifier("edging");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate lootContextPredicate, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        return new Conditions();
    }

    protected void trigger(ServerPlayerEntity player) {
        trigger(player, conditions -> {
            return conditions.requirementsMet();
        });
    }


    public static class Conditions
    extends AbstractCriterionConditions {

        public Conditions() {
            super(ID, LootContextPredicate.EMPTY);
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            return jsonObject;
        }

        boolean requirementsMet() {
            return true;
        }
    }
}
