package de.timesnake.library.reflection.wrapper;

import de.timesnake.library.reflection.NmsReflection;
import net.minecraft.world.EnumHand;

@NmsReflection
public enum ExEnumHand {

    MAIN_HAND(EnumHand.a),
    OFF_HAND(EnumHand.b);

    private final EnumHand hand;

    ExEnumHand(EnumHand hand) {
        this.hand = hand;
    }

    public static ExEnumHand fromNms(EnumHand hand) {
        for (ExEnumHand exHand : ExEnumHand.values()) {
            if (exHand.getNMS().equals(hand)) {
                return exHand;
            }
        }
        return null;
    }

    public EnumHand getNMS() {
        return this.hand;
    }
}
