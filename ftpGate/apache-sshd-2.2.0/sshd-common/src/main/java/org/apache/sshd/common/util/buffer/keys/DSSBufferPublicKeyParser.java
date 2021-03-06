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

package org.apache.sshd.common.util.buffer.keys;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPublicKeySpec;

import org.apache.sshd.common.config.keys.KeyUtils;
import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.apache.sshd.common.util.ValidateUtils;
import org.apache.sshd.common.util.buffer.Buffer;

/**
 * @author <a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>
 */
public class DSSBufferPublicKeyParser extends AbstractBufferPublicKeyParser<DSAPublicKey> {
    public static final DSSBufferPublicKeyParser INSTANCE = new DSSBufferPublicKeyParser();

    public DSSBufferPublicKeyParser() {
        super(DSAPublicKey.class, KeyPairProvider.SSH_DSS);
    }

    @Override
    public DSAPublicKey getRawPublicKey(String keyType, Buffer buffer) throws GeneralSecurityException {
        ValidateUtils.checkTrue(isKeyTypeSupported(keyType), "Unsupported key type: %s", keyType);
        BigInteger p = buffer.getMPInt();
        BigInteger q = buffer.getMPInt();
        BigInteger g = buffer.getMPInt();
        BigInteger y = buffer.getMPInt();

        return generatePublicKey(KeyUtils.DSS_ALGORITHM, new DSAPublicKeySpec(y, p, q, g));
    }
}
