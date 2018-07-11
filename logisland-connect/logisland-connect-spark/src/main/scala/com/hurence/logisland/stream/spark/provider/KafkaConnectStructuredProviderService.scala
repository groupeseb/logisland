/*
 *  * Copyright (C) 2018 Hurence (support@hurence.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.hurence.logisland.stream.spark.provider

import java.util
import java.util.Collections

import com.hurence.logisland.annotation.lifecycle.OnEnabled
import com.hurence.logisland.component.{InitializationException, PropertyDescriptor}
import com.hurence.logisland.controller.{AbstractControllerService, ControllerServiceInitializationContext}
import com.hurence.logisland.record.{FieldDictionary, FieldType, Record, StandardRecord}
import com.hurence.logisland.stream.StreamContext
import com.hurence.logisland.stream.spark.StreamOptions
import com.hurence.logisland.stream.spark.structured.provider.StructuredStreamProviderService
import org.apache.spark.sql.{Dataset, SparkSession}

class KafkaConnectStructuredProviderService extends AbstractControllerService with StructuredStreamProviderService {

    var connectorProperties = ""
    var keyConverter = ""
    var valueConverter = ""
    var keyConverterProperties = ""
    var valueConverterProperties = ""
    var maxConfigurations = 1
    var delegateConnectorClass = ""
    var offsetBackingStore = ""
    var offsetBackingStoreProperties = ""


    @OnEnabled
    @throws[InitializationException]
    override def init(context: ControllerServiceInitializationContext): Unit = {
        this.synchronized {
            try {
                delegateConnectorClass = context.getPropertyValue(StreamOptions.KAFKA_CONNECT_CONNECTOR_CLASS).asString()
                connectorProperties = context.getPropertyValue(StreamOptions.KAFKA_CONNECT_CONNECTOR_PROPERTIES).asString()
                valueConverter = context.getPropertyValue(StreamOptions.KAFKA_CONNECT_VALUE_CONVERTER).asString()
                valueConverterProperties = context.getPropertyValue(StreamOptions.KAFKA_CONNECT_VALUE_CONVERTER_PROPERTIES).asString()
                keyConverter = context.getPropertyValue(StreamOptions.KAFKA_CONNECT_KEY_CONVERTER).asString()
                keyConverterProperties = context.getPropertyValue(StreamOptions.KAFKA_CONNECT_KEY_CONVERTER_PROPERTIES).asString()
                maxConfigurations = (context getPropertyValue StreamOptions.KAFKA_CONNECT_MAX_TASKS).asInteger()
                offsetBackingStore = (context getPropertyValue StreamOptions.KAFKA_CONNECT_OFFSET_BACKING_STORE).asString()
                offsetBackingStoreProperties = context.getPropertyValue(StreamOptions.KAFKA_CONNECT_OFFSET_BACKING_STORE_PROPERTIES).asString()
            } catch {
                case e: Exception =>
                    throw new InitializationException(e)
            }
        }
    }


    /**
      * Allows subclasses to register which property descriptor objects are
      * supported.
      *
      * @return PropertyDescriptor objects this processor currently supports
      */
    override def getSupportedPropertyDescriptors() = {
        val descriptors: util.List[PropertyDescriptor] = new util.ArrayList[PropertyDescriptor]
        descriptors.add(StreamOptions.KAFKA_CONNECT_CONNECTOR_CLASS)
        descriptors.add(StreamOptions.KAFKA_CONNECT_CONNECTOR_PROPERTIES)
        descriptors.add(StreamOptions.KAFKA_CONNECT_KEY_CONVERTER)
        descriptors.add(StreamOptions.KAFKA_CONNECT_KEY_CONVERTER_PROPERTIES)
        descriptors.add(StreamOptions.KAFKA_CONNECT_VALUE_CONVERTER)
        descriptors.add(StreamOptions.KAFKA_CONNECT_VALUE_CONVERTER_PROPERTIES)
        descriptors.add(StreamOptions.KAFKA_CONNECT_MAX_TASKS)
        descriptors.add(StreamOptions.KAFKA_CONNECT_OFFSET_BACKING_STORE)
        descriptors.add(StreamOptions.KAFKA_CONNECT_OFFSET_BACKING_STORE_PROPERTIES)
        Collections.unmodifiableList(descriptors)
    }



    /**
      * create a streaming DataFrame that represents data received
      *
      * @param spark
      * @param streamContext
      * @return DataFrame currently loaded
      */
    override def read(spark: SparkSession, streamContext: StreamContext) = {

        import spark.implicits._
        implicit val myObjEncoder = org.apache.spark.sql.Encoders.kryo[Record]

        getLogger.info(s"Connecting kafka-connect source $delegateConnectorClass")
        spark.readStream
            .format("com.hurence.logisland.connect.source.KafkaConnectStreamSourceProvider")
            .option(StreamOptions.KAFKA_CONNECT_CONNECTOR_PROPERTIES.getName, connectorProperties)
            .option(StreamOptions.KAFKA_CONNECT_KEY_CONVERTER.getName, keyConverter)
            .option(StreamOptions.KAFKA_CONNECT_KEY_CONVERTER_PROPERTIES.getName, keyConverterProperties)
            .option(StreamOptions.KAFKA_CONNECT_VALUE_CONVERTER.getName, valueConverter)
            .option(StreamOptions.KAFKA_CONNECT_VALUE_CONVERTER_PROPERTIES.getName, valueConverterProperties)
            .option(StreamOptions.KAFKA_CONNECT_MAX_TASKS.getName, maxConfigurations)
            .option(StreamOptions.KAFKA_CONNECT_CONNECTOR_CLASS.getName, delegateConnectorClass)
            .option(StreamOptions.KAFKA_CONNECT_OFFSET_BACKING_STORE.getName, offsetBackingStore)
            .option(StreamOptions.KAFKA_CONNECT_OFFSET_BACKING_STORE_PROPERTIES.getName, offsetBackingStoreProperties)


            .load()
            //Topic, Partition, Key, Value
            .as[(String, Int, Array[Byte], Array[Byte])]
            .map(r =>
                new StandardRecord("kafka_connect")
                    .setField(FieldDictionary.RECORD_KEY, FieldType.BYTES, r._3)
                    .setField(FieldDictionary.RECORD_VALUE, FieldType.BYTES, r._4))
    }


    /**
      * create a streaming DataFrame that represents data to be written
      *
      * @param streamContext
      * @return DataFrame currently loaded
      */
    override def write(df: Dataset[Record], streamContext: StreamContext) = {
        //TODO: Add sink support
        df
    }

}
