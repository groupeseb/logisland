import { AppComponent } from './app.component';
import { HomeComponent } from '../home/home.component';
import { LeftbarComponent } from '../nav/leftbar.component';
import { TopicListComponent } from '../topic/list/topiclist.component';
import { PageNotFoundComponent } from './app-page-not-found.component';

import { Router, RouterModule, Routes } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import 'hammerjs';
import { NgModule } from '@angular/core';
import { MdButtonModule, MdCardModule, MdIconModule, MdMenuModule, MdSidenavModule, MdToolbarModule } from '@angular/material';

const appRoutes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'topic',
    component: TopicListComponent
  },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    LeftbarComponent,
    HomeComponent,
    TopicListComponent,
    PageNotFoundComponent,
  ],
  imports: [
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
