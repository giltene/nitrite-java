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

package org.dizitart.no2.rocksdb.rocksdb;

import org.dizitart.no2.collection.Document;
import org.dizitart.no2.common.KeyValuePair;
import org.dizitart.no2.exceptions.ValidationException;
import org.dizitart.no2.rocksdb.AbstractTest;
import org.dizitart.no2.store.NitriteMap;
import org.dizitart.no2.store.NitriteStore;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Anindya Chatterjee.
 */
public class NitriteMapStressTest extends AbstractTest {

    @Test
    public void testWithInsertReadUpdate() {
        NitriteStore<?> nitriteStore = db.getStore();
        NitriteMap<String, Document> nitriteMap = nitriteStore.openMap("testWithInsertReadUpdate");

        int count = 10000;
        for (int i = 0; i < count; i++) {
            Document record = Document.createDocument();
            record.put("firstName", UUID.randomUUID().toString());
            record.put("failed", false);
            record.put("lastName", UUID.randomUUID().toString());
            record.put("processed", false);

            nitriteMap.put(UUID.randomUUID().toString(), record);
        }

        for (KeyValuePair<String, Document> entry : nitriteMap.entries()) {
            String key = entry.getKey();
            Document record = entry.getValue();

            record.put("processed", true);

            nitriteMap.put(key, record);
        }

        assertEquals(nitriteMap.size(), 10000);
    }

    @Test
    public void testNullKey() {
        NitriteStore<?> nitriteStore = db.getStore();
        NitriteMap<String, Document> nitriteMap = nitriteStore.openMap("testNullKey");
        nitriteMap.put(null, Document.createDocument());

        assertNotNull(nitriteMap.get(null));
        assertEquals(nitriteMap.size(), 1);

        nitriteMap.put(null, Document.createDocument("first", 1));
        assertNotNull(nitriteMap.get(null));
        assertEquals(nitriteMap.size(), 1);
    }

    @Test(expected = ValidationException.class)
    public void testNullValue() {
        NitriteStore<?> nitriteStore = db.getStore();
        NitriteMap<String, Document> nitriteMap = nitriteStore.openMap("testNullValue");
        nitriteMap.put(null, null);
    }

    @Test(expected = ValidationException.class)
    public void testNullPutIfAbsent() {
        NitriteStore<?> nitriteStore = db.getStore();
        NitriteMap<String, Document> nitriteMap = nitriteStore.openMap("testNullPutIfAbsent");
        nitriteMap.putIfAbsent(null, null);
    }
}
