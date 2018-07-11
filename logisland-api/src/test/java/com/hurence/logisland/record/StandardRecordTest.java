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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hurence.logisland.record;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author tom
 */
public class StandardRecordTest {


    @Test
    public void validateRecordApi() throws IOException {

        String id = "firewall_record1";
        String type = "cisco";
        Record record = new StandardRecord(type).setId(id);

        assertTrue(record.isEmpty());
        assertEquals(record.size(), 0);

        // shortcut for id
        assertEquals(record.getId(), id);
        assertEquals(record.getField(FieldDictionary.RECORD_ID).asString(), id);

        // shortcut for time
        assertEquals(record.getTime().getTime(), record.getField(FieldDictionary.RECORD_TIME).asLong().longValue());

        // shortcut for type
        assertEquals(record.getType(), type);
        assertEquals(record.getType(), record.getField(FieldDictionary.RECORD_TYPE).asString());
        assertEquals(record.getType(), record.getField(FieldDictionary.RECORD_TYPE).getRawValue());


        record.setField("timestamp", FieldType.LONG, new Date().getTime())
                .setField("method", FieldType.STRING, "GET")
                .setField("ip_source", FieldType.STRING, "123.34.45.123")
                .setField("ip_target", FieldType.STRING, "255.255.255.255")
                .setField("url_scheme", FieldType.STRING, "http")
                .setStringField("url_host", "origin-www.20minutes.fr")
                .setField("url_port", FieldType.STRING, "80")
                .setField("url_path", FieldType.STRING, "/r15lgc-100KB.js")
                .setField("request_size", FieldType.INT, 1399)
                .setField("response_size", FieldType.INT, 452)
                .setField("is_outside_office_hours", FieldType.BOOLEAN, false)
                .setField("is_host_blacklisted", FieldType.BOOLEAN, false)
                .setField("tags", FieldType.ARRAY, new ArrayList<>(Arrays.asList("spam", "filter", "mail")))
                .setField("type", FieldType.UNION, new ArrayList<>(Arrays.asList("null", "string")));

        assertEquals(record.size(), 14);  // 14 + 3

        assertFalse(record.hasField("unkown_field"));
        assertTrue(record.hasField("method"));
        assertEquals(record.getField("method").asString(), "GET");
        assertTrue(record.getField("response_size").asInteger() - 452 == 0);

        record.removeField("is_host_blacklisted");
        assertEquals(record.size(), 13);

        record.setField("is_outside_office_hours", record.getField("is_outside_office_hours").getType(), !record.getField("is_outside_office_hours").asBoolean());
        assertTrue(record.getField("is_outside_office_hours").asBoolean());
        assertTrue(record.hasField("type"));
    }


    @Test
    public void validateRecordValidity() {


        Record record = new StandardRecord();
        record.setField("request_size", FieldType.INT, 1399);
        assertTrue(record.isValid());
        record.setField("request_size", FieldType.INT, "zer");
        assertFalse(record.isValid());
        record.setField("request_size", FieldType.INT, 45L);
        assertFalse(record.isValid());
        record.setField("request_size", FieldType.LONG, 45L);
        assertTrue(record.isValid());
        record.setField("request_size", FieldType.DOUBLE, 45.5d);
        assertTrue(record.isValid());
        record.setField("request_size", FieldType.DOUBLE, 45.5);
        assertTrue(record.isValid());
        record.setField("request_size", FieldType.DOUBLE, 45L);
        assertFalse(record.isValid());
        record.setField("request_size", FieldType.FLOAT, 45.5f);
        assertTrue(record.isValid());
        record.setField("request_size", FieldType.STRING, 45L);
        assertFalse(record.isValid());
        record.setField("request_size", FieldType.FLOAT, 45.5d);
        assertFalse(record.isValid());


        record.setField("request_size", FieldType.INT, 45);
        assertTrue(45.0d - record.getField("request_size").asDouble() == 0);
    }


    @Test
    public void validateNestedRecord() {


        Record leafRecord = new StandardRecord("leaf");
        leafRecord.setField("request_size", FieldType.INT, 1399);
        assertTrue(leafRecord.isValid());

        Record rootRecord = new StandardRecord("root");
        rootRecord.setField("request_str", FieldType.STRING, "zer");
        assertTrue(rootRecord.isValid());

        rootRecord.setField("record_leaf", FieldType.RECORD, leafRecord);
        assertTrue(rootRecord.isValid());


        assertTrue(rootRecord.getField("record_leaf").asRecord()
                .getField("request_size").asInteger() == 1399);



    }

    @Test
    public void validatePosition() {

        Record rootRecord = new StandardRecord("root");
        rootRecord.setField("request_str", FieldType.STRING, "zer");
        assertTrue(rootRecord.isValid());


        Position position = Position.from(1.0, 2.0, 3.0, 4.0, 5.0, 6,7, 8.0, new Date(10));



        assertFalse(rootRecord.hasPosition());
        rootRecord.setPosition(position);
        assertTrue(rootRecord.hasPosition());


        assertTrue(rootRecord.getPosition().getAltitude() == 1.0 );
        assertTrue(rootRecord.getPosition().getHeading() == 2.0 );
        assertTrue(rootRecord.getPosition().getLatitude() == 3.0 );
        assertTrue(rootRecord.getPosition().getLongitude() == 4.0 );
        assertTrue(rootRecord.getPosition().getPrecision() == 5.0 );
        assertTrue(rootRecord.getPosition().getSatellites() == 6 );
        assertTrue(rootRecord.getPosition().getStatus() == 7 );
        assertTrue(rootRecord.getPosition().getSpeed() == 8.0 );
        assertTrue(rootRecord.getPosition().getTimestamp().getTime() == 10L );

    }
}