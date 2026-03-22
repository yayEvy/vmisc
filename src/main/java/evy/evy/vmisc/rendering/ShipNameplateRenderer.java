package evy.evy.vmisc.rendering;

import com.mojang.blaze3d.vertex.*;
import evy.evy.vmisc.VmiscConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3d;
import org.valkyrienskies.core.api.ships.ClientShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mod.EventBusSubscriber(modid = "vmisc", value = Dist.CLIENT)
public class ShipNameplateRenderer {

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {

        if (!VmiscConfig.CLIENT.renderShipNameplates.get())
            return;

        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS)
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        var shipObjectWorld = VSGameUtilsKt.getShipObjectWorld(mc.level);
        if (shipObjectWorld == null) return;

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();

        Vector3d camPos = new Vector3d(
                event.getCamera().getPosition().x,
                event.getCamera().getPosition().y,
                event.getCamera().getPosition().z
        );

        for (ClientShip ship : shipObjectWorld.getLoadedShips()) {
            String name = ship.getSlug();
            if (name == null || name.isEmpty()) continue;

            var transform = ship.getRenderTransform();
            var pos = transform.getPositionInWorld();

            double renderX = pos.x();
            double renderY = pos.y();
            double renderZ = pos.z();

            var aabb = ship.getShipAABB();
            if (aabb == null) continue;

            double height = aabb.getMax(1) - aabb.getMin(1);
            double yAbove = renderY + (height / 2.0) + 0.5;

            renderNameplate(
                    name,
                    renderX,
                    yAbove,
                    renderZ,
                    poseStack,
                    buffer,
                    camPos,
                    mc.font,
                    mc.getEntityRenderDispatcher()
            );
        }

        buffer.endBatch();
    }

    private static void renderNameplate(String text,
                                        double x, double y, double z,
                                        PoseStack poseStack,
                                        MultiBufferSource buffer,
                                        Vector3d cameraPos,
                                        Font font,
                                        EntityRenderDispatcher renderer) {

        poseStack.pushPose();

        poseStack.translate(
                x - cameraPos.x,
                y - cameraPos.y,
                z - cameraPos.z
        );

        poseStack.mulPose(renderer.cameraOrientation());

        float scale = -0.025F;
        poseStack.scale(scale, scale, scale);

        float width = font.width(text) / 2f;

        font.drawInBatch(
                text,
                -width,
                0,
                0xFFFFFF,
                false,
                poseStack.last().pose(),
                buffer,
                Font.DisplayMode.NORMAL,
                0,
                15728880
        );

        poseStack.popPose();
    }
}