package evy.evy.vmisc;

import net.minecraftforge.common.ForgeConfigSpec;

public class VmiscConfig {

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;

    public static final ForgeConfigSpec SERVER_SPEC;
    public static final Server SERVER;

    static {
        ForgeConfigSpec.Builder clientBuilder = new ForgeConfigSpec.Builder();
        CLIENT = new Client(clientBuilder);
        CLIENT_SPEC = clientBuilder.build();

        ForgeConfigSpec.Builder serverBuilder = new ForgeConfigSpec.Builder();
        SERVER = new Server(serverBuilder);
        SERVER_SPEC = serverBuilder.build();
    }

    public static class Client {
        public final ForgeConfigSpec.BooleanValue renderShipNameplates;

        Client(ForgeConfigSpec.Builder builder) {
            builder.push("rendering");
            renderShipNameplates = builder
                    .comment("Render ship nameplates above ships")
                    .define("renderShipNameplates", false);
            builder.pop();
        }
    }

    public static class Server {
        public final ForgeConfigSpec.DoubleValue whackForceStrength;

        Server(ForgeConfigSpec.Builder builder) {
            builder.push("general");

            whackForceStrength = builder
                    .comment("The base force applied when smacking a ship with Knockback. Multiplied by enchantment level.")
                    .defineInRange("whackForceStrength", 1000000.0, 0.0, 1e12);

            builder.pop();
        }
    }
}