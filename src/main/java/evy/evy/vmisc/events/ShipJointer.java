package evy.evy.vmisc.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3d;
import org.valkyrienskies.mod.api.ValkyrienSkies;
import evy.evy.vmisc.VmiscConfig;
import evy.evy.vmisc.util.joints.GTPAUtils;
import org.valkyrienskies.mod.api.ValkyrienSkies;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ShipJointer {

    private static final Map<UUID, PendingSelection> pending = new HashMap<>();

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (event.getHand() != InteractionHand.MAIN_HAND) return;
        if (!player.isShiftKeyDown()) return;

        ServerLevel level = (ServerLevel) event.getLevel();
        BlockPos clickedPos = ((BlockHitResult) event.getHitVec()).getBlockPos();

        var ship = ValkyrienSkies.getShipManagingBlock(level, clickedPos);
        Long shipId = (ship != null) ? ship.getId() : null;

        Vector3d worldPos;
        if (ship != null) {
            Vector3d shipLocal = new Vector3d(
                    clickedPos.getX() + 0.5,
                    clickedPos.getY() + 0.5,
                    clickedPos.getZ() + 0.5
            );
            worldPos = ship.getShipToWorld().transformPosition(shipLocal, new Vector3d());
        } else {
            worldPos = new Vector3d(
                    clickedPos.getX() + 0.5,
                    clickedPos.getY() + 0.5,
                    clickedPos.getZ() + 0.5
            );
        }

        Vector3d localPos;
        if (ship != null) {
            localPos = ship.getWorldToShip().transformPosition(worldPos, new Vector3d());
        } else {
            localPos = worldPos;
        }

        UUID playerId = player.getUUID();
        PendingSelection first = pending.get(playerId);

        if (first == null) {
            pending.put(playerId, new PendingSelection(shipId, localPos, worldPos));
            player.sendSystemMessage(
                    net.minecraft.network.chat.Component.literal(
                            "First point selected"
                    )
            );
            event.setCanceled(true);
            return;
        }

        pending.remove(playerId);

        if (first.shipId != null && first.shipId.equals(shipId)) {
            player.sendSystemMessage(
                    net.minecraft.network.chat.Component.literal(
                            "Both blocks are on the same ship. Joint not created lol."
                    )
            );
            event.setCanceled(true);
            return;
        }

        double dist = first.worldPos.distance(worldPos);
        double maxDist = VmiscConfig.SERVER.maxJointDistance.get();
        if (dist > maxDist) {
            player.sendSystemMessage(
                    net.minecraft.network.chat.Component.literal(
                            String.format("Too far apart !!!!!! (%.1f > %.1f blocks)", dist, maxDist)
                    )
            );
            event.setCanceled(true);
            return;
        }

        GTPAUtils.addDistanceJoint(
                level,
                player,
                first.shipId, first.localPos, first.worldPos,
                shipId, localPos, worldPos
        );

        event.setCanceled(true);
    }

    private record PendingSelection(Long shipId, Vector3d localPos, Vector3d worldPos) {}
}