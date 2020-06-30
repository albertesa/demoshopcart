import { AuthEventModel } from './../auth-event.model';
import { Subscription } from 'rxjs';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { AuthService } from 'src/app/common/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {

  private authEventsSubscription: Subscription;

  constructor(
    private router: Router,
    private authSvc: AuthService,
    private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
  }

  processAuthEvent(authEvent: AuthEventModel) {
    console.log('authEvent', authEvent);
    if (authEvent.loggedIn) {
      this.router.navigate(['/prodscart']);
    } else {
      if (authEvent.errMsg !== 'logout_event') {
        this.openSnackBar(authEvent.errMsg);
      }
    }
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, 'Authentication error', {
      duration: 10000,
    });
  }

  ngOnDestroy() {
    if (this.authEventsSubscription) {
      this.authEventsSubscription.unsubscribe();
    }
  }

  onSubmit(form: NgForm) {
    console.log('form submitted', form);
    this.authSvc.login(form.value.email, form.value.passwd);
    this.authEventsSubscription = this.authSvc.authEvents.subscribe(
      t => {
        this.processAuthEvent(t);
      });
  }

}
