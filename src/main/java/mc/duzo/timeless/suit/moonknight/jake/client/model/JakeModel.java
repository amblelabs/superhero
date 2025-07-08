package mc.duzo.timeless.suit.moonknight.jake.client.model;

import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.moonknight.marc.client.model.MoonKnightModel;
import mc.duzo.timeless.suit.set.SetRegistry;

public class JakeModel extends MoonKnightModel {
    private ClientSuit parent;

    @Override
    public ClientSuit getSuit() {
        if (this.parent == null) {
            this.parent = SetRegistry.MOON_KNIGHT_JAKE.suit().toClient();
        }

        return this.parent;
    }

    @Override
    public boolean hasMoonKnightCape() {
        return false; // Jake does not have a cape
    }
}
