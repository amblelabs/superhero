package mc.duzo.timeless.suit.api;

/**
 * Marker plus configuration for any suit that wants the nano-assemble visual on equip.
 * The actual reveal is rendered by {@code NanoAssembleSuitFeature} which queries these
 * methods to drive its custom render layer.
 */
public interface NanoAssemblableSuit {
    /** ARGB tint of the boundary glow (0xAARRGGBB). Defaults to Iron Man gold. */
    default int getNanoEdgeColor() {
        return 0xFFFFB347;
    }

    /** Maximum radius in blocks the assembly sphere reaches. */
    default float getNanoMaxRadius() {
        return 4.0f;
    }

    /** Total ticks for assembly to complete. */
    default int getNanoAssembleTicks() {
        return 30;
    }
}
