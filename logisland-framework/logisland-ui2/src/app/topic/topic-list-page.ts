import { Component } from '@angular/core';
import { SearchCriteria } from '../search-criteria';
import { Topic } from './topic';
import { TopicService } from './topic-service';

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
    const tt: Topic[] = this.topicService.query({});
    // create a new topic
    const newTopic = this.topicService.save(new Topic('123')).name;
    this.topics = topicService.query();
  }
}
