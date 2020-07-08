import {Component, Inject} from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';

export interface DialogData {
  title: string;
  promptMsg: string;
}

export enum ConfirmDialogResult {
  YES, NO
}

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.css']
})
export class ConfirmDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

    onNo() {
      this.dialogRef.close(ConfirmDialogResult.NO);
    }

    onYes() {
      this.dialogRef.close(ConfirmDialogResult.YES);
    }

}
