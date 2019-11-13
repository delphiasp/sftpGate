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

package org.apache.sshd.client.session.forward;

import java.io.IOException;

import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.forward.PortForwardingManager;
import org.apache.sshd.common.util.net.SshdSocketAddress;

/**
 * @author <a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>
 */
public class DynamicPortForwardingTracker extends PortForwardingTracker {
    public DynamicPortForwardingTracker(ClientSession session, SshdSocketAddress localAddress, SshdSocketAddress boundAddress) {
        super(session, localAddress, boundAddress);
    }

    @Override
    public void close() throws IOException {
        if (open.getAndSet(false)) {
            PortForwardingManager manager = getClientSession();
            manager.stopDynamicPortForwarding(getBoundAddress());
        }
    }
}
