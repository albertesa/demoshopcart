import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/common/auth.service';
import { Component } from '@angular/core';
import { ConfigService } from './common/app-config.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'demo-shopping-cart-prj';

  isAuthenticated: boolean = false;

  constructor(private cfgSvc: ConfigService, private authSvc: AuthService) {
    this.isAuthenticated = this.authSvc.isAuthenticated();
    this.authSvc.authEvents.subscribe(aem => {
      console.log('app mod auth event', aem.loggedIn);
      this.isAuthenticated = aem.loggedIn;
    });
  }

  logout() {
    this.authSvc.logout();
  }

}
