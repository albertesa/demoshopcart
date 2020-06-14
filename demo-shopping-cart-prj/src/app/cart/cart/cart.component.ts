import { Component, OnInit } from '@angular/core';

import { Cart } from './cart.model';
import { CartService } from 'src/app/common/cart.service';
import { RepositoryService } from 'src/app/common/repository.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  cart: Cart;

  constructor(private cartService: CartService, private repo: RepositoryService) { }

  getValidCartItem() : string {
    let cartId = localStorage.getItem('cartId');
    if (!cartId || cartId === 'null' || cartId.length == 0) {
      console.log('Cart ID is not defined, set to "new"');
      cartId = 'new';
    }
    return cartId;
  }
  ngOnInit(): void {
    let cartId = this.getValidCartItem();
    this.cart = new Cart(cartId);
    if (cartId !== 'new') {
      this.repo.fetchCart(cartId).subscribe(respCart => {
        console.log('Downloaded existing cart', JSON.stringify(respCart));
        this.cart = { ...respCart };
      });
    }

    this.cartService.addCartItemEvent.subscribe(cartItem => {
      cartId = this.getValidCartItem();
      console.log('cartItem added', cartItem);
      this.repo.setCartItem(cartId, cartItem).subscribe(respCart => {
        console.log('Result of set cart item POST', JSON.stringify(respCart));
        this.cart = { ...respCart };
        localStorage.setItem('cartId', this.cart.id);
      });
    });
  }

  resetCart(): void {
    console.log('Resetting cart ...');
    localStorage.removeItem('cartId');
    this.cart = new Cart('new');
  }

}
