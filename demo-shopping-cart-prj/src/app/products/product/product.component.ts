import { Component, OnInit, Input } from '@angular/core';
import { Product } from '../product.model';
import { CartService } from 'src/app/common/cart.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {

  @Input() product: Product;

  constructor(private cartService: CartService) { }

  ngOnInit(): void {
  }

  onClick() {
    this.cartService.addProductToCart(this.product);
  }

}
