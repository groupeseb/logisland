import { AppComponent } from './app.component';
import { HomePage } from '../home/home-page';
import { LeftbarComponent } from '../nav/leftbar.component';
import { TopicListPage } from '../topic/topic-list-page';
import { TopicDetailPage } from '../topic/topic-detail-page';
import { PageNotFound } from './page-not-found';

import { Router, RouterModule, Routes } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { Angular2FontawesomeModule } from 'angular2-fontawesome/angular2-fontawesome'
import 'hammerjs';
import { NgModule } from '@angular/core';
import { MdButtonModule, MdCardModule, MdIconModule, MdMenuModule, MdSidenavModule, MdToolbarModule } from '@angular/material';

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
    MdButtonModule,
    MdCardModule,
    MdIconModule,
    MdMenuModule,
    MdSidenavModule,
    MdToolbarModule,
    RouterModule.forRoot(
      appRoutes
    )
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
  // Diagnostic only: inspect router configuration
  constructor(router: Router) {
    console.log('Routes: ', JSON.stringify(router.config, undefined, 2));
  }
}
