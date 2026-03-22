package evy.evy.vmisc.whack;

import evy.evy.vmisc.VmiscConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import org.joml.Vector3d;
import org.valkyrienskies.core.api.ships.LoadedServerShip;

@net.minecraftforge.fml.common.Mod.EventBusSubscriber
public class ShipSmacker {

    @SubscribeEvent
    public static void onSmackShip(PlayerInteractEvent.LeftClickBlock event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        ServerPlayer player = (ServerPlayer) event.getEntity();
        ItemStack weapon = player.getMainHandItem();
        int knockback = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.KNOCKBACK, weapon);

        if (knockback <= 0) return;

        LoadedServerShip serverShip = AttachmentUtils.getShipAt(level, event.getPos());
        if (serverShip == null) return;

        double baseStrength = VmiscConfig.SERVER.whackForceStrength.get();
        double totalStrength = baseStrength * knockback;

        Vector3d force = new Vector3d(
                player.getLookAngle().x,
                player.getLookAngle().y,
                player.getLookAngle().z
        );

        if (force.lengthSquared() > 1e-6) {
            force.normalize().mul(totalStrength);

            ShipSmackAttachment attachment = ShipSmackAttachment.getOrCreate(serverShip);
            attachment.addForce(force);
        }
    }
}