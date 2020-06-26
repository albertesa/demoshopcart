import { Component } from '@angular/core';
import { ConfigService } from './common/app-config.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'demo-shopping-cart-prj';

  constructor(private cfgSvc: ConfigService) {}

}
