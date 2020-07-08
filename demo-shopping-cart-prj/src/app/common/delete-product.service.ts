import { tap, map } from 'rxjs/operators';
import { Product } from './../products/product.model';
import { Observable } from 'rxjs';
import { ConfirmDialogResult, ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { Router } from '@angular/router';
import { ProductService } from './product.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class DeleteProductService {

  constructor(
    private router: Router,
    private prodSvc: ProductService,
    private _snackBar: MatSnackBar,
    public dialog: MatDialog) { }

  openConfirmDialog(prod: Product): Observable<ConfirmDialogResult> {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '250px',
      data: {
        title: 'Confirm delete',
        promptMsg: `Delete product: ${prod.productName} ???`
      }
    });

    return dialogRef.afterClosed().pipe(
      tap(console.log),
      map(res => res === undefined ? ConfirmDialogResult.NO : res)
    );
  }

  onDeleteProduct(prod: Product) {
    this.openConfirmDialog(prod)
      .subscribe((res: ConfirmDialogResult) => {
        if (res === ConfirmDialogResult.YES) {
          console.log('User confirmed delete', res);
          this.prodSvc.deleteProduct(prod)
            .then(res => {
              this.openSnackBar(JSON.stringify(res), 'Product deleted');
              console.log('Reload page ...');
              this.router.navigate(['/prodscart']);
            })
            .catch(err => {
              this.openSnackBar(JSON.stringify(err), 'Product delete status');
            });
        }
      });
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 10000,
    });
  }

}
