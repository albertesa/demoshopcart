import { CartService } from './../../common/cart.service';
import { RepositoryService } from './../../common/repository.service';
import { Component, OnInit, Input } from '@angular/core';
import { CartItem } from './cart-item.model';

@Component({
  selector: 'app-cart-item',
  templateUrl: './cart-item.component.html',
  styleUrls: ['./cart-item.component.css']
})
export class CartItemComponent implements OnInit {

  @Input() cartItem: CartItem;

  constructor(private cartSvc: CartService) { }

  ngOnInit(): void {
  }

  updateCartItem(newAmount: number) {
    const newCartItem = { ...this.cartItem };
    newCartItem.numOfItems = newAmount;
    this.cartSvc.updateItemInCart(newCartItem);
  }

}
