import { Component } from '@angular/core';
import { Topic } from './topic';

@Component({
  selector: 'li-topic-list',
  template: `<md-nav-list>
                <a *ngFor="let topic of topics" md-list-item [routerLink]="['/topic/detail', topic.id]">
                  {{topic.id}} {{topic.description}}
                </a>
             </md-nav-list>`
})

export class TopicListPage {
  topics: Topic[];

  constructor() {
    this.topics = [];
    for (let i = 0; i < 10; i++) {
      const newTopic = new Topic();
      newTopic.id = i.toString();
      newTopic.description = 'description ' + i.toString();
      this.topics.push(newTopic);
    }
  }
}
