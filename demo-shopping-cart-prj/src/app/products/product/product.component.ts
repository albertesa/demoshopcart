import { DeleteProductService } from './../../common/delete-product.service';
import { ProductService } from './../../common/product.service';
import { ConfigService } from './../../common/app-config.service';
import { Component, OnInit, Input } from '@angular/core';
import { Product } from '../product.model';
import { CartService } from 'src/app/common/cart.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {

  @Input() product: Product;

  constructor(
    private cartService: CartService,
    private cfgSvc: ConfigService,
    private router: Router,
    private prodSvc: ProductService,
    private delProdSvc: DeleteProductService) { }

  ngOnInit(): void {
  }

  onAddToCart() {
    this.cartService.addProductToCart(this.product);
  }

  onViewDetails() {
    this.router.navigate(['/viewprod', this.product.id]);
  }

  onEditDetails() {
    this.router.navigate(['/editprod', this.product.id]);
  }

  onDeleteProduct() {
    this.delProdSvc.onDeleteProduct(this.product);
  }

  generateLink(): string {
    return this.prodSvc.generateImageSrcUrl(this.product);
  }

}
