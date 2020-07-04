import { ConfigService } from './../../common/app-config.service';
import { AppConfig } from './../../common/app-config.model';
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

  constructor(private cartService: CartService, private cfgSvc: ConfigService) { }

  ngOnInit(): void {
  }

  onClick() {
    this.cartService.addProductToCart(this.product);
  }

  generateLink(): string {
    return `${this.cfgSvc.getServer()}/image/${this.product.productImg}`;
  }

}
