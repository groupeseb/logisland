import { Component } from '@angular/core';
import { Topic } from './topic';

@Component({
  selector: 'li-topic-list',
  template: `<ul><li template="ngFor let topic of topics" [class.odd]="odd">
                <a [routerLink]="['/topic/detail', topic.id]">{{topic.id}} {{topic.description}}</a>
             </li></ul>`
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
