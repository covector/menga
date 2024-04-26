package dev.covector.menga;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.*;

import java.util.function.Consumer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
 
public class MengaDataGenerator implements DataGeneratorEntrypoint {
 
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
 
        pack.addProvider(AdvancementsProvider::new);
    }
 
    static class AdvancementsProvider extends FabricAdvancementProvider {
        protected AdvancementsProvider(FabricDataOutput dataGenerator) {
            super(dataGenerator);
        }
 
        @Override
        public void generateAdvancement(Consumer<Advancement> consumer) {
            Advancement.Builder.createUntelemetered()
                    .display(
                            Menga.MENGA_ITEM,
                            Text.literal("Edging"),
                            Text.literal("On your way to the edge of glory!"),
                            new Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                            AdvancementFrame.CHALLENGE,
                            true,
                            true,
                            false
                    )
                    .criterion("edging", new EdgingCriterion.Conditions())
                    .build(consumer, Menga.MODID + "/root");
        }
    }
}