import { Injectable, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Product } from '../products/product.model';
import { CartItem } from '../cart/cart-item/cart-item.model';
import { RepositoryService } from './repository.service';
import { Cart } from '../cart/cart/cart.model';
import { Subject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CartService {

  private cart: Cart;
  addCartItemEvent: EventEmitter<CartItem> = new EventEmitter<CartItem>();
  updateCartItemEvent: EventEmitter<CartItem> = new EventEmitter<CartItem>();

  refreshCart: Subject<Cart> = new Subject<Cart>();

  constructor(private repo: RepositoryService) {
    let cartId = this.getValidCartId();
    this.cart = new Cart(cartId);
    if (cartId !== 'new') {
      this.repo.fetchCart(cartId).subscribe(respCart => {
        console.log('Downloaded existing  - service', JSON.stringify(respCart));
        this.cart = { ...respCart };
        this.refreshCart.next(this.cart);
      });
    } else {
      console.log('initializing a new cart');
    }
  }

  getCart(): Cart {
    return this.cart;
  }
  getValidCartId(): string {
    let cartId = localStorage.getItem('cartId');
    if (!cartId || cartId === 'null' || cartId.length == 0) {
      console.log('Cart ID is not defined, set to "new"');
      cartId = 'new';
    }
    return cartId;
  }

  addProductToCart(prod: Product) {
    console.log('Add prod to cart - service', prod);
    let cartItem = new CartItem(prod.id, prod.productName, prod.productImg, 1);
    console.log('Add prod ci to cart - service', cartItem);

    let cartId = this.getValidCartId();
    const foundCi = this.cart.items.filter(ci => ci.productId === cartItem.productId);
    if (foundCi.length > 0) {
      cartItem.numOfItems = foundCi[0].numOfItems + cartItem.numOfItems;
    }
    console.log('cartItem to add - service', cartItem);
    this.repo.setCartItem(cartId, cartItem).subscribe(respCart => {
      console.log('Result of set cart item POST - service', JSON.stringify(respCart));
      this.cart = { ...respCart };
      localStorage.setItem('cartId', this.cart.id);
      this.refreshCart.next(this.cart);
    });
  }

  updateItemInCart(newCartItem: CartItem) {
    console.log('Update cart item in cart - service', newCartItem);
    let cartId = this.getValidCartId();
    console.log('cartItem updated', newCartItem);
    this.repo.setCartItem(cartId, newCartItem).subscribe(respCart => {
      console.log('Result of update cart item POST', JSON.stringify(respCart));
      this.cart = { ...respCart };
      localStorage.setItem('cartId', this.cart.id);
      this.refreshCart.next(this.cart);
    });
  }

  resetCart(): void {
    console.log('Resetting cart ...');
    this.repo.deleteCard(this.cart.id).subscribe(dr => {
      console.log('Response of delete card request', JSON.stringify(dr));
    });
    localStorage.removeItem('cartId');
    this.cart = new Cart('new');
    this.refreshCart.next(this.cart);
  }

}
