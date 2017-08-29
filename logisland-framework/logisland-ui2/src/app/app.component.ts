import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `<div>
              <md-toolbar color="primary">
                <table>
                  <tr>
                    <td><a routerLink="/"><i style="color: white" class="fa fa-bars"></i></a></td>
                    <td ><h1>LogIsland</h1></td>
                    <td width="100%" align="right"></td>
                  </tr>
                </table>
              </md-toolbar>
              <div>
                <table style="width: 100%; min-height: 800px">
                  <tr valign="top">
                    <td class="leftbar">
                      <md-sidenav>
                        <md-nav-list>
                          <md-list-item>
                            <a md-line routerLink="/topic" routerLinkActive="active"><i class="fa fa-database"></i> TOPICS</a>
                          </md-list-item>
                          <md-list-item>
                            <a md-line routerLink="/job"><i class="fa fa-bolt"></i> JOBS</a>
                          </md-list-item>
                          <md-list-item>
                            <a md-line routerLink="/error"><i class="fa fa-exclamation"></i> ERRORS</a>
                          </md-list-item>
                        </md-nav-list>
                      </md-sidenav>
                    </td>
                    <td>
                      <div style="text-align:center">
                        <router-outlet></router-outlet>
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </div>`,
  styles: [`
      .leftbar {
        background-color: #eee;
        border-right: 1px solid #aaa;
        width: 200px;
        padding: 1em;
        padding-top: 0px;
      }
    `]
})
export class AppComponent {
  title = 'Logisland';
}
