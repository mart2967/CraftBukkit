package org.bukkit.craftbukkit.potion;

import java.util.HashMap;

import net.minecraft.server.MobEffectList;

import org.bukkit.potion.PotionEffectType;

public class CraftPotionEffectType extends PotionEffectType {
    private final MobEffectList handle;
    private HashMap<Integer, String> idToName;

    public CraftPotionEffectType(MobEffectList handle) {
        super(handle.id);
        this.handle = handle;
    }

    private HashMap<Integer, String> initializeIdToNameMap() {
    	HashMap<Integer, String> result = new HashMap<Integer, String>();
    	result.put(1, "SPEED");
    	result.put(2, "SLOW");
    	result.put(3, "FAST_DIGGING");
    	result.put(4, "SLOW_DIGGING");
    	result.put(5, "INCREASE_DAMAGE");
    	result.put(6, "HEAL");
    	result.put(7, "HARM");
    	result.put(8, "JUMP");
    	result.put(9, "CONFUSION");
    	result.put(10, "REGENERATION");
    	result.put(11, "DAMAGE_RESISTANCE");
    	result.put(12, "FIRE_RESISTANCE");
    	result.put(13, "WATER_BREATHING");
    	result.put(14, "INVISIBILITY");
    	result.put(15, "BLINDNESS");
    	result.put(16, "NIGHT_VISION");
    	result.put(17, "HUNGER");
    	result.put(18, "WEAKNESS");
    	result.put(19, "POISON");
    	result.put(20, "WITHER");
    	result.put(21, "HEALTH_BOOST");
    	result.put(22, "ABSORPTION");
    	result.put(23, "SATURATION");
    	return result;
	}

	@Override
    public double getDurationModifier() {
        return handle.getDurationModifier();
    }

    public MobEffectList getHandle() {
        return handle;
    }

    @Override
    public String getName() {
    	if(idToName == null){
    		this.idToName = initializeIdToNameMap();
    	}
    	String name;
    	name = idToName.get(handle.id);
    	if(name == null){
    		return "UNKNOWN_EFFECT_TYPE_" + handle.id;
    	}
    	return name;
    }

    @Override
    public boolean isInstant() {
        return handle.isInstant();
    }
}
