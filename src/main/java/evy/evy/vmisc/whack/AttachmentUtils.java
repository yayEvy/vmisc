package evy.evy.vmisc.whack;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import org.valkyrienskies.core.api.ships.LoadedServerShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class AttachmentUtils {

    private AttachmentUtils() {}

    @Nullable
    public static LoadedServerShip getShipAt(ServerLevel level, BlockPos pos) {
        Ship ship = VSGameUtilsKt.getShipObjectManagingPos(level, pos);

        if (ship instanceof LoadedServerShip loadedShip) {
            return loadedShip;
        }

        return null;
    }

    public static <T> T getOrCreate(LoadedServerShip ship, Class<T> attachmentClass, Supplier<T> factory) {

        T attachment = ship.getAttachment(attachmentClass);

        if (attachment == null) {
            attachment = factory.get();
            ship.setAttachment(attachmentClass, attachment);
        }

        return attachment;
    }

    @Nullable
    public static <T> T get(Level level, BlockPos pos, Class<T> attachmentClass, Supplier<T> factory) {

        if (!(level instanceof ServerLevel serverLevel)) {
            return null;
        }

        LoadedServerShip ship = getShipAt(serverLevel, pos);

        if (ship == null) {
            return null;
        }

        return getOrCreate(ship, attachmentClass, factory);
    }
}