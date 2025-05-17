package net.psunset.twilightforestfinalboss.client.model;

import net.minecraft.resources.ResourceLocation;
import net.psunset.twilightforestfinalboss.entity.boss.CastleKeeper;
import net.psunset.twilightforestfinalboss.tool.RLUtl;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class CastleKeeperModel extends GeoModel<CastleKeeper> {
    public ResourceLocation getAnimationResource(CastleKeeper entity) {
        return RLUtl.of("animations/castle_keeper.animation.json");
    }

    public ResourceLocation getModelResource(CastleKeeper entity) {
        return RLUtl.of("geo/castle_keeper.geo.json");
    }

    public ResourceLocation getTextureResource(CastleKeeper entity) {
        return RLUtl.of("textures/entity/" + entity.getTexture() + ".png");
    }

    public void setCustomAnimations(CastleKeeper animatable, long instanceId, AnimationState animationState) {
        GeoBone head = this.getAnimationProcessor().getBone("bone5");
        if (head != null) {
            EntityModelData entityData = (EntityModelData)animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * ((float)Math.PI / 180F));
            head.setRotY(entityData.netHeadYaw() * ((float)Math.PI / 180F));
        }

    }
}
