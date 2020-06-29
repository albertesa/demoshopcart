import { Injectable } from '@angular/core';

import { Subject } from 'rxjs';
import { AppConfig } from './app-config.model';

@Injectable({ providedIn: 'root' })
export class ConfigService {

  appConfig: AppConfig;
  appConfigLoaded: Subject<AppConfig> = new Subject<AppConfig>();

  constructor() { }

  setConfig(appConfig: AppConfig) {
    console.log('appConfig', JSON.stringify(appConfig));
    this.appConfig = { ...appConfig };
    this.appConfigLoaded.next({ ...this.appConfig });
  }

  getServer(): string {
    return this.appConfig.server;
  }

}
