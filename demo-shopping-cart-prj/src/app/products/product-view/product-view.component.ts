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
    private prodSvc: ProductService) { }

  ngOnInit(): void {
    this.prodId = this.route.snapshot.params['id'];
    this.product = this.prodSvc.getProduct(this.prodId);
    this.prodImgSrc = this.prodSvc.generateImageSrcUrl(this.product);
  }

}
