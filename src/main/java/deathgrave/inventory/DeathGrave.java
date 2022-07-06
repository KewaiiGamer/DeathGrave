package deathgrave.inventory;

import java.awt.Color;
import java.lang.reflect.Array;
import java.util.ArrayList;
import necesse.engine.network.server.ServerClient;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.InventoryObjectEntity;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.entity.pickup.ItemPickupEntity;
import necesse.level.gameObject.furniture.StorageBoxInventoryObject;
import necesse.level.maps.Level;

public class DeathGrave extends StorageBoxInventoryObject {
    String ownerName = null;

    public boolean canInteract(Level level, int x, int y, PlayerMob player) {
        return this.ownerName == null || (player != null && player.playerName.equals(this.ownerName));
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
        this.ownerName = ownerClient.playerMob.playerName;
    }

    public void onDestroyed(Level level, int x, int y, ServerClient client, ArrayList<ItemPickupEntity> itemsDropped) {
        if (this.ownerName == null) {
            super.onDestroyed(level, x, y, client, itemsDropped);
        } else if (client != null && client.playerMob.playerName.equals(this.ownerName)) {
            super.onDestroyed(level, x, y, client, itemsDropped);
        }

    }
    @Override
    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        return new DeathGraveInventoryObjectEntity(level, x, y, this.slots);
    }

    public static class DeathGraveInventoryObjectEntity extends InventoryObjectEntity {

        public DeathGraveInventoryObjectEntity(Level level, int x, int y, int slots) {
            super(level, x, y, slots);
        }

        @Override
        public void addSaveData(SaveData save) {
            super.addSaveData(save);
            DeathGrave deathGrave = (DeathGrave) this.getObject();
            ArrayList<String> playerNames = new ArrayList<>();
            playerNames.add(deathGrave.ownerName);
            save.addStringList("ownerName", playerNames);
        }

        @Override
        public void applyLoadData(LoadData save) {
            super.applyLoadData(save);
            DeathGrave deathGrave = (DeathGrave) this.getObject();
            deathGrave.ownerName = save.getStringList("ownerName", new ArrayList<>()).get(0);
        }
    }

}