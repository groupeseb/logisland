import { Component } from '@angular/core';

@Component({
  selector: 'li-leftbar',
  template: `<md-sidenav md-component-id="right">
                <a routerLink="/topic" routerLinkActive="active">Topics</a>
             </md-sidenav>`
})
export class LeftbarComponent {
}
