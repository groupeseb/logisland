import { AppConfiguration } from '../app.config';
import { RestService } from '../rest-service';
import { Topic } from './topic';
import { Injectable, Inject } from '@angular/core';
import { Http } from '@angular/http';

@Injectable()
export class TopicService extends RestService {
  constructor(private config: AppConfiguration, http: Http) {
    super(config.topics_api, http);
  }
}
