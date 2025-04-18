package mc.duzo.timeless.suit.spiderman.nwh.client;

import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.spiderman.client.GenericSpiderManModel;

public class NWHModel extends GenericSpiderManModel {
    private ClientSuit parent;

    @Override
    public ClientSuit getSuit() {
        if (this.parent == null) {
            this.parent = SetRegistry.NWH.suit().toClient();
        }

        return this.parent;
    }
}
