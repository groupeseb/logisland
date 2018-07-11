/**
 * Copyright (C) 2016 Hurence (support@hurence.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hurence.logisland.stream;

import com.hurence.logisland.component.ComponentContext;
import com.hurence.logisland.component.InitializationException;
import com.hurence.logisland.controller.ControllerServiceLookup;
import com.hurence.logisland.processor.ProcessContext;

import java.util.Collection;

public interface StreamContext extends ComponentContext {

    /**
     * @return the Stream
     */
    RecordStream getStream();


    /**
     * @return retrieve the list of process contexts
     */
    Collection<ProcessContext> getProcessContexts();

    /**
     * add a stream to the collection of ProcessContext
     *
     * @param processContext the ProcessContext to add
     */
    void addProcessContext(ProcessContext processContext);


    /**
     * Adds the given {@link ControllerServiceLookup}  so that the
     * configured Processor can access it
     *
     * @param controllerServiceLookup the service
     * @throws InitializationException ie
     */
    void addControllerServiceLookup(ControllerServiceLookup controllerServiceLookup) throws InitializationException;

    /**
     * get the given {@link ControllerServiceLookup}  so that the
     * configured Processor can access it
     *
     * @throws InitializationException ie
     */
    ControllerServiceLookup getControllerServiceLookup() throws InitializationException;

}
