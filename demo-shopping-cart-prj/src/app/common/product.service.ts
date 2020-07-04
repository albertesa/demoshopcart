import { ConfigService } from './app-config.service';
import { Injectable } from '@angular/core';

import { Product } from '../products/product.model';
import { RepositoryService } from './repository.service';

import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class ProductService {

  constructor(
    private repo: RepositoryService,
    private authSvc: AuthService,
    private cfgSvc: ConfigService) {
  }

  getProduct(prodId: string): Product {
    return new Product(prodId, 'dummy', 'dummy desc', 'dummy.jpg');
  }

  generateImageSrcUrl(prod: Product) {
    return `${this.cfgSvc.getServer()}/image/${prod.productImg}`;
  }

}
