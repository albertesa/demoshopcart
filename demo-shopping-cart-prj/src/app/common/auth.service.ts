import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Subject, throwError, ReplaySubject } from 'rxjs';
import { ConfigService } from './app-config.service';
import { AuthEventModel } from './../auth/auth-event.model';
@Injectable({ providedIn: 'root' })
export class AuthService {

  authEvents: ReplaySubject<AuthEventModel> = new ReplaySubject<AuthEventModel>(1);
  private token: string;
  private username: string;

  constructor(
    private cfgSvc: ConfigService,
    private http: HttpClient,
    private router: Router) {
    this.username = localStorage.getItem('user');
    this.token = localStorage.getItem('token');
    if (this.isAuthenticated()) {
      this.notifyAuthenticated();
    }
  }

  notifyAuthenticated() {
    this.authEvents.next(new AuthEventModel(true));
  }

  notifyNotAuthenticated(errMsg: string = null) {
    this.authEvents.next(new AuthEventModel(false, errMsg));
  }

  getToken(): string {
    return this.token;
  }

  getUserName(): string {
    return this.username;
  }

  isAuthenticated(): boolean {
    return this.isValidToken();
  }
  isValidToken(): boolean {
    return this.token != undefined && this.token.length > 0;
  }

  logout() {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    this.username = null;
    this.token = null;
    this.notifyNotAuthenticated('logout_event');
    this.router.navigate(['/']);
  }

  login(uname: string, passwd: string) {
    let server: string = this.cfgSvc.getServer();
    this.http
      .post<{token: string}>(`${server}/login`,
      {username: uname, password: passwd},
      {withCredentials: true})
      .pipe(
        catchError(
          err => {
            this.handleError(err);
            return throwError('Something bad happened; please try again later.');
          })
      ).subscribe(t => {
        this.username = uname;
        this.token = t.token;
        console.log('login response', t);
        localStorage.setItem('user', this.username);
        localStorage.setItem('token', this.token);
        this.notifyAuthenticated();
      });
  }
  private handleError(error: HttpErrorResponse) {
    let errMsg: string = '';
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      errMsg = error.error.message;
      console.error('An error occurred:', errMsg);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      errMsg = JSON.stringify(error);
      console.log('Raw login HTTP error', errMsg);
      if (error.error.message) {
        errMsg = error.error.message;
      }
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${JSON.stringify(errMsg)}`);
    }
    console.log('login error', errMsg);
    this.notifyNotAuthenticated(errMsg);
  };

}
