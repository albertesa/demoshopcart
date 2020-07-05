import { ProductService } from './../../common/product.service';
import { Component, OnInit } from '@angular/core';

import { Product } from '../product.model';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

  isDisabled: boolean = true;
  products: Product[] = [];

  constructor(
    private prodSvc: ProductService) { }

  ngOnInit(): void {
    this.fetchProducts();
  }

  fetchProducts() {
    this.prodSvc.getProducts()
      .then(prods => {
        this.products = prods;
      })
      .catch(err => {
        throw new Error(`Failed to load products: ${JSON.stringify(err)}`);
      });
  }

}
