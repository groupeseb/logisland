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
package com.hurence.logisland.util.runner;

import com.hurence.logisland.component.*;
import com.hurence.logisland.controller.ControllerService;
import com.hurence.logisland.controller.ControllerServiceLookup;
import com.hurence.logisland.processor.ProcessContext;
import com.hurence.logisland.processor.Processor;
import com.hurence.logisland.processor.StandardValidationContext;
import com.hurence.logisland.registry.VariableRegistry;
import com.hurence.logisland.validator.ValidationResult;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class MockProcessContext extends MockControllerServiceLookup implements ControllerServiceLookup, ProcessContext {

    private final ConfigurableComponent component;
    private final Map<PropertyDescriptor, String> properties = new HashMap<>();
    private final VariableRegistry variableRegistry;


    /**
     * Creates a new MockProcessContext for the given Processor
     *
     * @param component being mocked
     * @param variableRegistry variableRegistry
     */
    public MockProcessContext(final ConfigurableComponent component, final VariableRegistry variableRegistry) {
        this.component = Objects.requireNonNull(component);
        this.variableRegistry = variableRegistry;
    }

    /**
     * Creates a new MockProcessContext for the given Processor
     *
     * @param component being mocked
     */
    public MockProcessContext(final Processor component) {
        this(component, VariableRegistry.EMPTY_REGISTRY);
    }

    public MockProcessContext(final ControllerService component, final MockProcessContext context, final VariableRegistry variableRegistry) {
        this(component, variableRegistry);

        try {
            final Map<PropertyDescriptor, String> props = context.getControllerServiceProperties(component);
            properties.putAll(props);

            super.addControllerServices(context);
        } catch (IllegalArgumentException e) {
            // do nothing...the service is being loaded
        }
    }


    Map<PropertyDescriptor, String> getControllerServiceProperties(final ControllerService controllerService) {
        return super.getConfiguration(controllerService.getIdentifier()).getProperties();
    }

    /**
     * Creates a new MockProcessContext for the given ProcessContext
     *
     * @param context
     */
    public MockProcessContext(final ProcessContext context) {
        this(context.getProcessor(), VariableRegistry.EMPTY_REGISTRY);
    }

    @Override
    public PropertyValue getPropertyValue(final PropertyDescriptor descriptor) {
        return getPropertyValue(descriptor.getName());
    }

    @Override
    public PropertyValue getPropertyValue(final String propertyName) {
        final PropertyDescriptor descriptor = component.getPropertyDescriptor(propertyName);
        if (descriptor == null) {
            return null;
        }

        final String setPropertyValue = properties.get(descriptor);
        final String propValue = (setPropertyValue == null) ? descriptor.getDefaultValue() : setPropertyValue;

        return new MockPropertyValue(propValue, this, variableRegistry, descriptor);
    }

    @Override
    public PropertyValue newPropertyValue(final String rawValue) {
        return new StandardPropertyValue(rawValue);
    }


    @Override
    public ValidationResult setProperty(final String propertyName, final String propertyValue) {
        return setProperty(new PropertyDescriptor.Builder().name(propertyName).build(), propertyValue);
    }

    @Override
    public boolean removeProperty(String name) {
        return false;
    }

    /**
     * Updates the value of the property with the given PropertyDescriptor to
     * the specified value IF and ONLY IF the value is valid according to the
     * descriptor's validator. Otherwise, the property value is not updated. In
     * either case, the ValidationResult is returned, indicating whether or not
     * the property is valid
     *
     * @param descriptor of property to modify
     * @param value      new value
     * @return result
     */
    public ValidationResult setProperty(final PropertyDescriptor descriptor, final String value) {
        requireNonNull(descriptor);
        requireNonNull(value, "Cannot set property to null value; if the intent is to remove the property, call removeProperty instead");
        final PropertyDescriptor fullyPopulatedDescriptor = component.getPropertyDescriptor(descriptor.getName());

        final ValidationResult result = fullyPopulatedDescriptor.validate(value);
        String oldValue = properties.put(fullyPopulatedDescriptor, value);
        if (oldValue == null) {
            oldValue = fullyPopulatedDescriptor.getDefaultValue();
        }
        if ((value == null && oldValue != null) || (value != null && !value.equals(oldValue))) {
            component.onPropertyModified(fullyPopulatedDescriptor, oldValue, value);
        }

        return result;
    }

    public boolean removeProperty(final PropertyDescriptor descriptor) {
        Objects.requireNonNull(descriptor);
        final PropertyDescriptor fullyPopulatedDescriptor = component.getPropertyDescriptor(descriptor.getName());
        String value = null;

        if ((value = properties.remove(fullyPopulatedDescriptor)) != null) {
            if (!value.equals(fullyPopulatedDescriptor.getDefaultValue())) {
                component.onPropertyModified(fullyPopulatedDescriptor, value, null);
            }

            return true;
        }
        return false;
    }


    @Override
    public Map<PropertyDescriptor, String> getProperties() {
        final List<PropertyDescriptor> supported = component.getPropertyDescriptors();
        if (supported == null || supported.isEmpty()) {
            return Collections.unmodifiableMap(properties);
        } else {
            final Map<PropertyDescriptor, String> props = new LinkedHashMap<>();
            for (final PropertyDescriptor descriptor : supported) {
                props.put(descriptor, null);
            }
            props.putAll(properties);
            return props;
        }
    }

    @Override
    public String getProperty(PropertyDescriptor property) {
        return properties.get(property);
    }

    /**
     * Validates the current properties, returning ValidationResults for any
     * invalid properties. All processor defined properties will be validated.
     * If they are not included in the in the purposed configuration, the
     * default value will be used.
     *
     * @return Collection of validation result objects for any invalid findings
     * only. If the collection is empty then the processor is valid. Guaranteed
     * non-null
     */
    public Collection<ValidationResult> validate() {
        return component.validate(new StandardValidationContext(properties));
    }

    public boolean isValid() {
        for (final ValidationResult result : validate()) {
            if (!result.isValid()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Collection<ValidationResult> getValidationErrors() {
        return validate();
    }


    @Override
    public String getIdentifier() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public void addControllerServiceLookup(ControllerServiceLookup controllerServiceLookup) throws InitializationException {

    }

    @Override
    public Processor getProcessor() {
        return (Processor) component;
    }


    public void addControllerService(final String serviceIdentifier, final ControllerService controllerService, final Map<PropertyDescriptor, String> properties, final String annotationData) {
        requireNonNull(controllerService);
        final ControllerServiceConfiguration config = addControllerService(controllerService, serviceIdentifier);
        config.setProperties(properties);
        config.setAnnotationData(annotationData);
    }


}
