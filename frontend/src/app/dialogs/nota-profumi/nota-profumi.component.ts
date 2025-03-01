import { Component, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogRef
} from '@angular/material/dialog';
import { Profumo } from '../../interfaces/profumo.interfaces';
import { Nota } from '../../interfaces/note.interfaces';
import { Router } from '@angular/router';

@Component({
  selector: 'app-nota-profumi',
  standalone: false,
  templateUrl: './nota-profumi.component.html',
  styleUrl: './nota-profumi.component.css'
})

export class NotaProfumiComponent {
  data: NotaProfumiDialogData = inject<NotaProfumiDialogData>(MAT_DIALOG_DATA);
  
  constructor(private dialogRef: MatDialogRef<NotaProfumiComponent>, private router: Router) {
  console.log("Dati ricevuti nel dialog:", this.data);}

  navigateToProfumo(profumo: Profumo) {
    if (profumo.id !== undefined) {
      this.dialogRef.close(); // ✅ Chiude il dialog prima di navigare
      this.router.navigate(['/profumo', profumo.id]); // ✅ Naviga alla pagina del profumo
    } else {
      console.error("Errore: profumo.id è undefined");
    }
  }}

export interface NotaProfumiDialogData {
  profumi: Profumo[];
  selectedNota: Nota;
}

export interface NotaProfumiDialogResult {
  id: number;
}