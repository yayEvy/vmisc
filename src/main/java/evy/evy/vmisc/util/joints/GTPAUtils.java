package evy.evy.vmisc.util.joints;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.valkyrienskies.core.internal.joints.VSDistanceJoint;
import org.valkyrienskies.core.internal.joints.VSJointMaxForceTorque;
import org.valkyrienskies.core.internal.joints.VSJointPose;
import org.valkyrienskies.mod.api.ValkyrienSkies;
import org.valkyrienskies.mod.common.ValkyrienSkiesMod;
import org.valkyrienskies.mod.common.util.GameToPhysicsAdapter;

public class GTPAUtils {

    public static GameToPhysicsAdapter getGTPA(ServerLevel level) {
        String dimId = ValkyrienSkies.getDimensionId(level);
        return ValkyrienSkiesMod.getOrCreateGTPA(dimId);
    }

    public static void addDistanceJoint(
            ServerLevel level,
            ServerPlayer player,
            Long ship0, Vector3d localPos0, Vector3d worldPos0,
            Long ship1, Vector3d localPos1, Vector3d worldPos1
    ) {
        GameToPhysicsAdapter gtpa = getGTPA(level);

        Quaterniond identityRot = new Quaterniond();

        VSJointPose pose0 = new VSJointPose(localPos0, identityRot);
        VSJointPose pose1 = new VSJointPose(localPos1, identityRot);

        float maxForce = 1e12f;
        float maxTorque = 1e12f;
        VSJointMaxForceTorque maxFT = new VSJointMaxForceTorque(maxForce, maxTorque);

        double compliance = 1e-12;

        float distance = (float) worldPos0.distance(worldPos1);

        VSDistanceJoint joint = new VSDistanceJoint(
                ship0, pose0,
                ship1, pose1,
                maxFT,
                compliance,
                distance,
                distance,
                null,
                1e12f,
                null
        );

        gtpa.addJoint(joint, 0, jointId -> {
            if (jointId == -1) {
                player.sendSystemMessage(Component.literal("Failed to create joint xd."));
                return;
            }
            player.sendSystemMessage(Component.literal("Joint created xd rofl! (id " + jointId + ")"));
        });
    }
}