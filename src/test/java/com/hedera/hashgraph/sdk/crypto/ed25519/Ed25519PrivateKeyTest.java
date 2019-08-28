package com.hedera.hashgraph.sdk.crypto.ed25519;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class Ed25519PrivateKeyTest {

    private static final String testKeyStr = "302e020100300506032b657004220420db484b828e64b2d8f12ce3c0a0e93a0b8cce7af1bb8f39c97732394482538e10";
    private static final String testKeyPem = "-----BEGIN PRIVATE KEY-----\n"
        + "MC4CAQAwBQYDK2VwBCIEINtIS4KOZLLY8SzjwKDpOguMznrxu485yXcyOUSCU44Q\n"
        + "-----END PRIVATE KEY-----\n";

    @Test
    @DisplayName("private key generates successfully")
    void keyGenerates() {
        final Ed25519PrivateKey key = Ed25519PrivateKey.generate();

        assertNotNull(key);
        assertNotNull(key.toBytes());
    }

    @Test
    @DisplayName("private key can be recovered from bytes")
    void keySerialization() {
        final Ed25519PrivateKey key1 = Ed25519PrivateKey.generate();
        final byte[] key1Bytes = key1.toBytes();
        final Ed25519PrivateKey key2 = Ed25519PrivateKey.fromBytes(key1Bytes);
        final byte[] key2Bytes = key2.toBytes();

        assertArrayEquals(key1Bytes, key2Bytes);
    }

    @ParameterizedTest
    @DisplayName("private key can be recovered from external string")
    @ValueSource(strings = {
        "302e020100300506032b657004220420db484b828e64b2d8f12ce3c0a0e93a0b8cce7af1bb8f39c97732394482538e10",
        // raw hex (concatenated private + public key)
        "db484b828e64b2d8f12ce3c0a0e93a0b8cce7af1bb8f39c97732394482538e10" +
            "e0c8ec2758a5879ffac226a13c0c516b799e72e35141a0dd828f94d37988a4b7",
        // raw hex (just private key)
        "db484b828e64b2d8f12ce3c0a0e93a0b8cce7af1bb8f39c97732394482538e10"
    })
    void externalKeyDeserialize(String keyStr) {
        final Ed25519PrivateKey key = Ed25519PrivateKey.fromString(keyStr);
        assertNotNull(key);
        // the above are all the same key
        assertEquals(
            testKeyStr,
            key.toString()
        );
    }

    @Test
    @DisplayName("private key can be encoded to a string")
    void keyToString() {
        final Ed25519PrivateKey key = Ed25519PrivateKey.fromString(testKeyStr);

        assertNotNull(key);
        assertEquals(testKeyStr, key.toString());
    }

    @Test
    @DisplayName("private key can be decoded from a PEM file")
    void keyFromPem() throws IOException {
        final StringReader stringReader = new StringReader(testKeyPem);
        final Ed25519PrivateKey privateKey = Ed25519PrivateKey.fromPemFile(stringReader);

        assertEquals(privateKey.toString(), testKeyStr);
    }

    @Test
    @DisplayName("private key can be decoded from a PEM file")
    void keyToPem() throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final Ed25519PrivateKey privateKey = Ed25519PrivateKey.fromString(testKeyStr);
        privateKey.writePem(stringWriter);

        assertEquals(stringWriter.toString(), testKeyPem);
    }
}