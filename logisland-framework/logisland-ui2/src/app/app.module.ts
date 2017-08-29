import { AppComponent } from './app.component';
import { AppConfiguration } from './app.config';
import {CdkTableModule} from '@angular/cdk';
import { HomePage } from './home-page';
import { JobService } from './job/job-service';
import { TopicService } from './topic/topic-service';
import { TopicListComponent } from './topic/topic-list/topic-list.component';
import { TopicDetailPage } from './topic/topic-detail-page';
import { PageNotFound } from './page-not-found';

import { BrowserModule } from '@angular/platform-browser';
import 'hammerjs';
import { HttpModule } from '@angular/http'; // <<< changed
import { MdButtonModule,
         MdCardModule,
         MdCommonModule,
         MdIconModule,
         MdInputModule,
         MdListModule,
         MdMenuModule,
         MdPaginatorModule,
         MdSidenavModule,
         MdTableModule,
         MdTabsModule,
         MdToolbarModule
        } from '@angular/material';
import { NgModule } from '@angular/core';
import {ResourceModule} from 'ngx-resource';
import { Router, RouterModule, Routes } from '@angular/router';
import { JobListComponent } from './job/job-list/job-list.component';
import { ErrorListComponent } from './error/error-list/error-list.component';

const appRoutes: Routes = [
  { path: '', component: HomePage },
  { path: 'topic', component: TopicListComponent },
  { path: 'topic/:id', component: TopicDetailPage },
  { path: 'job', component: JobListComponent },
  { path: 'error', component: ErrorListComponent },
  { path: '**', component: PageNotFound }
];

@NgModule({
  declarations: [
    AppComponent,
    HomePage,
    TopicDetailPage,
    PageNotFound,
    TopicListComponent,
    JobListComponent,
    ErrorListComponent,
  ],
  imports: [
    BrowserModule,
    CdkTableModule,
    HttpModule,
    MdButtonModule,
    MdCardModule,
    MdCommonModule,
    MdIconModule,
    MdInputModule,
    MdListModule,
    MdMenuModule,
    MdPaginatorModule,
    MdSidenavModule,
    MdTableModule,
    MdTabsModule,
    MdToolbarModule,
    ResourceModule.forRoot(),
    RouterModule.forRoot(
      appRoutes
    )
  ],
  providers: [ AppConfiguration, JobService, TopicService ],
  bootstrap: [AppComponent]
})
export class AppModule {
  // Diagnostic only: inspect router configuration
  constructor(router: Router) {
    console.log('Routes: ', JSON.stringify(router.config, undefined, 2));
  }
}
