import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Product } from '../products/product.model';
import { CartItem } from '../cart/cart-item/cart-item.model';

@Injectable({providedIn: 'root'})
export class CartService {

  addCartItemEvent: EventEmitter<CartItem> = new EventEmitter<CartItem>();
  updateCartItemEvent: EventEmitter<CartItem> = new EventEmitter<CartItem>();

  constructor() {}

  addProductToCart(prod: Product) {
    console.log('Add prod to cart', prod);
    let ci = new CartItem(prod.id, prod.productName, prod.productImg, 1);
    console.log('Add prod ci to cart', ci);
    this.addCartItemEvent.emit(ci);
  }

  updateItemInCart(newCartItem: CartItem) {
    console.log('Update cart item in cart', newCartItem);
    this.updateCartItemEvent.emit(newCartItem);
  }

}
