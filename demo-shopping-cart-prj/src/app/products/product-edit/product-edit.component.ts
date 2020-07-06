import { ProductService } from './../../common/product.service';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { Component, OnInit } from '@angular/core';

import { Product } from './../product.model';

@Component({
  selector: 'app-product-edit',
  templateUrl: './product-edit.component.html',
  styleUrls: ['./product-edit.component.css']
})
export class ProductEditComponent implements OnInit {

  product: Product;
  prodId: string;
  prodName: string;
  prodDesc: string;
  prodImg: string;
  title: string = '';
  titleIconName: string = '';
  selectedFile: File;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private prodSvc: ProductService) { }

  ngOnInit(): void {
    this.setCurrentProduct(this.route.snapshot.params['id']);
    this.route.params.subscribe((params: Params) => {
      this.setCurrentProduct(params['id']);
    });
  }

  onFileChanged(event) {
    this.selectedFile = event.target.files[0]
    this.prodImg = this.selectedFile.name;
  }

  setCurrentProduct(prodId: string) {
    if (prodId === 'new') {
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

  onSubmit() {

  }

}

