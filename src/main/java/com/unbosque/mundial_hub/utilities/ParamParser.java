package com.unbosque.mundial_hub.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public final class ParamParser {
    public static UUID getUuidFromString(String raw) {
        return UUID.fromString(raw);
    }
}