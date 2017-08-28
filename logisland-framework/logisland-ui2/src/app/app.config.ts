
import { Injectable, Inject } from '@angular/core';

@Injectable()
export class AppConfiguration {
    public readonly jobs_api = 'http://localhost:8081/jobs';
    public readonly topics_api = 'http://localhost:8081/topics';
    public readonly plugins_api = 'http://localhost:8081/processors';

    constructor() {
    }

}

