package uz.shahzod.poplar;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(PoplarMod.MODID)
public class PoplarMod {

    public static final String MODID = "poplar";

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    private static AbstractBlock.Properties leavesProps() {
        return AbstractBlock.Properties.of(Material.LEAVES)
                .strength(0.2F)
                .randomTicks()
                .sound(SoundType.GRASS)
                .noOcclusion()
                .isValidSpawn((state, world, pos, type) -> false)
                .isSuffocating((state, world, pos) -> false)
                .isViewBlocking((state, world, pos) -> false);
    }

    public static final RegistryObject<Block> LOG = BLOCKS.register("poplar_log",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.of(Material.WOOD)
                    .strength(2.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> PLANKS = BLOCKS.register("poplar_planks",
            () -> new Block(AbstractBlock.Properties.of(Material.WOOD)
                    .strength(2.0F, 3.0F).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> LEAVES_RED = BLOCKS.register("poplar_leaves_red",
            () -> new LeavesBlock(leavesProps()));
    public static final RegistryObject<Block> LEAVES_ORANGE = BLOCKS.register("poplar_leaves_orange",
            () -> new LeavesBlock(leavesProps()));
    public static final RegistryObject<Block> LEAVES_YELLOW = BLOCKS.register("poplar_leaves_yellow",
            () -> new LeavesBlock(leavesProps()));

    public static final RegistryObject<Block> SAPLING = BLOCKS.register("poplar_sapling",
            () -> new SaplingBlock(new PoplarTree(), AbstractBlock.Properties.of(Material.PLANT)
                    .noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));

    private static RegistryObject<Item> blockItem(String name, RegistryObject<Block> block, ItemGroup tab) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static final RegistryObject<Item> LOG_ITEM =
            blockItem("poplar_log", LOG, ItemGroup.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Item> PLANKS_ITEM =
            blockItem("poplar_planks", PLANKS, ItemGroup.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Item> LEAVES_RED_ITEM =
            blockItem("poplar_leaves_red", LEAVES_RED, ItemGroup.TAB_DECORATIONS);
    public static final RegistryObject<Item> LEAVES_ORANGE_ITEM =
            blockItem("poplar_leaves_orange", LEAVES_ORANGE, ItemGroup.TAB_DECORATIONS);
    public static final RegistryObject<Item> LEAVES_YELLOW_ITEM =
            blockItem("poplar_leaves_yellow", LEAVES_YELLOW, ItemGroup.TAB_DECORATIONS);
    public static final RegistryObject<Item> SAPLING_ITEM =
            blockItem("poplar_sapling", SAPLING, ItemGroup.TAB_DECORATIONS);

    public PoplarMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }
}
