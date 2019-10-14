/*
 * Copyright 2017 S. Koulouzis.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.uva.sne.drip.rpc;

import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;

import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author S. Koulouzis.
 */
@Component
public class PlannerCaller extends DRIPCaller {

    private static final String REQUEST_QUEUE_NAME = "planner_queue";

    @Autowired
    ConnectionFactory factory;

    @Autowired
    public PlannerCaller(ConnectionFactory factory) throws IOException, TimeoutException {
        super(REQUEST_QUEUE_NAME, factory);
    }

}
