package evy.evy.vmisc.events;

import evy.evy.vmisc.util.ShipRenamer;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.server.level.ServerLevel;

@Mod.EventBusSubscriber(modid = "vmisc")
public class ShipRenameHandler {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {

        if (!(event.getLevel() instanceof ServerLevel serverLevel))
            return;

        ShipRenamer renamer = new ShipRenamer();

        InteractionResult result = renamer.useOn(
                serverLevel.getBlockState(event.getPos()),
                serverLevel,
                event.getPos(),
                event.getEntity(),
                event.getHand(),
                event.getHitVec()
        );

        if (result.consumesAction()) {
            event.setCanceled(true);
        }
    }
}