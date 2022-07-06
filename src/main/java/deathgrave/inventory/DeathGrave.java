package deathgrave.inventory;

import java.awt.Color;
import java.util.ArrayList;
import necesse.engine.network.server.ServerClient;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.pickup.ItemPickupEntity;
import necesse.level.gameObject.furniture.StorageBoxInventoryObject;
import necesse.level.maps.Level;

public class DeathGrave extends StorageBoxInventoryObject {
    ServerClient ownerClient = null;

    public boolean canInteract(Level level, int x, int y, PlayerMob player) {
        return this.ownerClient == null || player != null && player.equals(this.ownerClient.playerMob);
    }

    public void interact(Level level, int x, int y, PlayerMob player) {
        if (level.isServerLevel() && this.canInteract(level, x, y, player)) {
            this.onDestroyed(level, x, y, player.getServerClient(), null);
        }

    }

    public DeathGrave() {
        super("kew_grave", 50, new Color(83, 67, 119));
    }

    public void setServerClient(ServerClient ownerClient) {
        this.ownerClient = ownerClient;
    }

    public void onDestroyed(Level level, int x, int y, ServerClient client, ArrayList<ItemPickupEntity> itemsDropped) {
        if (this.ownerClient == null) {
            super.onDestroyed(level, x, y, client, itemsDropped);
        } else if (client != null && client.equals(this.ownerClient)) {
            super.onDestroyed(level, x, y, client, itemsDropped);
        }

    }
}
