package deathgrave.inventory;

import necesse.engine.network.server.ServerClient;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.InventoryObjectEntity;
import necesse.entity.objectEntity.ObjectEntity;
    import necesse.entity.pickup.ItemPickupEntity;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.ToolType;
import necesse.level.gameObject.furniture.StorageBoxInventoryObject;
import necesse.level.maps.Level;

import java.awt.*;
import java.util.ArrayList;

public class DeathGrave extends StorageBoxInventoryObject {

    @Override
    public void interact(Level level, int x, int y, PlayerMob player) {
        if (level.isServer()) {
            DeathGrave.DeathGraveInventoryObjectEntity objectEntity = (DeathGrave.DeathGraveInventoryObjectEntity) level.entityManager.getObjectEntity(x, y);
            if (player.getNetworkClient().getName().equals(objectEntity.ownerName)) {
                ArrayList<ItemPickupEntity> itemsDropped = new ArrayList<>();
                for (int i = 0; i <= objectEntity.slots; i++) {
                    InventoryItem item = objectEntity.inventory.getItem(i);
                    if (item != null) {
                        ItemPickupEntity itemPickupEntity = new ItemPickupEntity(level, item, x, y, player.getDrawX(), player.getDrawY());
                        itemsDropped.add(itemPickupEntity);
                    }
                }
                super.onDestroyed(level, objectEntity.getLevelObject().layerID, objectEntity.getX(), objectEntity.getY(), player, null, itemsDropped);
                level.setObject(objectEntity.getX(), objectEntity.getY(), 0);
                level.sendObjectUpdatePacket(objectEntity.getX(), objectEntity.getY());
                objectEntity.remove();
            }
        }
    }

    public DeathGrave() {
        super("kew_grave", 50, ToolType.UNBREAKABLE, new Color(83, 67, 119));
        this.canPlaceOnLiquid = false;
        this.canPlaceOnShore = false;
    }


    @Override
    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        return new DeathGraveInventoryObjectEntity(level, x, y, this.slots);
    }

    public static class DeathGraveInventoryObjectEntity extends InventoryObjectEntity {

        private String ownerName = null;

        public DeathGraveInventoryObjectEntity(Level level, int x, int y, int slots) {
            super(level, x, y, slots);
        }

        public void setServerClient(ServerClient ownerClient) {
            ownerName = ownerClient.playerMob.playerName;
        }

        @Override
        public void addSaveData(SaveData save) {
            super.addSaveData(save);
            ArrayList<String> playerNames = new ArrayList<>();
            playerNames.add(ownerName);
            save.addStringList("ownerName", playerNames);
        }

        @Override
        public void applyLoadData(LoadData save) {
            super.applyLoadData(save);
            ownerName = save.getStringList("ownerName", new ArrayList<>()).get(0);
        }
    }

}