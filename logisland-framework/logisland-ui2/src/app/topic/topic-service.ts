import { CrudService } from '../crud-service';
import { Topic } from './topic';
import { Injectable, Inject } from '@angular/core';
import {Resource, ResourceParams, ResourceCRUD, ResourceMethod} from 'ngx-resource';

@Injectable()
@ResourceParams({
  url: 'http://localhost:8081/topics'
})
export class TopicService extends CrudService<Topic> {}
