/*
 * Copyright (c) 2019-2020. Nitrite author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dizitart.no2.rocksdb;

import org.nustaq.serialization.FSTConfiguration;

/**
 * @author Anindya Chatterjee
 */
class FSTMarshaller implements Marshaller {
    private final FSTConfiguration configuration;

    public FSTMarshaller() {
        configuration = FSTConfiguration.createDefaultConfiguration();
    }

    @Override
    public <T> byte[] marshal(T object) {
        return configuration.asByteArray(object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unmarshal(byte[] bytes, Class<T> type) {
        return (T) configuration.asObject(bytes);
    }
}
