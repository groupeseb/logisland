import { AppComponent } from './app.component';
import { AppRoutingModule } from './app.routing.module';
import { HomeComponent } from '../home/home.component';
import { LeftbarComponent } from '../nav/leftbar.component';
import { TopicListComponent } from '../topic/list/topiclist.component';
import { PageNotFoundComponent } from './app-page-not-found.component';

import { Router } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import 'hammerjs';
import { NgModule } from '@angular/core';
import { MdButtonModule, MdCardModule, MdIconModule, MdMenuModule, MdSidenavModule, MdToolbarModule } from '@angular/material';

@NgModule({
  declarations: [
    AppComponent,
    LeftbarComponent,
    HomeComponent,
    TopicListComponent,
    PageNotFoundComponent,
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    MdButtonModule,
    MdCardModule,
    MdIconModule,
    MdMenuModule,
    MdSidenavModule,
    MdToolbarModule,
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
