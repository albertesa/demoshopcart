import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { AuthService } from './../common/auth.service';
@Injectable()
export class AuthInterceptorService implements HttpInterceptor {

  constructor(private authSvc: AuthService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('Intercepted request', req.url);
    if (this.authSvc.isAuthenticated()) {
      let modifiedReq: HttpRequest<any> = req.clone({
        setHeaders: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + this.authSvc.getToken(),
          'Authorization-User': this.authSvc.getUserName()
        }
      });
      console.log('Modified headers 1', JSON.stringify(modifiedReq.headers));
      return next.handle(modifiedReq);
    } else {
      return next.handle(req);
    }
  }

}
