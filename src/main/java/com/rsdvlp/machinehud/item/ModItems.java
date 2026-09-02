package com.rsdvlp.machinehud.item;

import com.rsdvlp.machinehud.MachineHUD;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModItems {

    // MachineHUDで追加するItemを登録するためのRegistry。
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MachineHUD.MODID);

    // HUDを表示するためのゴーグル。
    //
    // 今回はまずArmorItem.Type.HELMETとして登録し、
    // プレイヤーの頭装備スロットへ装備できるようにする。
    public static final Supplier<ArmorItem> MACHINE_HUD_GOGGLES =
            ITEMS.register(
                    "machine_hud_goggles",
                    () -> new MachineHudGogglesItem(
                            ArmorMaterials.LEATHER,
                            new Item.Properties()
                                    .durability(128)
                    )
            );

    private ModItems() {
    }
}