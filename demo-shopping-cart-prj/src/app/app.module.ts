import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { FormsModule, NG_VALIDATORS } from '@angular/forms';

import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { FlexLayoutModule } from '@angular/flex-layout';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProductsComponent } from './products/products/products.component';
import { ProductComponent } from './products/product/product.component';
import { CartComponent } from './cart/cart/cart.component';
import { CartItemComponent } from './cart/cart-item/cart-item.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material/material.module';
import { MatDividerModule } from '@angular/material/divider';
import { SignupComponent } from './auth/signup/signup.component';
import { LoginComponent } from './auth/login/login.component';
import { WelcomeComponent } from './welcome/welcome/welcome.component';
import { ProductsCartComponent } from './products-cart/products-cart/products-cart.component';
import { ConfigService } from './common/app-config.service';
import { AppConfig } from './common/app-config.model';
import { AuthInterceptorService } from './auth/auth-interceptor.service';
import { ExtendedEmailValidatorDirective } from './common/extended-email.directive';
import { ProductViewComponent } from './products/product-view/product-view.component';
import { ProductEditComponent } from './products/product-edit/product-edit.component';

@NgModule({
  declarations: [
    AppComponent,
    ProductsComponent,
    ProductComponent,
    CartComponent,
    CartItemComponent,
    SignupComponent,
    LoginComponent,
    WelcomeComponent,
    ProductsCartComponent,
    ExtendedEmailValidatorDirective,
    ProductViewComponent,
    ProductEditComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MaterialModule,
    FlexLayoutModule,
    MatDividerModule
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initApp,
      multi: true,
      deps: [HttpClient, ConfigService]
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

export function initApp(http: HttpClient, cfgSvc: ConfigService) {
  return () => {
    return http.get<AppConfig>('assets/appConfig.json')
      .subscribe(appCfg => {
        cfgSvc.setConfig(appCfg);
      });
  };
}
