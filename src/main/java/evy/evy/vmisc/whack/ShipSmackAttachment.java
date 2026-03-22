package evy.evy.vmisc.whack;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3d;
import org.jetbrains.annotations.NotNull;

import org.valkyrienskies.core.api.ships.LoadedServerShip;
import org.valkyrienskies.core.api.ships.ShipPhysicsListener;
import org.valkyrienskies.core.api.ships.PhysShip;
import org.valkyrienskies.core.api.world.PhysLevel;

public final class ShipSmackAttachment implements ShipPhysicsListener {

    private final List<Vector3d> pendingForces = new java.util.concurrent.CopyOnWriteArrayList<>();

    public static ShipSmackAttachment getOrCreate(LoadedServerShip ship) {
        return AttachmentUtils.getOrCreate(ship, ShipSmackAttachment.class, () -> {
            ShipSmackAttachment attachment = new ShipSmackAttachment();
            ship.setAttachment(ShipSmackAttachment.class, attachment);
            return attachment;
        });
    }
    public void addForce(Vector3d force) {
        pendingForces.add(force);
    }

    @Override
    public void physTick(@NotNull PhysShip ship, @NotNull PhysLevel level) {

        for (Vector3d force : pendingForces) {
            ship.applyInvariantForce(force);
        }

        pendingForces.clear();
    }
}