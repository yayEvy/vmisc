package evy.evy.vmisc;

import com.mojang.logging.LogUtils;
import evy.evy.vmisc.whack.ShipSmackAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.valkyrienskies.core.api.VsBeta;
import org.valkyrienskies.mod.common.ValkyrienSkiesMod;

@Mod(Vmisc.MODID)
public class Vmisc {

    public static final String MODID = "vmisc";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Vmisc() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, VmiscConfig.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, VmiscConfig.SERVER_SPEC);

        modEventBus.addListener(this::commonSetup);
    }
    @VsBeta
    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            registerAttachments();
        });
    }

    public static void registerAttachments() {
        LOGGER.info("Registering {} attachments...", Vmisc.MODID);


        // who up smacking they ships
        ValkyrienSkiesMod.getApi().registerAttachment(
                ShipSmackAttachment.class, builder -> {
                    builder.build();
                    return null;
                }
        );

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do gay when the server starts
        LOGGER.info("HELLO i am gay");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("HELLO FROM gay SETUP");
        }
    }


}
