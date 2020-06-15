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

  getValidCartId() : string {
    let cartId = localStorage.getItem('cartId');
    if (!cartId || cartId === 'null' || cartId.length == 0) {
      console.log('Cart ID is not defined, set to "new"');
      cartId = 'new';
    }
    return cartId;
  }
  ngOnInit(): void {
    let cartId = this.getValidCartId();
    this.cart = new Cart(cartId);
    if (cartId !== 'new') {
      this.repo.fetchCart(cartId).subscribe(respCart => {
        console.log('Downloaded existing cart', JSON.stringify(respCart));
        this.cart = { ...respCart };
      });
    }
    this.cartService.addCartItemEvent.subscribe(cartItem => {
      cartId = this.getValidCartId();
      const foundCi = this.cart.items.filter(ci => ci.productId === cartItem.productId);
      if (foundCi.length > 0) {
        cartItem.numOfItems = foundCi[0].numOfItems + cartItem.numOfItems;
      }
      console.log('cartItem added', cartItem);
      this.repo.setCartItem(cartId, cartItem).subscribe(respCart => {
        console.log('Result of set cart item POST', JSON.stringify(respCart));
        this.cart = { ...respCart };
        localStorage.setItem('cartId', this.cart.id);
      });
    });

    this.cartService.updateCartItemEvent.subscribe(cartItem => {
      cartId = this.getValidCartId();
      console.log('cartItem updated', cartItem);
      this.repo.setCartItem(cartId, cartItem).subscribe(respCart => {
        console.log('Result of update cart item POST', JSON.stringify(respCart));
        this.cart = { ...respCart };
        localStorage.setItem('cartId', this.cart.id);
      });
    });
  }

  resetCart(): void {
    console.log('Resetting cart ...');
    this.repo.deleteCard(this.cart.id).subscribe(dr => {
      console.log('Response of delete card request', JSON.stringify(dr));
    });
    localStorage.removeItem('cartId');
    this.cart = new Cart('new');
  }

}
