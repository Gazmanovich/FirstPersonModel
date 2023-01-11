package dev.tr7zw.firstperson.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.firstperson.FirstPersonModelCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

// lower prio to run before fabric api
@Mixin(value = HumanoidArmorLayer.class, priority = 100)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>>
        extends RenderLayer<T, M> {

    private static Minecraft fpm_mc = Minecraft.getInstance();
    
    public ArmorFeatureRendererMixin(RenderLayerParent<T, M> context) {
        super(context);
    }

    @Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
    private void renderArmor(PoseStack matrices, MultiBufferSource vertexConsumers, T livingEntity,
            EquipmentSlot equipmentSlot, int i, A bipedEntityModel, CallbackInfo info) {
        if(livingEntity != fpm_mc.cameraEntity) {
            return;
        }
        if(equipmentSlot == EquipmentSlot.HEAD && FirstPersonModelCore.isRenderingPlayer) {
            info.cancel();
        }
        if(equipmentSlot == EquipmentSlot.CHEST && FirstPersonModelCore.isRenderingPlayer
                && (livingEntity instanceof LocalPlayer player && player.isSwimming())){
            info.cancel();
        }
        if(equipmentSlot == EquipmentSlot.CHEST && FirstPersonModelCore.isRenderingPlayer
                && FirstPersonModelCore.instance.showVanillaHands()) {
            info.cancel();
        }
    }

}
