export class SchemaEntry {
    public name = 'key1';
    public encrypted = false;
    public indexed = true;
    public persistent = true;
    public optional = true;
    public type = 'STRING';

    constructor(name: string) {
        this.name = name;
    }

}

export class Topic {
    public name: string;
    public partitions = 1;
    public replicationFactor = 1;
    public documentation: String = 'description of the topic';
    public serializer:  string; // = 'com.hurence.logisland.serializer.KryoSerializer';
    public businessTimeField:  String = 'record_time';
    public rowkeyField:  String = 'record_id';
    public recordTypeField:  String = 'record_type';
    public keySchema: SchemaEntry[] = [new SchemaEntry('key1'), new SchemaEntry('key2')];
    public valueSchema: SchemaEntry[] = [new SchemaEntry('value1'), new SchemaEntry('value2')];
}
