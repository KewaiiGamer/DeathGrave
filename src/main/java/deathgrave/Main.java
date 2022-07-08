package deathgrave;

import deathgrave.inventory.DeathGrave;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.ObjectRegistry;

@ModEntry
public class Main {
    public Main() {
    }

    public void init() {
        ObjectRegistry.registerObject("kew_grave", new DeathGrave(), 10.0F, false);
    }
}