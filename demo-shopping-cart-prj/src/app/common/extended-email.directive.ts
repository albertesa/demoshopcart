import { Directive } from '@angular/core';
import { NG_VALIDATORS, AbstractControl, ValidationErrors, Validator, FormControl, Validators } from '@angular/forms';

@Directive({
  selector: '[validEmail]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: ExtendedEmailValidatorDirective, multi: true }
  ]
})
export class ExtendedEmailValidatorDirective implements Validator {

  validate(c: FormControl): ValidationErrors | null {
    return ExtendedEmailValidatorDirective.validateEmail(c);
  }

  static validateEmail(control: FormControl): ValidationErrors | null {
    console.log('ExtendedEmailValidatorDirective', control.value);
    if (control.value == null) {
      return null;
    }
    return Validators.pattern(
      /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      )(control);
  }
}
