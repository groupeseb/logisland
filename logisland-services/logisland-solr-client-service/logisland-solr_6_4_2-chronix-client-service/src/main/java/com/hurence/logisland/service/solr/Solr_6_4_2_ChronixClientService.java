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
package com.hurence.logisland.service.solr;

import com.hurence.logisland.annotation.documentation.CapabilityDescription;
import com.hurence.logisland.annotation.documentation.Tags;
import com.hurence.logisland.annotation.lifecycle.OnEnabled;
import com.hurence.logisland.component.InitializationException;
import com.hurence.logisland.component.PropertyDescriptor;
import com.hurence.logisland.controller.AbstractControllerService;
import com.hurence.logisland.controller.ControllerServiceInitializationContext;
import com.hurence.logisland.processor.ProcessException;
import com.hurence.logisland.record.Record;
import com.hurence.logisland.service.datastore.DatastoreClientService;
import com.hurence.logisland.service.datastore.DatastoreClientServiceException;
import com.hurence.logisland.service.datastore.MultiGetQueryRecord;
import com.hurence.logisland.service.datastore.MultiGetResponseRecord;
import com.hurence.logisland.validator.StandardValidators;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Tags({"solr", "client"})
@CapabilityDescription("Implementation of ChronixClientService for Solr 6 4 2")
public class Solr_6_4_2_ChronixClientService extends AbstractControllerService implements DatastoreClientService {

    private static Logger logger = LoggerFactory.getLogger(Solr_6_4_2_ChronixClientService.class);
    protected volatile SolrClient solr;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ChronixUpdater updater;
    final BlockingQueue<Record> queue = new ArrayBlockingQueue<>(1000000);

    public static final PropertyDescriptor SOLR_CLOUD = new PropertyDescriptor.Builder()
            .name("solr.cloud")
            .description("is slor cloud enabled")
            .required(true)
            .addValidator(StandardValidators.BOOLEAN_VALIDATOR)
            .defaultValue("false")
            .build();

    public static final PropertyDescriptor SOLR_CONNECTION_STRING = new PropertyDescriptor.Builder()
            .name("solr.connection.string")
            .description("zookeeper quorum host1:2181,host2:2181 for solr cloud or http address of a solr core ")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .defaultValue("localhost:8983/solr")
            .build();

    public static final PropertyDescriptor SOLR_COLLECTION = new PropertyDescriptor.Builder()
            .name("solr.collection")
            .description("name of the collection to use")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();


    public static final PropertyDescriptor FLUSH_INTERVAL = new PropertyDescriptor.Builder()
            .name("flush.interval")
            .description("flush interval in ms")
            .required(false)
            .addValidator(StandardValidators.POSITIVE_INTEGER_VALIDATOR)
            .defaultValue("500")
            .build();

    public static final PropertyDescriptor METRICS_TYPE_MAPPING = new PropertyDescriptor.Builder()
            .name("metrics.type.mapping")
            .description("The mapping between record field name and chronix metric type. " +
                    "This is a comma separated list. E.g. record_value:metric,quality:quality")
            .required(false)
            .addValidator(StandardValidators.COMMA_SEPARATED_LIST_VALIDATOR)
            .defaultValue("")
            .build();




    @Override
    public List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        List<PropertyDescriptor> props = new ArrayList<>();
        props.add(BATCH_SIZE);
        props.add(BULK_SIZE);
        props.add(SOLR_CLOUD);
        props.add(SOLR_COLLECTION);
        props.add(SOLR_CONNECTION_STRING);
        props.add(FLUSH_INTERVAL);
        props.add(METRICS_TYPE_MAPPING);
        return Collections.unmodifiableList(props);
    }

    @Override
    @OnEnabled
    public void init(ControllerServiceInitializationContext context) throws InitializationException {
        synchronized (this) {
            try {
                createSolrClient(context);
                createChronixStorage(context);
            } catch (Exception e) {
                throw new InitializationException("Error while instantiating ChronixClientService. " +
                        "Please check your configuration!", e);
            }
        }
    }


    private Map<String, String> createMetricsTypeMapping(ControllerServiceInitializationContext context) {
        return Arrays.stream(context.getPropertyValue(METRICS_TYPE_MAPPING).asString()
                .split(","))
                .filter(StringUtils::isNotBlank)
                .map(s -> s.split(":"))
                .collect(Collectors.toMap(a -> a[0], a -> a[1]));
    }

    /**
     * Instantiate Chronix Client. This should be called by subclasses' @OnScheduled method to create a client
     * if one does not yet exist. If called when scheduled, closeClient() should be called by the subclasses' @OnStopped
     * method so the client will be destroyed when the processor is stopped.
     *
     * @param context The context for this processor
     * @throws ProcessException if an error occurs while creating an Chronix client
     */
    protected void createSolrClient(ControllerServiceInitializationContext context) throws ProcessException {
        if (solr != null) {
            return;
        }


        // create a solr client
        final boolean isCloud = context.getPropertyValue(SOLR_CLOUD).asBoolean();
        final String connectionString = context.getPropertyValue(SOLR_CONNECTION_STRING).asString();
        final String collection = context.getPropertyValue(SOLR_COLLECTION).asString();


        if (isCloud) {
            //logInfo("creating solrCloudClient on $solrUrl for collection $collection");
            CloudSolrClient cloudSolrClient = new CloudSolrClient.Builder().withZkHost(connectionString).build();
            cloudSolrClient.setDefaultCollection(collection);
            cloudSolrClient.setZkClientTimeout(30000);
            cloudSolrClient.setZkConnectTimeout(30000);
            solr = cloudSolrClient;
        } else {
            // logInfo(s"creating HttpSolrClient on $solrUrl for collection $collection")
            solr = new HttpSolrClient.Builder(connectionString + "/" + collection).build();
        }


    }


    protected void createChronixStorage(ControllerServiceInitializationContext context) throws ProcessException {
        if (updater != null) {
            return;
        }

        // setup a thread pool of solr updaters
        int batchSize = context.getPropertyValue(BATCH_SIZE).asInteger();
        long flushInterval = context.getPropertyValue(FLUSH_INTERVAL).asLong();
        updater = new ChronixUpdater(solr, queue, createMetricsTypeMapping(context), batchSize, flushInterval);
        executorService.execute(updater);

    }

    @Override
    public void createCollection(String name, int partitionsCount, int replicationFactor) throws DatastoreClientServiceException {
        throw new DatastoreClientServiceException("not implemented yet");
    }

    @Override
    public void dropCollection(String name) throws DatastoreClientServiceException {
        throw new DatastoreClientServiceException("not implemented yet");
    }

    @Override
    public long countCollection(String name) throws DatastoreClientServiceException {
        throw new DatastoreClientServiceException("not implemented yet");
    }

    @Override
    public boolean existsCollection(String name) throws DatastoreClientServiceException {
        return false;
    }

    @Override
    public void refreshCollection(String name) throws DatastoreClientServiceException {
        throw new DatastoreClientServiceException("not implemented yet");
    }

    @Override
    public void copyCollection(String reindexScrollTimeout, String src, String dst) throws DatastoreClientServiceException {
        throw new DatastoreClientServiceException("not implemented yet");
    }

    @Override
    public void createAlias(String collection, String alias) throws DatastoreClientServiceException {
        throw new DatastoreClientServiceException("not implemented yet");
    }

    @Override
    public boolean putMapping(String indexName, String doctype, String mappingAsJsonString) throws DatastoreClientServiceException {
        throw new DatastoreClientServiceException("not implemented yet");
    }

    @Override
    public void bulkFlush() throws DatastoreClientServiceException {
    }

    @Override
    public void bulkPut(String collectionName, Record record) throws DatastoreClientServiceException {

        if (record != null)
            queue.add(record);
        else
            logger.debug("trying to add null record in the queue");
      /*  try {
            MetricTimeSeries metric = convertToMetric(record);

            List<MetricTimeSeries> timeSeries = new ArrayList<>();
            timeSeries.add(metric);
            storage.add(converter, timeSeries, solr);

            solr.commit();


        } catch (DatastoreClientServiceException ex) {
            logger.error(ex.toString() + " for record " + record.toString());
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }


    @Override
    public void put(String collectionName, Record record, boolean asynchronous) throws DatastoreClientServiceException {

        bulkPut(collectionName, record);


    }

    @Override
    public void remove(String collectionName, Record record, boolean asynchronous) throws DatastoreClientServiceException {
        try {
            solr.deleteById(collectionName, record.getId());

        } catch (SolrServerException | IOException e) {
            logger.error(e.toString());
            throw new DatastoreClientServiceException(e);
        }
    }

    @Override
    public List<MultiGetResponseRecord> multiGet(List<MultiGetQueryRecord> multiGetQueryRecords) throws DatastoreClientServiceException {
        return null;
    }

    @Override
    public Record get(String collectionName, Record record) throws DatastoreClientServiceException {
        return null;
    }

    @Override
    public Collection<Record> query(String queryString) {
        try {
            SolrQuery query = new SolrQuery();
            query.setQuery(queryString);

            QueryResponse response = solr.query(query);

            //response.getResults().forEach(doc -> doc.);

        } catch (SolrServerException | IOException e) {
            logger.error(e.toString());
            throw new DatastoreClientServiceException(e);
        }

        return null;
    }

    @Override
    public long queryCount(String queryString) {
        try {
            SolrQuery query = new SolrQuery();
            query.setQuery(queryString);

            QueryResponse response = solr.query(query);

            return response.getResults().getNumFound();

        } catch (SolrServerException | IOException e) {
            logger.error(e.toString());
            throw new DatastoreClientServiceException(e);
        }
    }
}
