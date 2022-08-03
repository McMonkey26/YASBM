package com.pew.yetanotherskyblockmod.tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.pew.yetanotherskyblockmod.YASBM;
import com.pew.yetanotherskyblockmod.config.ModConfig;
import com.pew.yetanotherskyblockmod.util.Utils;

import net.minecraft.client.option.KeyBinding;

public class Keys implements com.pew.yetanotherskyblockmod.Features.Feature {
    private Map<KeyBinding, String> binds = new HashMap<>();

    @Override
    public void init() {updateCache();}
    public void onConfigUpdate() {updateCache();}
    public void tick() {
        for (Entry<KeyBinding, String> keypair : binds.entrySet()) {
            if (!keypair.getKey().isPressed()) continue;
            Utils.command(keypair.getValue());
            break;
        }
    }

    private void updateCache() {
        binds.clear();
        ModConfig.get().tools.keys.forEach((Character key, String command) -> {
            add(key, command);
        });
    }

    // Local manipulation commands
    public boolean add(char key, String command) {
        command = command.trim();
        if (binds.containsValue(command)) return false;
        return binds.putIfAbsent(
            new KeyBinding(
                "customkey."+key,
                java.awt.event.KeyEvent.getExtendedKeyCodeForChar(key),
                "key.categories."+YASBM.MODID+".custom"
            ),
            command
        ) == null;
    }
    public boolean remove(char key) {
        Iterator<KeyBinding> it = binds.keySet().iterator();
        while (it.hasNext()) {
            KeyBinding bind = it.next();
            if (!bind.getTranslationKey().equals("customkey."+key)) continue;
            it.remove();
            return true;
        }
        return false;
    }
}