import { RestangularModule, Restangular } from 'ngx-restangular';
import { CrudService } from '../crud-service';
import { Topic } from './topic';
import { Injectable, Inject } from '@angular/core';

@Injectable()
export class TopicService extends CrudService<Topic> {

   constructor(@Inject(Restangular) restangular) {
       super(restangular, 'topics');
    }

}
