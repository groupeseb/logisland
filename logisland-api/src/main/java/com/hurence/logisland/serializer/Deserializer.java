/**
 * Copyright (C) 2016 Hurence (support@hurence.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hurence.logisland.serializer;


import java.io.IOException;
import java.io.InputStream;

/**
 * Provides an interface for deserializing an array of bytes into an Object
 *
 * @param <T> type
 */
public interface Deserializer<T> {

    /**
     * Deserializes the given byte array input an Object and returns that value.
     *
     * @param objectDataInput input
     * @return returns deserialized value
     * @throws DeserializationException if a valid object cannot be deserialized
     * from the given byte array
     * @throws IOException ex
     */
    T deserialize(InputStream objectDataInput) throws DeserializationException, IOException;

}
