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

package org.dizitart.no2.collection.operation;

import org.dizitart.no2.collection.Document;
import org.dizitart.no2.collection.DocumentCursor;
import org.dizitart.no2.collection.NitriteId;
import org.dizitart.no2.common.KeyValuePair;
import org.dizitart.no2.common.Lookup;
import org.dizitart.no2.common.RecordStream;
import org.dizitart.no2.exceptions.InvalidOperationException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.dizitart.no2.common.util.ObjectUtils.deepEquals;

/**
 * @author Anindya Chatterjee.
 */
class JoinedDocumentStream implements RecordStream<Document> {
    private final RecordStream<KeyValuePair<NitriteId, Document>> recordStream;
    private final DocumentCursor foreignCursor;
    private final Lookup lookup;

    JoinedDocumentStream(RecordStream<KeyValuePair<NitriteId, Document>> recordStream,
                         DocumentCursor foreignCursor,
                         Lookup lookup) {
        this.recordStream = recordStream;
        this.foreignCursor = foreignCursor;
        this.lookup = lookup;
    }


    @Override
    public Iterator<Document> iterator() {
        Iterator<KeyValuePair<NitriteId, Document>> iterator = recordStream == null ? Collections.emptyIterator()
            : recordStream.iterator();
        return new JoinedDocumentIterator(iterator);
    }

    @Override
    public String toString() {
        return toList().toString();
    }

    private class JoinedDocumentIterator implements Iterator<Document> {
        private final Iterator<KeyValuePair<NitriteId, Document>> iterator;

        JoinedDocumentIterator(Iterator<KeyValuePair<NitriteId, Document>> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Document next() {
            KeyValuePair<NitriteId, Document> next = iterator.next();
            Document document = next.getValue();
            if (document != null) {
                return join(document.clone(), foreignCursor, lookup);
            }
            return null;
        }

        @Override
        public void remove() {
            throw new InvalidOperationException("remove on a cursor is not supported");
        }

        private Document join(Document localDocument, DocumentCursor foreignCursor, Lookup lookup) {
            Object localObject = localDocument.get(lookup.getLocalField());
            if (localObject == null) return localDocument;
            Set<Document> target = new HashSet<>();

            for (Document foreignDocument : foreignCursor) {
                Object foreignObject = foreignDocument.get(lookup.getForeignField());
                if (foreignObject != null) {
                    if (deepEquals(foreignObject, localObject)) {
                        target.add(foreignDocument);
                    }
                }
            }
            if (!target.isEmpty()) {
                localDocument.put(lookup.getTargetField(), target);
            }
            return localDocument;
        }
    }
}

