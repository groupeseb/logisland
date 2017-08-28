import { Component, OnInit } from '@angular/core';
import { MdTable } from '@angular/material';
import { DataSource } from '@angular/cdk';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';
import { Job } from '../job';
import { JobService } from '../job-service';
import { CrudServiceToDataSource } from '../../crud-service-to-data-source';

@Component({
  selector: 'app-job-list',
  templateUrl: './job-list.component.html',
  styleUrls: ['./job-list.component.css']
})

export class JobListComponent {
  displayedColumns = ['name', 'documentation'];
  dataSource: DataSource<Job>;

  constructor(private service: JobService) {
    this.dataSource = new CrudServiceToDataSource<Job>(service);
  }

}

