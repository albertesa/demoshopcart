import { ConfigService } from './app-config.service';
import { Injectable } from '@angular/core';

import { Product } from '../products/product.model';
import { RepositoryService } from './repository.service';

import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class ProductService {

  private products: Product[] = [
    new Product('1', 'apples', 'Ambrosia apples', 'apples.jpg'),
    new Product('2', 'dummy', 'Dummy LEGO stuff', 'dummy.jpg')
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
    return new Promise((resolve, reject) => {
      this.repo.fetchProducts().subscribe(prods => {
        console.log('Fetched products', prods.length);
        this.products = prods.slice();
        resolve(this.products.slice());
      },
      err => {
        console.error('err', err);
        console.error(`Failed retrieve products from server: ${JSON.stringify(err)}`);
        reject(err);
      });
    });
  }

  getProduct(prodId: string): Product {
    let prod = this.products.find(p => p.id === prodId);
    if (prod) {
      return { ...prod };
    }
    // TODO - fetch from server and add to products
  }

  deleteProduct(prod: Product) {
    alert(`Delete product: ${prod.productName}`);
  }

  generateImageSrcUrl(prod: Product) {
    return `assets/images/${prod.productImg}`
    //return `${this.cfgSvc.getServer()}/image/${prod.productImg}`;
  }

}
