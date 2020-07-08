import { Observable } from 'rxjs';
import { CanDeactivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

export interface CanComponentDeactivate {
  canDeactivate(currRoute: ActivatedRouteSnapshot,
    currState: RouterStateSnapshot,
    nextState?: RouterStateSnapshot
    ): Observable<boolean> | Promise<boolean> | boolean;
}

export class CanDeactivateGuard implements CanDeactivate<CanComponentDeactivate> {
  canDeactivate(
    component: CanComponentDeactivate,
    currRoute: ActivatedRouteSnapshot,
    currState: RouterStateSnapshot,
    nextState?: RouterStateSnapshot

  ): Observable<boolean> | Promise<boolean> | boolean {
    return component.canDeactivate(currRoute, currState, nextState);
  }
}
