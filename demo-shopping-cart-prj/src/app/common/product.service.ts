import { Subject } from 'rxjs';
import { DeleteResponse } from './delete-response.model';
import { ConfigService } from './app-config.service';
import { Injectable } from '@angular/core';

import { Product } from '../products/product.model';
import { RepositoryService } from './repository.service';

import { AuthService } from './auth.service';
import { rejects } from 'assert';

@Injectable({ providedIn: 'root' })
export class ProductService {

  private products: Product[] = [
    //new Product('1', 'apples', 'Ambrosia apples', 'apples.jpg'),
    //new Product('2', 'dummy', 'Dummy LEGO stuff', 'dummy.jpg')
  ];

  subjProdsChanged: Subject<Product[]> = new Subject<Product[]>();

  constructor(
    private repo: RepositoryService,
    private authSvc: AuthService,
    private cfgSvc: ConfigService) {
  }

  submitProductWithImage(prod: Product, img: File): Promise<Product> {
    const uploadData = new FormData();
    uploadData.append('img', img);
    uploadData.append('id', prod.id);
    uploadData.append('productName', prod.productName);
    uploadData.append('productDesc', prod.productDesc);
    uploadData.append('productImg', prod.productImg);
    return new Promise<Product>((resolve, reject) => {
      this.repo.submitProductWithImage(uploadData).subscribe(
        (res: Product) => {
          let prods: Product[] = this.products.filter(p => p.id !== res.id);
          prods.push(res);
          this.products = [ ...prods ];
          this.subjProdsChanged.next([ ...this.products ]);
          resolve(res);
        },
        err => {
          reject(err);
        }
      );
    });
  }

  getProducts(refresh: boolean = true): Promise<Product[]> {
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

  deleteProduct(prod: Product): Promise<DeleteResponse> {
    if (confirm(`Delete product: ${prod.productName} ???`)) {
      return new Promise<DeleteResponse>((resolve, reject) => {
        this.repo.deleteProduct(prod.id).subscribe(
          res => {
            console.log('Result of product delete', res);
            let prods: Product[] = this.products.filter(p => p.id !== prod.id);
            this.products = [ ...prods ];
            this.subjProdsChanged.next([ ...this.products ]);
            resolve(res);
          },
          err => {
            reject(err);
          }
        );
      });
    }
    return Promise.reject('User canceled product delete request');
  }

  generateImageSrcUrl(prod: Product) {
    return `assets/images/${prod.productImg}`
    //return `${this.cfgSvc.getServer()}/image/${prod.productImg}`;
  }

}
