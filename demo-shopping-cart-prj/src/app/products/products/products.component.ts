import { Component, OnInit } from '@angular/core';

import { Product } from '../product.model';
import { RepositoryService } from '../../common/repository.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

  productId: string = 'tomatoes';
  productName: string = 'tomatoes';
  productDesc: string = 'tomatoes';
  productImg: string = 'tomatoes';

  isDisabled: boolean = true;
  products: Product[] = [];

  constructor(private repo: RepositoryService) { }

  ngOnInit(): void {
    this.repo.fetchProducts().subscribe(prods => {
      this.products = prods;
    });
  }

  changeProductName() {
    this.productName = 'potatoes';
  }

  addProductName() {
    this.products.push(new Product(
      this.productId, this.productName, this.productDesc, this.productImg));
  }

  enableChangeProductName() {
    this.isDisabled = false;
  }

}
