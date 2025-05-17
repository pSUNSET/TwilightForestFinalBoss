package net.psunset.twilightforestfinalboss.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.psunset.twilightforestfinalboss.entity.boss.CastleKeeper;
import net.psunset.twilightforestfinalboss.tool.RLUtl;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class CastleKeeperLayer extends GeoRenderLayer<CastleKeeper> {
    private static final ResourceLocation LAYER = RLUtl.of("textures/entity/castle_keeper_glow.png");

    public CastleKeeperLayer(GeoRenderer<CastleKeeper> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, CastleKeeper animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType glowRenderType = RenderType.eyes(LAYER);
        this.getRenderer().reRender(this.getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color(255, 255, 255, 255));
    }
}
