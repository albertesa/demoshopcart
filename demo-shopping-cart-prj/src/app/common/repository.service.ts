import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Cart } from './../cart/cart/cart.model';
import { CartItem } from './../cart/cart-item/cart-item.model';
import { Product } from '../products/product.model';
import { throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { DeleteResponse } from './delete-response.model';

@Injectable({providedIn: 'root'})
export class RepositoryService {

  constructor(private http: HttpClient) {}

  deleteCard(cartId: string) {
    return this.http
      .post<DeleteResponse>(`http://localhost:8080/cart/${cartId}/deletecart`, {withCredentials: true})
      .pipe(
        catchError(this.handleError)
      );
  }

  setCartItem(cartId: string, cartItem: CartItem) {
    return this.http
      .post<Cart>(`http://localhost:8080/cart/${cartId}/additem`, cartItem, {withCredentials: true})
      .pipe(
        catchError(this.handleError)
      );
  }

  fetchCart(cartId: string) {
    return this.http
      .get<Cart>(`http://localhost:8080/cart/${cartId}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  fetchProducts() {
    return this.http
      .get<Product[]>('http://localhost:8080/product/list', {withCredentials: true})
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
