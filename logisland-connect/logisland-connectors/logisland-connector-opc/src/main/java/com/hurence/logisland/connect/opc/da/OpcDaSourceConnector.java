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

package com.hurence.logisland.connect.opc.da;

import com.hurence.logisland.connect.opc.CommonUtils;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.config.ConfigValue;
import org.apache.kafka.common.utils.Utils;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * OPC-DA Connector.
 *
 * @author amarziali
 */
public class OpcDaSourceConnector extends SourceConnector {

    private static final Logger logger = LoggerFactory.getLogger(OpcDaSourceConnector.class);

    private Map<String, ConfigValue> configValues;

    public static final String PROPERTY_HOST = "host";
    public static final String PROPERTY_PORT = "port";
    public static final String PROPERTY_DOMAIN = "domain";
    public static final String PROPERTY_USER = "user";
    public static final String PROPERTY_PASSWORD = "password";
    public static final String PROPERTY_CLSID = "clsId";
    public static final String PROPERTY_PROGID = "progId";
    public static final String PROPERTY_TAGS = "tags";
    public static final String PROPERTY_SOCKET_TIMEOUT = "socketTimeoutMillis";
    public static final String PROPERTY_DEFAULT_REFRESH_PERIOD = "defaultRefreshPeriodMillis";
    public static final String PROPERTY_DIRECT_READ = "directReadFromDevice";


    /**
     * The configuration.
     */
    private static final ConfigDef CONFIG = new ConfigDef()
            .define(PROPERTY_HOST, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "The OPC-DA server host")
            .define(PROPERTY_PORT, ConfigDef.Type.INT, ConfigDef.Importance.LOW, "The OPC-DA server port")
            .define(PROPERTY_DOMAIN, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "The logon domain")
            .define(PROPERTY_USER, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "The logon user")
            .define(PROPERTY_PASSWORD, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "The logon password")
            .define(PROPERTY_CLSID, ConfigDef.Type.STRING, ConfigDef.Importance.MEDIUM, "The CLSID of the OPC server COM component")
            .define(PROPERTY_PROGID, ConfigDef.Type.STRING, ConfigDef.Importance.MEDIUM, "The Program ID of the OPC server COM component")
            .define(PROPERTY_TAGS, ConfigDef.Type.LIST, Collections.emptyList(), (name, value) -> {
                if (value == null) {
                    throw new ConfigException("Cannot be null");
                }
                List<String> list = (List<String>) value;
                for (String s : list) {
                    if (!CommonUtils.validateTagFormat(s)) {
                        throw new ConfigException("Tag list should be like [tag_name]:[refresh_period_millis] with optional refresh period");
                    }
                }
            }, ConfigDef.Importance.HIGH, "The tags to subscribe to following format tagname:refresh_period_millis. E.g. myTag:1000")
            .define(PROPERTY_SOCKET_TIMEOUT, ConfigDef.Type.LONG, 10_000,ConfigDef.Importance.LOW, "The socket timeout (defaults to 10 seconds)")
            .define(PROPERTY_DEFAULT_REFRESH_PERIOD, ConfigDef.Type.LONG, 1_000, ConfigDef.Importance.LOW, "The default data refresh period in milliseconds")
            .define(PROPERTY_DIRECT_READ, ConfigDef.Type.BOOLEAN, false, ConfigDef.Importance.LOW, "Use server cache or read directly from device");


    @Override
    public String version() {
        return getClass().getPackage().getImplementationVersion();
    }

    @Override
    public void start(Map<String, String> props) {
        //shallow copy
        configValues = config().validate(props).stream().collect(Collectors.toMap(ConfigValue::name, Function.identity()));
        logger.info("Starting OPC-DA connector (version {}) on server {} reading tags {}", version(),
                configValues.get(PROPERTY_HOST).value(), configValues.get(PROPERTY_TAGS).value());
    }

    @Override
    public Class<? extends Task> taskClass() {
        return OpcDaSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        Long defaultRefreshPeriod = (Long) configValues.get(PROPERTY_DEFAULT_REFRESH_PERIOD).value();
        //first partition tags per refresh period
        Map<Long, List<String>> tagPartitions = ((List<String>) configValues.get(PROPERTY_TAGS).value())
                .stream().collect(Collectors.groupingBy(tag -> CommonUtils.parseTag(tag, defaultRefreshPeriod).getValue()));
        List<List<String>> tags = new ArrayList<>(tagPartitions.values());
        int maxPartitions = Math.min(maxTasks, tags.size());
        int batchSize = (int) Math.ceil((double) tags.size() / maxPartitions);
        //then find the ideal partition size and flatten tag list into a comma separated string (since config is a map of string,string)
        return IntStream.range(0, maxPartitions)
                .mapToObj(i -> tags.subList(i * batchSize, Math.min((i + 1) * batchSize, tags.size())))
                .map(l -> {
                    Map<String, String> ret = configValues.entrySet().stream()
                            .filter(a -> a.getValue().value() != null)
                            .collect(Collectors.toMap(a -> a.getKey(), a -> a.getValue().value().toString()));
                    ret.put(PROPERTY_TAGS, Utils.join(l.stream().flatMap(List::stream).collect(Collectors.toList()), ","));
                    return ret;
                })
                .collect(Collectors.toList());


    }

    @Override
    public void stop() {
        logger.info("Stopping OPC-DA connector (version {}) on server {}", version(), configValues.get(PROPERTY_HOST).value());
    }

    @Override
    public ConfigDef config() {
        return CONFIG;
    }
}
