import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Subject, throwError, ReplaySubject } from 'rxjs';
import { ConfigService } from './app-config.service';
import { AuthEventModel } from './../auth/auth-event.model';
import { SignupResponseModel } from './../auth/signup/signup-response.model';
@Injectable({ providedIn: 'root' })
export class AuthService {

  authEvents: ReplaySubject<AuthEventModel> = new ReplaySubject<AuthEventModel>(1);
  private token: string;
  private email: string;

  constructor(
    private cfgSvc: ConfigService,
    private http: HttpClient,
    private router: Router) {
    this.email = localStorage.getItem('user');
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
    return this.email;
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
    this.email = null;
    this.token = null;
    this.notifyNotAuthenticated('logout_event');
    this.router.navigate(['/']);
  }

  login(email: string, passwd: string) {
    let server: string = this.cfgSvc.getServer();
    this.http
      .post<{token: string}>(`${server}/login`,
      {email: email, password: passwd},
      {withCredentials: true})
      .pipe(
        catchError(
          err => {
            let errMsg: string = this.handleError(err);
            this.notifyNotAuthenticated(errMsg);
            return throwError('Something bad happened; please try again later.');
          })
      ).subscribe(t => {
        this.email = email;
        this.token = t.token;
        console.log('login response', t);
        localStorage.setItem('user', this.email);
        localStorage.setItem('token', this.token);
        this.notifyAuthenticated();
      });
  }
  private handleError(error: HttpErrorResponse): string {
    console.log('error', JSON.stringify(error));
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
    console.log('post error', errMsg);
    return errMsg;
  };

  signup(email: string, passwd: string): Promise<SignupResponseModel> {
    let server: string = this.cfgSvc.getServer();
    return new Promise<SignupResponseModel>((resolve, reject) => {
        this.http
        .post<SignupResponseModel>(`${server}/signup`,
        {email: email, password: passwd},
        {withCredentials: true})
        .subscribe((sr: SignupResponseModel) => {
          console.log('Auth svc signup response', JSON.stringify(sr));
          if (sr.status !== 'success') {
            reject(sr);
            return;
          }
          resolve(sr);
        },
        err => {
          let errMsg = this.handleError(err);
          console.error('Auth svc signup error', errMsg);
          reject(new SignupResponseModel(email, errMsg));
        });
    });
  }

}
