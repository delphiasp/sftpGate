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

package org.apache.sshd.common.util.security;

import java.lang.reflect.Method;
import java.security.GeneralSecurityException;
import java.security.Provider;
import java.util.Objects;

import org.apache.sshd.common.util.GenericUtils;
import org.apache.sshd.common.util.ValidateUtils;

/**
 * @param <T> Type of security entity being generated by this factory
 * @author <a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>
 */
public interface SecurityEntityFactory<T> {
    Class<T> getEntityType();

    T getInstance(String algorithm) throws GeneralSecurityException;

    /**
     * Uses reflection in order to wrap the {@code getInstance} method(s)
     * as a security entity factory.
     *
     * @param <F> Type of entity being generated by the factor
     * @param entityType The entity type class
     * @param registrar The {@code SecurityProviderRegistrar} to use - if
     * {@code null} then default provider is used (if specified).
     * @param defaultProvider Default provider choice to use if no registrar
     * provided. If {@code null}/empty then JCE default is used
     * @return The {@link SecurityEntityFactory} for the entity
     * @throws ReflectiveOperationException If failed to create the factory
     * @see #toDefaultFactory(Class)
     * @see #toNamedProviderFactory(Class, String)
     * @see #toProviderInstanceFactory(Class, Provider)
     * @see SecurityProviderChoice#isNamedProviderUsed()
     * @see SecurityProviderChoice#getSecurityProvider()
     */
    static <F> SecurityEntityFactory<F> toFactory(
            Class<F> entityType, SecurityProviderChoice registrar, SecurityProviderChoice defaultProvider)
            throws ReflectiveOperationException {
        if (registrar == null) {
            if ((defaultProvider == null) || (defaultProvider == SecurityProviderChoice.EMPTY)) {
                return toDefaultFactory(entityType);
            } else if (defaultProvider.isNamedProviderUsed()) {
                return toNamedProviderFactory(entityType, defaultProvider.getName());
            } else {
                return toProviderInstanceFactory(entityType, defaultProvider.getSecurityProvider());
            }
        } else if (registrar.isNamedProviderUsed()) {
            return toNamedProviderFactory(entityType, registrar.getName());
        } else {
            return toProviderInstanceFactory(entityType, registrar.getSecurityProvider());
        }
    }

    static <F> SecurityEntityFactory<F> toDefaultFactory(Class<F> entityType)
            throws ReflectiveOperationException {
        Method m = entityType.getDeclaredMethod("getInstance", String.class);
        return new SecurityEntityFactory<F>() {
            private final String s = SecurityEntityFactory.class.getSimpleName()
                    + "[" + entityType.getSimpleName() + "]"
                    + "[default]";

            @Override
            public Class<F> getEntityType() {
                return entityType;
            }

            @Override
            public F getInstance(String algorithm) throws GeneralSecurityException {
                try {
                    Object value = m.invoke(null, algorithm);
                    return entityType.cast(value);
                } catch (ReflectiveOperationException t) {
                    Throwable e = GenericUtils.peelException(t);
                    if (e instanceof GeneralSecurityException) {
                        throw (GeneralSecurityException) e;
                    } else if (e instanceof RuntimeException) {
                        throw (RuntimeException) e;
                    } else if (e instanceof Error) {
                        throw (Error) e;
                    } else {
                        throw new GeneralSecurityException(e);
                    }
                }
            }

            @Override
            public String toString() {
                return s;
            }
        };
    }

    static <F> SecurityEntityFactory<F> toNamedProviderFactory(Class<F> entityType, String name)
            throws ReflectiveOperationException {
        ValidateUtils.checkNotNullAndNotEmpty(name, "No provider name specified");
        Method m = entityType.getDeclaredMethod("getInstance", String.class, String.class);
        return new SecurityEntityFactory<F>() {
            private final String s = SecurityEntityFactory.class.getSimpleName()
                    + "[" + entityType.getSimpleName() + "]"
                    + "[" + name + "]";

            @Override
            public Class<F> getEntityType() {
                return entityType;
            }

            @Override
            public F getInstance(String algorithm) throws GeneralSecurityException {
                try {
                    Object value = m.invoke(null, algorithm, name);
                    return entityType.cast(value);
                } catch (ReflectiveOperationException t) {
                    Throwable e = GenericUtils.peelException(t);
                    if (e instanceof GeneralSecurityException) {
                        throw (GeneralSecurityException) e;
                    } else if (e instanceof RuntimeException) {
                        throw (RuntimeException) e;
                    } else if (e instanceof Error) {
                        throw (Error) e;
                    } else {
                        throw new GeneralSecurityException(e);
                    }
                }
            }

            @Override
            public String toString() {
                return s;
            }
        };
    }

    static <F> SecurityEntityFactory<F> toProviderInstanceFactory(Class<F> entityType, Provider provider)
            throws ReflectiveOperationException {
        Objects.requireNonNull(provider, "No provider instance");
        Method m = entityType.getDeclaredMethod("getInstance", String.class, Provider.class);
        return new SecurityEntityFactory<F>() {
            private final String s = SecurityEntityFactory.class.getSimpleName()
                    + "[" + entityType.getSimpleName() + "]"
                    + "[" + Provider.class.getSimpleName() + "]"
                    + "[" + provider.getName() + "]";

            @Override
            public Class<F> getEntityType() {
                return entityType;
            }

            @Override
            public F getInstance(String algorithm) throws GeneralSecurityException {
                try {
                    Object value = m.invoke(null, algorithm, provider);
                    return entityType.cast(value);
                } catch (ReflectiveOperationException t) {
                    Throwable e = GenericUtils.peelException(t);
                    if (e instanceof GeneralSecurityException) {
                        throw (GeneralSecurityException) e;
                    } else if (e instanceof RuntimeException) {
                        throw (RuntimeException) e;
                    } else if (e instanceof Error) {
                        throw (Error) e;
                    } else {
                        throw new GeneralSecurityException(e);
                    }
                }
            }

            @Override
            public String toString() {
                return s;
            }
        };
    }
}
