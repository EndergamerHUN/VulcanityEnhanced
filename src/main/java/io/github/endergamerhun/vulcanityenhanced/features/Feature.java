package io.github.endergamerhun.vulcanityenhanced.features;

public abstract class Feature {
    private boolean ENABLED = true;
    public void setState(boolean state) {
        ENABLED = state;
    }
    public boolean enabled() {
        return ENABLED;
    }
    public abstract void reload();
}
