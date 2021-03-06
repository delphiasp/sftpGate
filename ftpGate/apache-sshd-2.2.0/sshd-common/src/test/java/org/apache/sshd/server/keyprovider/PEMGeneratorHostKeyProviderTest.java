/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sshd.server.keyprovider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;

import org.apache.sshd.common.cipher.ECCurves;
import org.apache.sshd.common.config.keys.KeyUtils;
import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.apache.sshd.common.util.io.IoUtils;
import org.apache.sshd.common.util.security.SecurityUtils;
import org.apache.sshd.util.test.JUnitTestSupport;
import org.apache.sshd.util.test.NoIoTestCase;
import org.junit.Assume;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;

/**
 * @author <a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Category({ NoIoTestCase.class })
public class PEMGeneratorHostKeyProviderTest extends JUnitTestSupport {
    public PEMGeneratorHostKeyProviderTest() {
        super();
    }

    @Test
    public void testDSA() throws IOException, GeneralSecurityException {
        Assume.assumeTrue("BouncyCastle not registered", SecurityUtils.isBouncyCastleRegistered());
        testPEMGeneratorHostKeyProvider(KeyUtils.DSS_ALGORITHM, KeyPairProvider.SSH_DSS, 512, null);
    }

    @Test
    public void testRSA() throws IOException, GeneralSecurityException {
        Assume.assumeTrue("BouncyCastle not registered", SecurityUtils.isBouncyCastleRegistered());
        testPEMGeneratorHostKeyProvider(KeyUtils.RSA_ALGORITHM, KeyPairProvider.SSH_RSA, 512, null);
    }

    @Test
    public void testECnistp256() throws IOException, GeneralSecurityException {
        Assume.assumeTrue("BouncyCastle not registered", SecurityUtils.isBouncyCastleRegistered());
        Assume.assumeTrue("ECC not supported", SecurityUtils.isECCSupported());
        Assume.assumeTrue(ECCurves.nistp256 + " N/A", ECCurves.nistp256.isSupported());
        testPEMGeneratorHostKeyProvider(KeyUtils.EC_ALGORITHM, KeyPairProvider.ECDSA_SHA2_NISTP256, -1, new ECGenParameterSpec("prime256v1"));
    }

    @Test
    public void testECnistp384() throws IOException, GeneralSecurityException {
        Assume.assumeTrue("BouncyCastle not registered", SecurityUtils.isBouncyCastleRegistered());
        Assume.assumeTrue("ECC not supported", SecurityUtils.isECCSupported());
        Assume.assumeTrue(ECCurves.nistp384 + " N/A", ECCurves.nistp384.isSupported());
        testPEMGeneratorHostKeyProvider(KeyUtils.EC_ALGORITHM, KeyPairProvider.ECDSA_SHA2_NISTP384, -1, new ECGenParameterSpec("P-384"));
    }

    @Test
    public void testECnistp521() throws IOException, GeneralSecurityException {
        Assume.assumeTrue("BouncyCastle not registered", SecurityUtils.isBouncyCastleRegistered());
        Assume.assumeTrue("ECC not supported", SecurityUtils.isECCSupported());
        Assume.assumeTrue(ECCurves.nistp521 + " N/A", ECCurves.nistp521.isSupported());
        testPEMGeneratorHostKeyProvider(KeyUtils.EC_ALGORITHM, KeyPairProvider.ECDSA_SHA2_NISTP521, -1, new ECGenParameterSpec("P-521"));
    }

    private Path testPEMGeneratorHostKeyProvider(
            String algorithm, String keyType, int keySize, AlgorithmParameterSpec keySpec)
                throws IOException, GeneralSecurityException {
        Path path = initKeyFileLocation(algorithm);
        KeyPair kpWrite = invokePEMGeneratorHostKeyProvider(path, algorithm, keyType, keySize, keySpec);
        assertTrue("Key file not generated: " + path, Files.exists(path, IoUtils.EMPTY_LINK_OPTIONS));

        KeyPair kpRead = invokePEMGeneratorHostKeyProvider(path, algorithm, keyType, keySize, keySpec);
        PublicKey pubWrite = kpWrite.getPublic();
        PublicKey pubRead = kpRead.getPublic();
        if (pubWrite instanceof ECPublicKey) {
            // The algorithm is reported as ECDSA instead of EC
            assertECPublicKeyEquals("Mismatched EC public key", ECPublicKey.class.cast(pubWrite), ECPublicKey.class.cast(pubRead));
        } else {
            assertKeyEquals("Mismatched public keys", pubWrite, pubRead);
        }
        return path;
    }

    private static KeyPair invokePEMGeneratorHostKeyProvider(
            Path path, String algorithm, String keyType, int keySize, AlgorithmParameterSpec keySpec)
                throws IOException, GeneralSecurityException {
        AbstractGeneratorHostKeyProvider provider = SecurityUtils.createGeneratorHostKeyProvider(path.toAbsolutePath().normalize());
        provider.setAlgorithm(algorithm);
        provider.setOverwriteAllowed(true);
        if (keySize > 0) {
            provider.setKeySize(keySize);
        }
        if (keySpec != null) {
            provider.setKeySpec(keySpec);
        }

        return validateKeyPairProvider(provider, keyType);
    }

    private static KeyPair validateKeyPairProvider(KeyPairProvider provider, String keyType)
            throws IOException, GeneralSecurityException {
        Iterable<String> types = provider.getKeyTypes(null);
        KeyPair kp = null;
        for (String type : types) {
            if (keyType.equals(type)) {
                kp = provider.loadKey(null, keyType);
                assertNotNull("Failed to load key for " + keyType, kp);
                break;
            }
        }

        assertNotNull("Expected key type not found: " + keyType, kp);
        return kp;
    }

    private Path initKeyFileLocation(String algorithm) throws IOException {
        Path path = assertHierarchyTargetFolderExists(getTempTargetRelativeFile(getClass().getSimpleName()));
        path = path.resolve(algorithm + "-PEM.key");
        Files.deleteIfExists(path);
        return path;
    }
}
