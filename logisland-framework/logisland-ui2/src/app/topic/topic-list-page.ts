import { Component } from '@angular/core';
import { Topic } from './topic';
import { TopicService } from './topic-service';
import { RestangularModule, Restangular } from 'ngx-restangular';

@Component({
  selector: 'li-topic-list',
  template: ` <h2><fa name="database"></fa> Topics</h2>
              <md-nav-list>
                <md-list-item *ngFor="let topic of topics">
                  <a [routerLink]="['/topic/detail', topic.id]">
                    {{topic.id}} {{topic.description}}
                  </a>
                </md-list-item>
             </md-nav-list>`
})

export class TopicListPage {
  topics: Topic[];

  constructor(private topicService: TopicService) {
    this.topics = [];
    for (let i = 0; i < 10; i++) {
      const newTopic = new Topic();
      newTopic.id = i.toString();
      newTopic.description = 'description ' + i.toString();
      this.topics.push(newTopic);
    }
  }
}
