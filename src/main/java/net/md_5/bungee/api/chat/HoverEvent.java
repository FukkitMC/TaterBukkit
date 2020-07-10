package net.md_5.bungee.api.chat;

import lombok.EqualsAndHashCode;
import lombok.ToString;

public final class HoverEvent {

    private Action action;
    private BaseComponent[] value;

    public HoverEvent(Action action, BaseComponent[] value) {
        this.action = action;
        this.value = value;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public BaseComponent[] getValue() {
        return value;
    }

    public void setValue(BaseComponent[] value) {
        this.value = value;
    }

    public enum Action {

        SHOW_TEXT,
        SHOW_ACHIEVEMENT,
        SHOW_ITEM,
        SHOW_ENTITY
    }
}
