import { MatSnackBar } from '@angular/material/snack-bar';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';

import { SignupResponseModel } from './signup-response.model';
import { AuthService } from 'src/app/common/auth.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit, OnDestroy {

  constructor(
    private router: Router,
    private authSvc: AuthService,
    private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
  }

  ngOnDestroy() {
  }

  onSubmit(form: NgForm) {
    console.log('form submitted', form);
    this.authSvc.signup(form.value.email, form.value.passwd)
      .then((sr: SignupResponseModel) => {
        console.log('Signup success', sr.status);
        this.openSnackBar('Succesfuly signed up. Please login.');
        this.router.navigate(['/login']);
      })
      .catch((sr: SignupResponseModel) => {
        console.error('Signup error', sr.status);
        this.openSnackBar(sr.status);
      });
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, 'Signup status', {
      duration: 10000,
    });
  }

}
