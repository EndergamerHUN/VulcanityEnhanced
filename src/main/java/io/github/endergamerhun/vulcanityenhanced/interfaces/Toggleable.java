package io.github.endergamerhun.vulcanityenhanced.interfaces;

public abstract class Toggleable {
    private boolean enabled = false;
    public boolean enabled() {
        return enabled;
    }

    public void setEnabled(boolean state) {
        enabled = state;
    }
}
