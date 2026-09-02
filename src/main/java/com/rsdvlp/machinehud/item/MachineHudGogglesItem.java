package com.rsdvlp.machinehud.item;

import com.rsdvlp.machinehud.MachineHUD;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Machine HUD Goggles本体。
 *
 * 通常のHelmetとして装備できるようArmorItemを継承する。
 * クライアント側の専用モデル登録は別クラスで行う。
 */
public class MachineHudGogglesItem extends ArmorItem {

    // Machine HUD Goggles専用の装備テクスチャ。
    //
    // 実際のファイル:
    // assets/machinehud/textures/models/armor/machine_hud_goggles.png
    private static final ResourceLocation ARMOR_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    MachineHUD.MODID,
                    "textures/models/armor/machine_hud_goggles.png"
            );


    public MachineHudGogglesItem(
            Holder<ArmorMaterial> material,
            Item.Properties properties
    ) {

        // ゴーグルは頭装備として使用するため、
        // ArmorItem.Type.HELMETを指定する。
        super(
                material,
                ArmorItem.Type.HELMET,
                properties
        );
    }


    /**
     * Machine HUD Gogglesを装備したときに使用する
     * Armorテクスチャを返す。
     *
     * NeoForgeのClientHooks#getArmorTexture()から
     * このメソッドが呼び出される。
     */
    @Override
    public ResourceLocation getArmorTexture(
            ItemStack stack,
            Entity entity,
            EquipmentSlot slot,
            ArmorMaterial.Layer layer,
            boolean innerModel
    ) {

        // Machine HUD Gogglesでは、
        // ArmorMaterial側の標準テクスチャではなく
        // MachineHUD専用テクスチャを使用する。
        return ARMOR_TEXTURE;
    }
}