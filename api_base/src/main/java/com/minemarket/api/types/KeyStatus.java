package com.minemarket.api.types;

public enum KeyStatus {
    VALID, INVALID, UNKNOWN, BLOCKED, WAITING_VALIDATION;

    public static KeyStatus getByName(String name) {
        for (KeyStatus k : values())
            if (k.name().equalsIgnoreCase(name))
                return k;
        return null;
    }
}
