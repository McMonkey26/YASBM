package com.pew.yetanotherskyblockmod.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface ScreenCloseEvent {
    Event<ScreenCloseEvent> EVENT = EventFactory.createArrayBacked(ScreenCloseEvent.class,
        (listeners) -> () -> {
            for (ScreenCloseEvent listener : listeners) {
                listener.on();
            }
        }
    );

    void on();
}
