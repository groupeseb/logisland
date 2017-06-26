import { AppComponent } from './app.component';
import { HomePage } from './home-page';
import { LeftbarComponent } from './leftbar.component';
import { TopicService } from './topic/topic-service';
import { TopicListPage } from './topic/topic-list-page';
import { TopicDetailPage } from './topic/topic-detail-page';
import { PageNotFound } from './page-not-found';

import { Angular2FontawesomeModule } from 'angular2-fontawesome/angular2-fontawesome'
import { BrowserModule } from '@angular/platform-browser';
import 'hammerjs';
import { HttpModule } from '@angular/http'; // <<< changed
import { MdButtonModule,
         MdCardModule,
         MdIconModule,
         MdListModule,
         MdMenuModule,
         MdSidenavModule,
         MdToolbarModule
        } from '@angular/material';
import { NgModule } from '@angular/core';
import {ResourceModule} from 'ngx-resource';

import { Router, RouterModule, Routes } from '@angular/router';

const appRoutes: Routes = [
  { path: '', component: HomePage },
  { path: 'topic', component: TopicListPage },
  { path: 'topic/detail/:id', component: TopicDetailPage },
  { path: '**', component: PageNotFound }
];

@NgModule({
  declarations: [
    AppComponent,
    LeftbarComponent,
    HomePage,
    TopicListPage,
    TopicDetailPage,
    PageNotFound,
  ],
  imports: [
    Angular2FontawesomeModule,
    BrowserModule,
    HttpModule,
    MdButtonModule,
    MdCardModule,
    MdIconModule,
    MdListModule,
    MdMenuModule,
    MdSidenavModule,
    MdToolbarModule,
    ResourceModule.forRoot(),
    RouterModule.forRoot(
      appRoutes
    )
  ],
  providers: [ TopicService ],
  bootstrap: [AppComponent]
})
export class AppModule {
  // Diagnostic only: inspect router configuration
  constructor(router: Router) {
    console.log('Routes: ', JSON.stringify(router.config, undefined, 2));
  }
}
