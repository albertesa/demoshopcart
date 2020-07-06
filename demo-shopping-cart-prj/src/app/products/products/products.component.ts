import { MatSnackBar } from '@angular/material/snack-bar';
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
    private prodSvc: ProductService,
    private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.fetchProducts();
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, 'Error loading products', {
      duration: 10000,
    });
  }

  fetchProducts() {
    this.prodSvc.getProducts()
      .then(prods => {
        this.products = prods;
      })
      .catch(err => {
        console.error('prods component err', err);
        this.openSnackBar(`Failed to load products: ${JSON.stringify(err)}`);
      });
  }

}
