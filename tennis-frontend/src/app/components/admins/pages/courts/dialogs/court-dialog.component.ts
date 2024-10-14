import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
    standalone: true,
    selector: 'app-court-dialog',
    templateUrl: './court-dialog.component.html',
    styleUrls: ['./court-dialog.component.scss'],
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatIconModule,    
        MatDialogModule,  
      ],
      providers: []
})
export class CourtDialogComponent {
  createCourtGroup: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<CourtDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.createCourtGroup = this.fb.group({
      name: [data?.name || '', Validators.required],
      description: [data?.description || ''],
      address: [data?.address || ''],
      imageUrl: [data?.imageUrl || ''],
    });
  }

  onCreateSubmit(): void {
    if (this.createCourtGroup.valid) {
      this.dialogRef.close(this.createCourtGroup.value);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}