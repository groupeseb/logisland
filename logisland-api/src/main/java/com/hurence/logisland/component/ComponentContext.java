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
package com.hurence.logisland.component;


import com.hurence.logisland.validator.ValidationResult;

import java.util.Map;

/**
 * <p>
 * Provides a bridge between a Processor and the Framework
 * </p>
 *
 * <p>
 * <b>Note: </b>Implementations of this interface are NOT necessarily
 * thread-safe.
 * </p>
 */
public interface ComponentContext extends ConfiguredComponent {

    /**
     * Retrieves the current value set for the given descriptor, if a value is
     * set - else uses the descriptor to determine the appropriate default value
     *
     * @param descriptor to lookup the value of
     * @return the property value of the given descriptor
     */
    PropertyValue getPropertyValue(PropertyDescriptor descriptor);

    /**
     * Retrieves the current value set for the given descriptor, if a value is
     * set - else uses the descriptor to determine the appropriate default value
     *
     * @param propertyName of the property to lookup the value for
     * @return property value as retrieved by property name
     */
    PropertyValue getPropertyValue(String propertyName);

    /**
     * Sets the property with the given name to the given value
     *
     * @param name the name of the property to update
     * @param value the value to update the property to
     */
    ValidationResult setProperty(String name, String value);

    /**
     * Creates and returns a {@link PropertyValue} object that can be used for
     * evaluating the value of the given String
     *
     * @param rawValue the raw input before any property evaluation has occurred
     * @return a {@link PropertyValue} object that can be used for
     * evaluating the value of the given String
     */
    PropertyValue newPropertyValue(String rawValue);


    /**
     * @return a Map of all PropertyDescriptors to their configured getAllFields. This
     * Map may or may not be modifiable, but modifying its getAllFields will not
     * change the getAllFields of the processor's properties
     */
    Map<PropertyDescriptor, String> getProperties();


    /**
     * @return the configured name of this processor
     */
    String getName();


}
