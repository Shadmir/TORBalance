package com.shadmir.torbalance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ArmorEventHandler {

    private final Random random = new Random();

    public ArmorEventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        // Equalize damage taken regardless of armor durability
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            float totalProtection = 0;

            for (ItemStack armor : player.inventory.armorInventory) {
                if (armor != null && armor.getItem() instanceof ItemArmor) {
                    ItemArmor itemArmor = (ItemArmor) armor.getItem();
                    totalProtection += itemArmor.damageReduceAmount;
                }
            }

            // Apply protection enchantment multiplier
            totalProtection *= Config.protectionEnchantmentMultiplier;

            // Adjust damage based on total protection
            event.ammount = event.ammount / (1 + totalProtection / 20.0f);
        }

        // Adjust Thorns enchantment behavior
        if (event.source.getEntity() instanceof EntityPlayer) {
            EntityPlayer attacker = (EntityPlayer) event.source.getEntity();
            ItemStack[] armorInventory = attacker.inventory.armorInventory;

            // Find armor pieces with Thorns enchantment
            List<ItemStack> thornsArmor = new ArrayList<>();
            for (ItemStack armor : armorInventory) {
                if (armor != null && armor.isItemEnchanted() && armor.getEnchantmentTagList() != null) {
                    thornsArmor.add(armor);
                }
            }

            // Damage a random piece of armor with Thorns
            if (!thornsArmor.isEmpty()) {
                ItemStack randomArmor = thornsArmor.get(random.nextInt(thornsArmor.size()));
                randomArmor.damageItem(1, attacker);
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;

            for (ItemStack armor : player.inventory.armorInventory) {
                if (armor != null && armor.getItem() instanceof ItemArmor) {
                    int maxDurability = armor.getMaxDamage();
                    int currentDurability = armor.getItemDamage();

                    // Adjust durability loss rate based on maximum durability
                    double durabilityLossFactor = 1.0;

                    TORBalance.LOG.info("Max Durability: " + maxDurability);
                    TORBalance.LOG.info("Current Durability: " + currentDurability);

                    if (currentDurability > 0) {
                        int adjustedDamage = (int) Math.ceil(1 * durabilityLossFactor);
                        armor.setItemDamage(currentDurability - adjustedDamage);
                    }
                }
            }
        }
    }
}
