import { MatDialog } from '@angular/material/dialog';
import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { ProfumoService } from '../services/profumo.service';
import { NotaService } from '../services/nota.service';
import { Profumo } from '../interfaces/profumo.interfaces';
import { NotaTipoDTO } from '../interfaces/notaTipoDTO';

@Component({
  selector: 'app-crud-profumi',
  standalone: false,
  templateUrl: './crud-profumi.component.html',
  styleUrls: ['./crud-profumi.component.css']
})
export class CrudProfumiComponent implements OnInit {
  @ViewChild('deleteDialog') deleteDialog!: TemplateRef<any>;

  profumi: Profumo[] = [];
  profumiFiltrati: Profumo[] = []; // 🔍 Profumi filtrati
  noteDisponibili: NotaTipoDTO[] = [];
  profumo: Profumo = this.resetProfumo();
  idDaEliminare: number | null = null;
  searchText: string = ''; // 🔍 Testo della ricerca

  constructor(
    private profumoService: ProfumoService,
    private notaService: NotaService,
    public dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.caricaProfumiENote();
  }

  /** 🔹 Carica i profumi e le note, ordinandoli alfabeticamente */
  private caricaProfumiENote(): void {
    this.profumoService.getProfumi().subscribe({
      next: (data) => {
        console.log("📌 Profumi caricati:", data);
        this.ordinaProfumi(data);
      },
      error: (err) => console.error("❌ Errore nel caricamento dei profumi:", err)
    });

    this.notaService.getNoteConTipo().subscribe({
      next: (data) => {
        console.log("📌 Note caricate con tipo:", data);
        this.noteDisponibili = data;
      },
      error: (err) => console.error("❌ Errore nel caricamento delle note:", err)
    });
  }

  /** 🔹 Ordina i profumi alfabeticamente e aggiorna l'elenco filtrato */
  private ordinaProfumi(data: Profumo[]) {
    this.profumi = data.sort((a, b) => a.nome.localeCompare(b.nome));
    this.profumiFiltrati = [...this.profumi];
  }

  /** 🔍 Filtra i profumi in base alla ricerca */
  filtraProfumi(): void {
    if (!this.searchText) {
      this.profumiFiltrati = [...this.profumi];
    } else {
      this.profumiFiltrati = this.profumi.filter(p =>
        p.nome.toLowerCase().includes(this.searchText.toLowerCase())
      );
    }
  }

  /** 🔹 Aggiunge una nuova nota con tipo di default */
  aggiungiNota(): void {
    if (!this.profumo.note) {
      this.profumo.note = [];
    }
    this.profumo.note.push({ id: 0, nomeNota: 'Nuova Nota', tipoNotaId: 1, tipoNotaNome: 'Testa' }); 
  }

  /** 🔹 Rimuove una nota dall'array */
  rimuoviNota(index: number): void {
    if (this.profumo.note) {
      this.profumo.note.splice(index, 1);
    }
  }

  /** 🔹 Salva o aggiorna un profumo */
  salvaProfumo(): void {
    console.log("🔹 Salvando profumo:", this.profumo);

    if (this.profumo.id) {
      this.profumoService.updateProfumo(this.profumo.id, this.profumo).subscribe({
        next: () => {
          alert("✅ Profumo aggiornato con successo!");
          this.resetForm();
          this.caricaProfumiENote();
        },
        error: (err) => {
          console.error("❌ Errore nell'aggiornamento:", err);
          alert("❌ Errore nell'aggiornamento del profumo.");
        }
      });
    } else {
      this.profumoService.createProfumo(this.profumo).subscribe({
        next: () => {
          alert("✅ Profumo aggiunto con successo!");
          this.resetForm();
          this.caricaProfumiENote();
        },
        error: (err) => {
          console.error("❌ Errore nel salvataggio:", err);
          alert("❌ Errore nel salvataggio del profumo.");
        }
      });
    }
  }

 /** 🔹 Modifica un profumo esistente */
modificaProfumo(profumo: Profumo): void {
  console.log("🔹 Modifica profumo selezionato:", profumo);
  
  // Assegna il profumo selezionato
  this.profumo = { ...profumo };

  // Se il profumo ha delle note, mantieni l'array, altrimenti inizializzalo
  this.profumo.note = profumo.note ? [...profumo.note] : [];

  // Assicura che le note disponibili siano caricate
  if (this.noteDisponibili.length === 0) {
    this.notaService.getNoteConTipo().subscribe({
      next: (data) => {
        console.log("📌 Note disponibili caricate:", data);
        this.noteDisponibili = data;
      },
      error: (err) => console.error("❌ Errore nel caricamento delle note:", err)
    });
  }
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

  /** 🔹 Elimina un profumo */
  eliminaProfumo(): void {
    if (this.idDaEliminare !== null) {
      this.profumoService.deleteProfumo(this.idDaEliminare).subscribe({
        next: () => {
          alert('✅ Profumo eliminato con successo!');
          this.caricaProfumiENote();
          this.closeDialog();
        },
        error: (err) => console.error("❌ Errore nell'eliminazione del profumo:", err)
      });
    }
  }

  /** 🔹 Chiude il dialog */
  closeDialog(): void {
    this.dialog.closeAll();
    this.idDaEliminare = null;
  }

  /** 🔹 Resetta il form */
  resetForm(): void {
    this.profumo = this.resetProfumo();
  }

  /** 🔹 Crea un oggetto Profumo vuoto */
  resetProfumo(): Profumo {
    return {
      id: 0,
      nome: '',
      marca: '',
      descrizione: '',
      formato: '',
      prezzo: 0,
      immagineUrl: '',
      note: [] 
    };
  }
}
