package de.timesnake.library.reflection.wrapper;

import de.timesnake.library.reflection.NmsReflection;
import net.minecraft.world.entity.EnumItemSlot;
import org.bukkit.inventory.EquipmentSlot;

@NmsReflection
public enum ExEnumItemSlot {

    MAIN_HAND(EnumItemSlot.a),
    OFFHAND(EnumItemSlot.b),
    HEAD(EnumItemSlot.f),
    FEET(EnumItemSlot.c),
    LEGS(EnumItemSlot.d),
    CHEST(EnumItemSlot.e);

    private final EnumItemSlot nmsSlot;

    ExEnumItemSlot(EnumItemSlot nmsSlot) {
        this.nmsSlot = nmsSlot;
    }

    public EnumItemSlot getNmsSlot() {
        return nmsSlot;
    }

    public static ExEnumItemSlot parseEquipmentSlot(EquipmentSlot slot) {
        switch (slot) {
            case HAND:
                return ExEnumItemSlot.MAIN_HAND;
            case OFF_HAND:
                return ExEnumItemSlot.OFFHAND;
            case FEET:
                return ExEnumItemSlot.FEET;
            case LEGS:
                return ExEnumItemSlot.LEGS;
            case CHEST:
                return ExEnumItemSlot.CHEST;
            case HEAD:
                return ExEnumItemSlot.HEAD;

        }
        return null;
    }
}
