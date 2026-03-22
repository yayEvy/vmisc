package evy.evy.vmisc.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class ShipRenamer {

    public InteractionResult useOn(BlockState state, Level level, BlockPos pos,
                                   Player player, InteractionHand hand, BlockHitResult hit) {

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() != Items.NAME_TAG) {
            return InteractionResult.PASS;
        }

        if (!stack.hasCustomHoverName()) {
            return InteractionResult.PASS;
        }

        ServerShip ship = VSGameUtilsKt.getShipManagingPos((net.minecraft.server.level.ServerLevel) level, pos);

        if (ship == null) {
            return InteractionResult.PASS;
        }

        String newSlug = stack.getHoverName().getString();

        ship.setSlug(newSlug);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        player.displayClientMessage(Component.literal("Ship renamed to: " + newSlug), true);

        return InteractionResult.SUCCESS;
    }
}
