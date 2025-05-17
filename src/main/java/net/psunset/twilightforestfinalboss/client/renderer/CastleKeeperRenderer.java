package net.psunset.twilightforestfinalboss.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.psunset.twilightforestfinalboss.client.layer.CastleKeeperLayer;
import net.psunset.twilightforestfinalboss.client.model.CastleKeeperModel;
import net.psunset.twilightforestfinalboss.entity.boss.CastleKeeper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CastleKeeperRenderer extends GeoEntityRenderer<CastleKeeper> {
    public CastleKeeperRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CastleKeeperModel());
        this.shadowRadius = 0.8F;
        this.addRenderLayer(new CastleKeeperLayer(this));
    }

    @Override
    public RenderType getRenderType(CastleKeeper animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(this.getTextureLocation(animatable));
    }

    @Override
    public void preRender(PoseStack poseStack, CastleKeeper animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        float scale = 1.0F;
        this.scaleHeight = scale;
        this.scaleWidth = scale;
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    protected float getDeathMaxRotation(CastleKeeper entityLivingBaseIn) {
        return 0.0F;
    }
}
