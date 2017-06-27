import { AppConfiguration } from '../app.config';
import { CrudService } from '../crud-service';
import { Topic } from './topic';
import { Injectable, Inject } from '@angular/core';
import { Http } from '@angular/http';

@Injectable()
export class TopicService extends CrudService<Topic> {
  constructor(private config: AppConfiguration, http: Http) {
    super(config.topics_api, http);
  }
}
