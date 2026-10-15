package uz.shahzod.poplar;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = PoplarMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(PoplarMod.SAPLING.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(PoplarMod.LEAVES_RED.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(PoplarMod.LEAVES_ORANGE.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(PoplarMod.LEAVES_YELLOW.get(), RenderType.cutoutMipped());
        });
    }
}
