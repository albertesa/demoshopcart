import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { CookieService } from 'ngx-cookie-service';

import { AuthService } from './../common/auth.service';
@Injectable()
export class AuthInterceptorService implements HttpInterceptor {

  constructor(
    private authSvc: AuthService,
    private cookieSvc: CookieService
    ) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('Intercepted request', req.url);
    let hdrs = {};
    if (req.url.endsWith('/setprodwimg')) {
      //hdrs['Content-Type'] = 'multipart/form-data';
    } else {
      hdrs['Content-Type'] = 'application/json';
    }
    let xsrfCv = this.cookieSvc.get('SSBA-STOK');
    console.log('Adding xsrf header', xsrfCv);
    if (xsrfCv && xsrfCv.length > 0) {
      hdrs['H-SSBA-STOK'] = xsrfCv;
    }
    if (this.authSvc.isAuthenticated()) {
      hdrs['Authorization'] = 'Bearer ' + this.authSvc.getToken();
      hdrs['Authorization-User'] = this.authSvc.getUserName();
    }
    let modifiedReq: HttpRequest<any> = req.clone({
      withCredentials: true,
      setHeaders: hdrs
    });
    console.log('Modified headers 1', JSON.stringify(modifiedReq.headers));
    return next.handle(modifiedReq);
  }

}
