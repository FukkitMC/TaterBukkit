package io.github.fukkitmc.fukkit.extras;

public interface ImpossibleCriterionExtra {

	net.minecraft.advancement.criterion.ImpossibleCriterion.Conditions a(com.google.gson.JsonObject var0, net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer var1);

	void b(net.minecraft.advancement.PlayerAdvancementTracker var0, net.minecraft.advancement.criterion.Criterion.ConditionsContainer var1);

	void a(net.minecraft.advancement.PlayerAdvancementTracker var0, net.minecraft.advancement.criterion.Criterion.ConditionsContainer var1);
}
