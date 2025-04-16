package com.shadmir.torbalance;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static String greeting = "Hello World";
    public static double protectionEnchantmentMultiplier = 1.0;
    public static double durabilityAdjustmentFactor = 1.0;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        greeting = configuration.getString("greeting", Configuration.CATEGORY_GENERAL, greeting, "How shall I greet?");
        protectionEnchantmentMultiplier = configuration
            .get(
                Configuration.CATEGORY_GENERAL,
                "protectionEnchantmentMultiplier",
                protectionEnchantmentMultiplier,
                "Multiplier for protection enchantment effectiveness.")
            .getDouble();
        durabilityAdjustmentFactor = configuration
            .get(
                Configuration.CATEGORY_GENERAL,
                "durabilityAdjustmentFactor",
                durabilityAdjustmentFactor,
                "Factor to adjust armor durability loss.")
            .getDouble();

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
