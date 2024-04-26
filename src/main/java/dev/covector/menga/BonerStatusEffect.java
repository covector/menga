package dev.covector.menga;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class BonerStatusEffect extends StatusEffect {
    public BonerStatusEffect() {
      super(
        StatusEffectCategory.BENEFICIAL,
        0xFCF9F0);
    }
   
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
      return false;
    }
}
