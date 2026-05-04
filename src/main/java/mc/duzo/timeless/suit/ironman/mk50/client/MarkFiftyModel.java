package mc.duzo.timeless.suit.ironman.mk50.client;

import mc.duzo.timeless.suit.client.ClientSuit;
import mc.duzo.timeless.suit.ironman.client.GenericIronManModel;
import mc.duzo.timeless.suit.set.SetRegistry;

public class MarkFiftyModel extends GenericIronManModel {
    private ClientSuit parent;

    @Override
    public ClientSuit getSuit() {
        if (this.parent == null) {
            this.parent = SetRegistry.MARK_FIFTY.suit().toClient();
        }
        return this.parent;
    }
}
