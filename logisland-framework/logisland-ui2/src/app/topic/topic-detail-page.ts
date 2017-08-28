import { ActivatedRoute } from '@angular/router';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Topic } from './topic';
import { TopicService } from './topic-service';

@Component({
  selector: 'app-topic-detail',
  templateUrl: `./topic-detail-page.html`
})

export class TopicDetailPage implements OnInit, OnDestroy {
  private sub: any;
  private topic: Topic;

  constructor(private topicService: TopicService, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      const id = params['id'];
      this.topic = this.topicService.get(id);
      // In a real app: dispatch action to load the details here.
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }
}
