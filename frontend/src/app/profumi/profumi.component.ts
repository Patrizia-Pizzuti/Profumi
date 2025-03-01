import { Component, OnInit } from '@angular/core';
import { NotaService } from '../services/nota.service';
import { ProfumoService } from '../services/profumo.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Profumo } from '../interfaces/profumo.interfaces';

@Component({
  selector: 'app-profumi',
  standalone: false,
  templateUrl: './profumi.component.html',
  styleUrls: ['./profumi.component.css']
})
export class ProfumiComponent implements OnInit {
  profumi: Profumo[] = [];
  profumiFiltrati: Profumo[] = []; // 🔍 Profumi filtrati per nome o marca
  searchText: string = ''; // 🔍 Testo della ricerca
  notaId?: number;

  constructor(
    private notaService: NotaService,
    private profumoService: ProfumoService,
    private route: ActivatedRoute,
    public router: Router
  ) {}

  ngOnInit() {
    this.caricaProfumi();
  }

  /** 🔹 Carica tutti i profumi ordinati alfabeticamente */
  private caricaProfumi(): void {
    this.profumoService.getProfumi().subscribe({
      next: (data: Profumo[]) => {
        console.log("📌 Profumi ricevuti:", data);
        this.ordinaProfumi(data);
      },
      error: (err) => console.error("❌ Errore nel recupero dei profumi:", err)
    });
  }

  /** 🔹 Ordina i profumi alfabeticamente e aggiorna l'elenco filtrato */
  private ordinaProfumi(data: Profumo[]) {
    this.profumi = data.sort((a, b) => a.nome.localeCompare(b.nome));
    this.profumiFiltrati = [...this.profumi]; // Mantiene la lista originale ordinata
  }

  /** 🔍 Filtra i profumi SOLO per nome o marca */
  filtraProfumi(): void {
    if (!this.searchText) {
      this.profumiFiltrati = [...this.profumi]; // Mostra tutti i profumi se il campo è vuoto
    } else {
      this.profumiFiltrati = this.profumi
        .filter(p =>
          p.nome.toLowerCase().includes(this.searchText.toLowerCase()) ||
          p.marca.toLowerCase().includes(this.searchText.toLowerCase())
        )
        .sort((a, b) => a.nome.localeCompare(b.nome)); // Mantiene sempre l'ordine alfabetico
    }
  }

  /** 🔹 Cliccando su un profumo, apre i dettagli */
  viewProfumoDetails(id?: number): void {
    if (id === undefined) {
      console.error("❌ Errore: ID profumo non valido!");
      return;
    }
    this.router.navigate(['/profumo', id]);
  }
}
