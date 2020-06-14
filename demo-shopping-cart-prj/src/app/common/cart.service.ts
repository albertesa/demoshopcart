import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Product } from '../products/product.model';
import { CartItem } from '../cart/cart-item/cart-item.model';

@Injectable({providedIn: 'root'})
export class CartService {

  addCartItemEvent: EventEmitter<CartItem> = new EventEmitter<CartItem>();

  constructor() {}

  addProductToCart(prod: Product) {
    console.log('prod', prod);
    let ci = new CartItem(prod.id, prod.productName, prod.productImg, 1);
    console.log('ci', ci);
    this.addCartItemEvent.emit(ci);
  }

}
