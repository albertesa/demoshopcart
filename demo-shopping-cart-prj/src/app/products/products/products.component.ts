import { Component, OnInit } from '@angular/core';

import { Product } from '../product.model';
import { RepositoryService } from '../../common/repository.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

  isDisabled: boolean = true;
  products: Product[] = [];

  constructor(private repo: RepositoryService) { }

  ngOnInit(): void {
    this.repo.fetchProducts().subscribe(prods => {
      this.products = prods;
    });
  }

}
