package com.rsdvlp.machinehud;

import com.rsdvlp.machinehud.config.ClientConfig;
import com.rsdvlp.machinehud.item.ModItems;
import com.rsdvlp.machinehud.model.MachineHudGogglesModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MachineHUD.MODID)
public class MachineHUD {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "machinehud";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "machinehud" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "machinehud" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "machinehud" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Block with the id "machinehud:example_block", combining the namespace and path
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    // Creates a new BlockItem with the id "machinehud:example_block", combining the namespace and path
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    // Creates a new food item with the id "machinehud:example_id", nutrition 1 and saturation 2
    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public MachineHUD(IEventBus modEventBus, ModContainer modContainer) {
        /*
         * Machine HUD Gogglesのモデルレイヤー登録イベントを、
         * MOD専用Event Busへ登録する。
         *
         * @EventBusSubscriber(bus = Bus.MOD)を使用せず、
         * NeoForge 1.21.1以降の方式としてEvent Busへ直接登録する。
         */
        modEventBus.addListener(
                MachineHUD::registerLayerDefinitions
        );
        // Machine HUD Gogglesへ
        // クライアント専用の描画拡張を登録する。
        modEventBus.addListener(
                MachineHUD::registerClientExtensions
        );
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // MachineHUDで追加するItemをNeoForgeへ登録する。
        ModItems.ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (MachineHUD) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // MachineHUDのクライアント専用設定をNeoForgeへ登録する。
        // CLIENTを指定することでHUD表示など、
        // 各プレイヤーのクライアント側だけで使用する設定として扱われる。
        modContainer.registerConfig(
                ModConfig.Type.CLIENT,
                ClientConfig.SPEC
        );
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(EXAMPLE_BLOCK_ITEM);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    /**
     * Machine HUD Gogglesのモデル形状を
     * Minecraftのモデルシステムへ登録する。
     */
    private static void registerLayerDefinitions(
            EntityRenderersEvent.RegisterLayerDefinitions event
    ) {

        // ModelLayerLocationと、
        // 実際のモデル形状を生成するcreateBodyLayer()を関連付ける。
        event.registerLayerDefinition(
                com.rsdvlp.machinehud.model.ModModelLayers.MACHINE_HUD_GOGGLES,
                MachineHudGogglesModel::createBodyLayer
        );
    }

    /**
     * Machine HUD Gogglesへ
     * クライアント専用の描画処理を登録する。
     */
    private static void registerClientExtensions(
            RegisterClientExtensionsEvent event
    ) {

        event.registerItem(

                new IClientItemExtensions() {

                    /**
                     * Machine HUD Gogglesを装備した際に使用する
                     * HumanoidModelを返す。
                     */
                    @Override
                    public @NotNull HumanoidModel<?> getHumanoidArmorModel(
                            @NotNull LivingEntity livingEntity,
                            @NotNull ItemStack itemStack,
                            @NotNull EquipmentSlot equipmentSlot,
                            @NotNull HumanoidModel<?> original
                    ) {

                        // 頭装備以外として描画される場合は、
                        // Minecraft標準モデルをそのまま使用する。
                        if (equipmentSlot != EquipmentSlot.HEAD) {
                            return original;
                        }

                        // Machine HUD Goggles専用モデルを返す。
                        return MachineHudGogglesClient.getModel();
                    }
                },
                new Item[] {// このClient Extensionを適用するItem。
                        ModItems.MACHINE_HUD_GOGGLES.get()
                });
    }
}
