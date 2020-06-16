import { Component, OnInit, OnDestroy } from '@angular/core';

import { Cart } from './cart.model';
import { CartService } from 'src/app/common/cart.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit, OnDestroy {

  cart: Cart;
  private refreshCartSubscription: Subscription;

  constructor(private cartService: CartService) { }

  ngOnInit(): void {
    this.cart = this.cartService.getCart();
    this.refreshCartSubscription = this.cartService.refreshCart.subscribe(cart => {
      this.cart = cart;
    });
  }

  resetCart(): void {
    this.cartService.resetCart();
  }

  ngOnDestroy() {
    this.refreshCartSubscription.unsubscribe();
  }

}
