export class Topic {

    public name: String;
    public partitions = 1;
    public replicationFactor = 1;
    public documentation: String = 'describe here the content of the topic';
    public serializer:  String = 'com.hurence.logisland.serializer.KryoSerializer';
    public businessTimeField:  String = 'record_time';
    public rowkeyField:  String = 'record_id';
    public recordTypeField:  String = 'record_type';
    public keySchema: [
        { name: 'key1', encrypted: false, indexed: true, persistent: true, optional: true, type: 'STRING' },
        { name: 'key2', encrypted: false, indexed: true, persistent: true, optional: true, type: 'STRING' }
    ];
    public valueSchema: [
        { name: 'value1', encrypted: false, indexed: true, persistent: true, optional: true, type: 'STRING' },
        { name: 'value2', encrypted: false, indexed: true, persistent: true, optional: true, type: 'STRING' }
    ];

    constructor(name: String) {
        this.name = name;
    }

}
