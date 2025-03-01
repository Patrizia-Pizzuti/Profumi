import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NotaService } from '../services/nota.service';
import { Nota } from '../interfaces/note.interfaces';
import { NotaTipoDTO } from '../interfaces/notaTipoDTO';
import { NotaCreateDTO } from '../interfaces/notaCreateDTO'; 
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-note-crud',
  standalone: false,
  templateUrl: './note-crud.component.html',
  styleUrls: ['./note-crud.component.css']
})
export class NoteCrudComponent implements OnInit {
  @ViewChild('deleteDialog') deleteDialog!: TemplateRef<any>;

  note: NotaTipoDTO[] = [];
  noteFiltrate: NotaTipoDTO[] = [];
  nota: Nota = this.resetNota();
  idDaEliminare: number | null = null;
  searchText: string = '';

  constructor(
    private notaService: NotaService,
    public dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.caricaNote();
  }

  /** 🔹 Carica tutte le note e le ordina alfabeticamente */
  private caricaNote(): void {
    this.notaService.getNoteConTipo().subscribe({
      next: (data) => {
        console.log("📌 Note caricate:", data);
        this.note = data.sort((a, b) => a.nomeNota.localeCompare(b.nomeNota));
        this.noteFiltrate = [...this.note];
      },
      error: (err) => console.error("❌ Errore nel caricamento delle note:", err)
    });
  }

  /** 🔍 Filtra le note in base alla barra di ricerca */
  filtraNote(): void {
    if (!this.searchText) {
      this.noteFiltrate = [...this.note];
    } else {
      this.noteFiltrate = this.note.filter(n =>
        n.nomeNota.toLowerCase().includes(this.searchText.toLowerCase())
      );
    }
  }

  /** 🔹 Salva o aggiorna una nota */
  salvaNota(): void {
    console.log("🔹 Salvando nota:", this.nota);

    if (this.nota.id) {
      this.notaService.updateNota(this.nota.id, this.nota).subscribe({
        next: () => {
          this.snackBar.open('✅ Nota aggiornata con successo!', 'OK', { duration: 3000 });
          this.resetForm();
          this.caricaNote();
        },
        error: (err) => {
          console.error("❌ Errore nell'aggiornamento:", err);
          this.snackBar.open("❌ Errore nell'aggiornamento della nota.", 'OK', { duration: 3000 });
        }
      });
    } else {
      const nuovaNota: NotaCreateDTO = { nome: this.nota.nome };

      this.notaService.createNota(nuovaNota).subscribe({
        next: () => {
          this.snackBar.open('✅ Nota aggiunta con successo!', 'OK', { duration: 3000 });
          this.resetForm();
          this.caricaNote();
        },
        error: (err) => {
          console.error("❌ Errore nel salvataggio:", err);
          this.snackBar.open("❌ Errore nel salvataggio della nota.", 'OK', { duration: 3000 });
        }
      });
    }
  }

  /** 🔹 Modifica una nota esistente */
  modificaNota(nota: NotaTipoDTO): void {
    console.log("🔹 Modifica nota selezionata:", nota);
    this.nota = { id: nota.id, nome: nota.nomeNota, tipoNotaId: 1 };
  }

  /** 🔹 Apre il dialog per la conferma dell'eliminazione */
  openDeleteDialog(id?: number): void {
    if (id === undefined) {
      console.error("❌ Errore: ID non valido per l'eliminazione!");
      return;
    }
    this.idDaEliminare = id;
    this.dialog.open(this.deleteDialog, { width: '300px' });
  }

  /** 🔹 Elimina una nota */
  eliminaNota(): void {
    if (this.idDaEliminare !== null) {
      this.notaService.deleteNota(this.idDaEliminare).subscribe({
        next: () => {
          console.log("✅ Nota eliminata con successo!");
          this.snackBar.open('✅ Nota eliminata con successo!', 'OK', { duration: 3000 });

          this.note = this.note.filter(n => n.id !== this.idDaEliminare);
          this.noteFiltrate = this.noteFiltrate.filter(n => n.id !== this.idDaEliminare);

          this.closeDialog();
        },
        error: (err) => {
          console.error("❌ Errore nell'eliminazione della nota:", err);
          this.snackBar.open(`❌ Errore: ${err.error?.message || 'Errore nell\'eliminazione della nota!'}`, 'OK', { duration: 3000 });
        }
      });
    } else {
      console.error("⚠️ Errore: `idDaEliminare` è NULL!");
    }
  }

  /** 🔹 Chiude il dialog */
  closeDialog(): void {
    this.dialog.closeAll();
    this.idDaEliminare = null;
  }

  /** 🔹 Resetta il form */
  resetForm(): void {
    this.nota = this.resetNota();
  }

  /** 🔹 Crea un oggetto Nota vuoto */
  resetNota(): Nota {
    return {
      id: 0,
      nome: '',
      tipoNotaId: null
    };
  }
}
