import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NotaService } from '../services/nota.service';
import { Nota } from '../interfaces/note.interfaces';
import { Profumo } from '../interfaces/profumo.interfaces';
import { NotaProfumiComponent, NotaProfumiDialogData } from '../dialogs/nota-profumi/nota-profumi.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-note',
  standalone: false,
  templateUrl: './note.component.html',
  styleUrls: ['./note.component.css']
})
export class NoteComponent implements OnInit {
  note: Nota[] = [];
  noteFiltrate: Nota[] = []; // 🔹 Array per le note filtrate
  searchText: string = ''; // 🔍 Testo della ricerca

  constructor(
    private notaService: NotaService,
    private dialog: MatDialog,
    private router: Router
  ) {}

  ngOnInit(): void {
    console.log("📌 Caricamento delle note...");

    this.notaService.getNote().subscribe({
      next: (data) => {
        this.note = data.sort((a, b) => a.nome.localeCompare(b.nome)); // ✅ Ordina alfabeticamente
        this.noteFiltrate = [...this.note]; // 🔹 Inizializza la lista filtrata
        console.log("✅ Note caricate con successo:", this.note);
      },
      error: (err) => console.log("❌ Errore nel caricamento delle note:", err)
    });
  }

  /** 🔍 Filtra le note in base alla barra di ricerca */
  filtraNote(): void {
    if (!this.searchText) {
      this.noteFiltrate = [...this.note]; // Se il campo è vuoto, mostra tutte le note
    } else {
      this.noteFiltrate = this.note.filter(n =>
        n.nome.toLowerCase().includes(this.searchText.toLowerCase())
      );
    }
  }

  // ✅ Ripristinati i metodi originali
  goBack(): void {
    this.router.navigate(['/']);
  }

  goHome(): void {
    this.router.navigate(['/']);
  }

  /** ✅ Metodo per aprire il dialog con i profumi della nota selezionata */
  openDialog(nota: Nota): void {
    if (!nota?.id) {
      console.warn("⚠️ ID della nota non definito. Impossibile aprire il dialog.");
      return;
    }

    console.log(`📌 Apertura dialog per la nota ID: ${nota.id} - Nome: ${nota.nome}`);

    this.notaService.getProfumiByNota(nota.id).subscribe({
      next: (profumi: Profumo[]) => {
        console.log(`✅ Profumi ricevuti per la nota "${nota.nome}":`, profumi);

        const dialogData: NotaProfumiDialogData = {
          profumi: profumi,
          selectedNota: nota
        };

        this.dialog.open(NotaProfumiComponent, {
          data: dialogData
        });
      },
      error: (err) => console.error(`❌ Errore nel recupero dei profumi per la nota "${nota.nome}":`, err)
    });
  }
}
