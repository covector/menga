package dev.covector.menga;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public interface PPRendererAccessor {
    public void renderPP(MatrixStack matrices, VertexConsumer vertices, int light, int overlay);
    public void renderBalls(MatrixStack matrices, VertexConsumer vertices, int light, int overlay);
}
