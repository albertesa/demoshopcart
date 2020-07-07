import { DeleteResponse } from './delete-response.model';
import { UpdateResponse } from './update-response.model';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';


import { Cart } from './../cart/cart/cart.model';
import { CartItem } from './../cart/cart-item/cart-item.model';
import { Product } from '../products/product.model';
import { ConfigService } from './app-config.service';


@Injectable({providedIn: 'root'})
export class RepositoryService {

  private server: string;

  constructor(
    private http: HttpClient,
    private cfgSvc: ConfigService) {
      this.server = this.cfgSvc.getServer();
  }

  submitProductWithImage(formData: FormData) {
    console.log('formData', formData);
    return this.http
      .post<Product>(`${this.server}/product/setprodwimg`, formData);
  }

  deleteProduct(prodId: string) {
    return this.http
      .post<DeleteResponse>(`${this.server}/product/${prodId}/deleteprod`, {})
      .pipe(
        catchError(this.handleError)
      );
  }

  resetCart(cartId: string) {
    return this.http
      .post<UpdateResponse>(`${this.server}/cart/${cartId}/resetcart`, {})
      .pipe(
        catchError(this.handleError)
      );
  }

  setCartItem(cartId: string, cartItem: CartItem) {
    return this.http
      .post<Cart>(`${this.server}/cart/${cartId}/additem`, cartItem)
      .pipe(
        catchError(this.handleError)
      );
  }

  fetchCart(cartId: string) {
    return this.http
      .get<Cart>(`${this.server}/cart/${cartId}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  fetchProducts() {
    return this.http
      .get<Product[]>(`${this.server}/product/list`)
      .pipe(
        catchError(this.handleError)
      );
  }
  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${JSON.stringify(error.error)}`);
    }
    // return an observable with a user-facing error message
    return throwError(
      'Something bad happened; please try again later.');
  };

}
