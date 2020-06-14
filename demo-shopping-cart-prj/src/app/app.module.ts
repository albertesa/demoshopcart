import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProductsComponent } from './products/products/products.component';
import { ProductComponent } from './products/product/product.component';
import { CartComponent } from './cart/cart/cart.component';
import { CartItemComponent } from './cart/cart-item/cart-item.component';

@NgModule({
  declarations: [
    AppComponent,
    ProductsComponent,
    ProductComponent,
    CartComponent,
    CartItemComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
