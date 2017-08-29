import { Component, ElementRef, ViewChild } from '@angular/core';
import { SearchCriteria } from '../../search-criteria';
import { Topic } from '../topic';
import { TopicService } from '../topic-service';
import { DataSource } from '@angular/cdk';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';
import { ITdDataTableColumn } from '@covalent/core';
import { TdDialogService } from '@covalent/core';
import { CrudServiceToDataSource } from '../../crud-service-to-data-source';

@Component({
  selector: 'app-topic-list',
  templateUrl: './topic-list.component.html',
  styleUrls: ['./topic-list.component.css']
})

export class TopicListComponent {
  displayedColumns = ['name', 'documentation'];
  dataSource: DataSource<Topic>;
  @ViewChild('filter') filter: ElementRef;
  
  constructor(private service: TopicService) {
    this.dataSource = new CrudServiceToDataSource<Topic>(service);
  }

}
