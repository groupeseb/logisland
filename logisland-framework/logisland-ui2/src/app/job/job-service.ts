import { AppConfiguration } from '../app.config';
import { CrudService } from '../crud-service';
import { Job } from './job';
import { Injectable, Inject } from '@angular/core';
import { Http } from '@angular/http';

@Injectable()
export class JobService extends CrudService<Job> {
  constructor(private config: AppConfiguration, http: Http) {
    super(config.jobs_api, http);
  }
}
