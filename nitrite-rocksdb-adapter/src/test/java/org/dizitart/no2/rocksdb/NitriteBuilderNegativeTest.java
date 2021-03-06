/*
 * Copyright (c) 2017-2020. Nitrite author or authors.
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

import org.dizitart.no2.exceptions.NitriteIOException;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;

import static org.dizitart.no2.rocksdb.DbTestOperations.getRandomTempDbFile;
import static org.dizitart.no2.rocksdb.TestUtil.createDb;


/**
 * @author Anindya Chatterjee.
 */
public class NitriteBuilderNegativeTest {
    private final String filePath = getRandomTempDbFile();

    @Test(expected = NitriteIOException.class)
    public void testOpenWithLock() {
        TestUtil.createDb(filePath);
        TestUtil.createDb(filePath);
    }

    @Test(expected = NitriteIOException.class)
    public void testInvalidDirectory() {
        String filePath = "/ytgr/hfurh/frij.db";
        TestUtil.createDb(filePath);
    }

    @After
    public void cleanUp() throws IOException {
        TestUtil.deleteFile(filePath);
    }
}
