import { Observable } from 'rxjs';
import { CanComponentDeactivate } from './../../common/candeactivate-guard.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProductService } from './../../common/product.service';
import { Router, ActivatedRoute, Params, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Component, OnInit } from '@angular/core';

import { Product } from './../product.model';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-product-edit',
  templateUrl: './product-edit.component.html',
  styleUrls: ['./product-edit.component.css']
})
export class ProductEditComponent implements OnInit, CanComponentDeactivate {

  product: Product;
  prodId: string;
  prodName: string;
  prodDesc: string;
  prodImg: string;
  title: string = '';
  titleIconName: string = '';
  selectedFile: File;
  isAddNew: boolean = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private prodSvc: ProductService,
    private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.setCurrentProduct(this.route.snapshot.params['id']);
    this.route.params.subscribe((params: Params) => {
      this.setCurrentProduct(params['id']);
    });
  }

  canDeactivate(currRoute: ActivatedRouteSnapshot,
      currState: RouterStateSnapshot,
      nextState?: RouterStateSnapshot
      ): Observable<boolean> | Promise<boolean> | boolean {
      return confirm('Leave the page ???');
    }

  onFileChanged(event) {
    console.log('onFileChanged', event);
    this.selectedFile = event.target.files[0]
    this.prodImg = this.selectedFile.name;
  }

  setCurrentProduct(prodId: string) {
    if (prodId === 'new') {
      this.isAddNew = true;
      this.product = new Product('new', '', '', '');
      this.title = 'Add new product';
      this.titleIconName = 'playlist_add';
    } else {
      this.product = this.prodSvc.getProduct(prodId);
      this.title = 'Edit product';
      this.titleIconName = 'edit';
    }
    this.prodId = this.product.id;
    this.prodName = this.product.productName;
    this.prodDesc = this.product.productDesc;
    this.prodImg = this.product.productImg;
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, 'Product submission status', {
      duration: 10000,
    });
  }

  onCancel() {
    console.log('Edit product cancelled');
    if (this.isAddNew) {
      this.router.navigate(['/prodscart']);
    } else {
      this.router.navigate(['/viewprod', this.prodId]);
    }
  }

  onSubmit() {
    this.product.productName = this.prodName;
    this.product.productDesc = this.prodDesc;
    this.product.productName = this.prodName;
    this.product.productImg = this.prodImg;
    console.log('Submitted data', this.product);
    this.prodSvc.submitProductWithImage(this.product, this.selectedFile)
      .then(res => {
        console.log('Product submission response', JSON.stringify(res));
        this.openSnackBar(`Product submitted successfully`);
        this.router.navigate(['/viewprod', res.id]);
      })
      .catch(err => {
        this.openSnackBar(JSON.stringify(err));
      });
  }

}

