import { ConfigService } from './app-config.service';
import { Injectable } from '@angular/core';

import { Product } from '../products/product.model';
import { RepositoryService } from './repository.service';

import { AuthService } from './auth.service';
import { rejects } from 'assert';

@Injectable({ providedIn: 'root' })
export class ProductService {

  private products: Product[] = [

  ];

  constructor(
    private repo: RepositoryService,
    private authSvc: AuthService,
    private cfgSvc: ConfigService) {
  }

  getProducts(refresh: boolean = false): Promise<Product[]> {
    console.log('Fetching products');
    if (!refresh) {
      if (this.products.length > 0) {
        console.log('Return products from cache');
        return Promise.resolve(this.products);
      }
    }
    console.log('Retrieve products from server');
    return new Promise((resolve, refresh) => {
      this.repo.fetchProducts().subscribe(prods => {
        console.log('Fetched products', prods.length);
        this.products = prods.slice();
        resolve(this.products.slice());
      },
      err => {
        console.error(`Failed retrieve products fro server: ${JSON.stringify(err)}`);
        rejects(err);
      });
    });
  }

  getProduct(prodId: string): Product {
    return new Product(prodId, 'dummy', 'dummy desc', 'dummy.jpg');
  }

  deleteProduct(prod: Product) {
    alert(`Delete product: ${Product.name}`);
  }

  generateImageSrcUrl(prod: Product) {
    return `${this.cfgSvc.getServer()}/image/${prod.productImg}`;
  }

}
