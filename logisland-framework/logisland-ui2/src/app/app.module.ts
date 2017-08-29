import { AppConfiguration } from './app.config';
import { CdkTableModule } from '@angular/cdk';
import { JobService } from './job/job-service';
import { TopicService } from './topic/topic-service';
import { TopicListComponent } from './topic/topic-list/topic-list.component';
import { TopicDetailPage } from './topic/topic-detail-page';
import { PageNotFound } from './page-not-found';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import 'hammerjs';
import { HomePageComponent } from './home-page/home-page.component';
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
import { RootComponent } from './root/root.component';
import { NgModule } from '@angular/core';
import { ResourceModule } from 'ngx-resource';
import { Router, RouterModule, Routes } from '@angular/router';
import { JobListComponent } from './job/job-list/job-list.component';
import { ErrorListComponent } from './error/error-list/error-list.component';

const appRoutes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'topic', component: TopicListComponent },
  { path: 'topic/:id', component: TopicDetailPage },
  { path: 'job', component: JobListComponent },
  { path: 'error', component: ErrorListComponent },
  { path: '**', component: PageNotFound }
];

@NgModule({
  declarations: [
    HomePageComponent,
    TopicDetailPage,
    PageNotFound,
    RootComponent,
    TopicListComponent,
    JobListComponent,
    ErrorListComponent,
    HomePageComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
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
  bootstrap: [ RootComponent ]
})
export class AppModule {
  // Diagnostic only: inspect router configuration
  constructor(router: Router) {
    console.log('Routes: ', JSON.stringify(router.config, undefined, 2));
  }
}
