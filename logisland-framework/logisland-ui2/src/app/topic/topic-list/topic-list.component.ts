import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { SearchCriteria } from '../../search-criteria';
import { Topic } from '../topic';
import { TopicService } from '../topic-service';
import { DataSourceExt } from '../../data-source-ext';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';

import { ITdDataTableColumn } from '@covalent/core';
import { TdDialogService } from '@covalent/core';
import { RestDataSource } from '../../rest-data-source';

@Component({
  selector: 'app-topic-list',
  templateUrl: './topic-list.component.html',
  styleUrls: ['./topic-list.component.css']
})

export class TopicListComponent implements OnInit {
  displayedColumns = ['name', 'documentation'];
  dataSource: DataSourceExt<Topic>;

  constructor(private service: TopicService) {
    this.dataSource = new RestDataSource<Topic>(this.service);
  }

  onSearch(value: string) {
    console.log('press enter');
    this.dataSource.searchFilter = value;
  }

  ngOnInit() {
  }

}
