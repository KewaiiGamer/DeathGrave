package deathgrave;

import deathgrave.inventory.DeathGrave;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.registries.ObjectRegistry;
import necesse.entity.mobs.Attacker;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.InventoryObjectEntity;
import necesse.entity.pickup.ItemPickupEntity;
import necesse.entity.pickup.PickupEntity;
import net.bytebuddy.asm.Advice;

import java.util.HashSet;
import java.util.Iterator;

@ModMethodPatch(
        target = PlayerMob.class,
        name = "onDeath",
        arguments = {Attacker.class, HashSet.class}
)
public class DeathPatch {
    @Advice.OnMethodExit
    static void onExit(@Advice.This Mob mob, @Advice.Argument(0) Attacker attacker, @Advice.Argument(1) HashSet<Attacker> attackers) {
        DeathGrave deathGrave = (DeathGrave) ObjectRegistry.getObject("kew_grave");
        int tileX = mob.getTileX();
        int tileY = mob.getTileY();
        deathGrave.placeObject(mob.getLevel(), tileX, tileY, 0);
        InventoryObjectEntity objectEntity = (InventoryObjectEntity)mob.getLevel().entityManager.getObjectEntity(tileX, tileY);

        for (PickupEntity pickupEntity : mob.getLevel().entityManager.pickups) {
            ItemPickupEntity next = (ItemPickupEntity) pickupEntity;
            objectEntity.getInventory().addItem(mob.getLevel(), null, next.item, "death");
        }

        deathGrave.setServerClient(((PlayerMob)mob).getServerClient());
        mob.getLevel().sendObjectUpdatePacket(tileX, tileY);
    }
}
