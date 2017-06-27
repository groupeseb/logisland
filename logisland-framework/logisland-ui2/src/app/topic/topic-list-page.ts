import { Component } from '@angular/core';
import { SearchCriteria } from '../search-criteria';
import { Topic } from './topic';
import { TopicService } from './topic-service';

@Component({
  selector: 'li-topic-list',
  template: ` <h3 class="title"><i class="fa fa-database"></i> Topics</h3>
              <md-nav-list>
                <md-list-item *ngFor="let topic of topics">
                  <a [routerLink]="['/topic/detail', topic.id]">
                    {{topic.name}} {{topic.documentation}}
                  </a>
                </md-list-item>
             </md-nav-list>`,
  styles: [`
      .title {
        text-align: left;
        padding-left: 1em;
      }
    `]
})

export class TopicListPage {
  private topics: Topic[] = [];

  constructor(private topicService: TopicService) {
    // get the topic
    this.topics = this.topicService.query();
  }
}
