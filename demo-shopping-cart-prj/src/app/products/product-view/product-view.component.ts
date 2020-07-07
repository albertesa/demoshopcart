import { MatSnackBar } from '@angular/material/snack-bar';
import { ProductService } from './../../common/product.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';

import { Product } from './../product.model';

@Component({
  selector: 'app-product-view',
  templateUrl: './product-view.component.html',
  styleUrls: ['./product-view.component.css']
})
export class ProductViewComponent implements OnInit {

  product: Product;
  prodId: string = 'new';
  prodImgSrc: string = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private prodSvc: ProductService,
    private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.prodId = this.route.snapshot.params['id'];
    this.product = this.prodSvc.getProduct(this.prodId);
    this.prodImgSrc = this.prodSvc.generateImageSrcUrl(this.product);
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 10000,
    });
  }

  onDeleteProduct() {
    if (confirm(`Delete product: ${this.product.productName} ???`)) {
      this.prodSvc.deleteProduct(this.product)
      .then(res => {
        this.openSnackBar(JSON.stringify(res), 'Product deleted');
        console.log('Navigate away ...');
        this.router.navigate(['/prodscart']);
      })
      .catch(err => {
        this.openSnackBar(JSON.stringify(err), 'Product delete status');
      });
    }
  }

}
