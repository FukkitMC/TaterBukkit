package io.github.fukkitmc.fukkit.extras;

public interface AbstractCriterionExtra {

	void b(net.minecraft.advancement.PlayerAdvancementTracker var0, net.minecraft.advancement.criterion.Criterion.ConditionsContainer var1);

	net.minecraft.advancement.criterion.AbstractCriterionConditions a(com.google.gson.JsonObject var0, net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer var1);

	net.minecraft.advancement.criterion.AbstractCriterionConditions b(com.google.gson.JsonObject var0, net.minecraft.predicate.entity.EntityPredicate.Extended var1, net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer var2);

	void a(net.minecraft.advancement.PlayerAdvancementTracker var0, net.minecraft.advancement.criterion.Criterion.ConditionsContainer var1);
}
