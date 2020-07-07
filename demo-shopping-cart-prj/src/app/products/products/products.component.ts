import { Subscription } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProductService } from './../../common/product.service';
import { Component, OnInit, OnDestroy } from '@angular/core';

import { Product } from '../product.model';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit, OnDestroy {

  isDisabled: boolean = true;
  products: Product[] = [];
  private subscriptionProdsChanged: Subscription;

  constructor(
    private prodSvc: ProductService,
    private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.fetchProducts();
    this.subscriptionProdsChanged = this.prodSvc.subjProdsChanged.subscribe(
        (prods: Product[]) => {
          this.products = prods;
        });
  }

  ngOnDestroy() {
    this.subscriptionProdsChanged.unsubscribe();
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
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
        this.openSnackBar(
          `Failed to load products: ${JSON.stringify(err)}`,
          'Error loading products');
      });
  }

}
