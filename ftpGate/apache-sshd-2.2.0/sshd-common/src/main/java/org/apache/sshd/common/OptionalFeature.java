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

package org.apache.sshd.common;

import java.util.Collection;

import org.apache.sshd.common.util.GenericUtils;

/**
 * @author <a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>
 */
@FunctionalInterface
public interface OptionalFeature {
    OptionalFeature TRUE = new OptionalFeature() {
        @Override
        public boolean isSupported() {
            return true;
        }

        @Override
        public String toString() {
            return "TRUE";
        }
    };

    OptionalFeature FALSE = new OptionalFeature() {
        @Override
        public boolean isSupported() {
            return false;
        }

        @Override
        public String toString() {
            return "FALSE";
        }
    };

    boolean isSupported();

    static OptionalFeature of(boolean supported) {
        return supported ? TRUE : FALSE;
    }

    static OptionalFeature all(Collection<? extends OptionalFeature> features) {
        return () -> {
            if (GenericUtils.isEmpty(features)) {
                return false;
            }

            for (OptionalFeature f : features) {
                if (!f.isSupported()) {
                    return false;
                }
            }

            return true;
        };
    }

    static OptionalFeature any(Collection<? extends OptionalFeature> features) {
        return () -> {
            if (GenericUtils.isEmpty(features)) {
                return false;
            }

            for (OptionalFeature f : features) {
                if (f.isSupported()) {
                    return true;
                }
            }

            return false;
        };
    }
}
