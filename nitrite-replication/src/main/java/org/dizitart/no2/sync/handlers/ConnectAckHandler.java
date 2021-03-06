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

package org.dizitart.no2.sync.handlers;

import lombok.extern.slf4j.Slf4j;
import org.dizitart.no2.sync.ReplicationTemplate;
import org.dizitart.no2.sync.message.ConnectAck;

/**
 * @author Anindya Chatterjee
 */
@Slf4j
public class ConnectAckHandler implements MessageHandler<ConnectAck> {
    private ReplicationTemplate replica;

    public ConnectAckHandler(ReplicationTemplate replica) {
        this.replica = replica;
    }

    @Override
    public void handleMessage(ConnectAck message) {
        replica.collectGarbage(message.getTombstoneTtl());
        replica.setConnected();
        replica.startFeedExchange();
        replica.sendChanges();
    }
}
