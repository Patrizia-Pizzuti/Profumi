import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProfumoService } from '../services/profumo.service';
import { Profumo } from '../interfaces/profumo.interfaces';
import { NotaTipoDTO } from '../interfaces/notaTipoDTO';

@Component({
  selector: 'app-profumi-dettaglio',
  standalone: false,
  templateUrl: './profumo-dettaglio.component.html',
  styleUrls: ['./profumo-dettaglio.component.css']
})
export class ProfumiDettaglioComponent implements OnInit {
  profumo?: Profumo;
  noteTesta: NotaTipoDTO[] = [];
  noteCuore: NotaTipoDTO[] = [];
  noteFondo: NotaTipoDTO[] = [];

  constructor(
    private route: ActivatedRoute,
    private profumoService: ProfumoService,
    public router: Router
  ) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!; // ✅ Conversione diretta in numero con "!"

    if (id) {
      this.profumoService.getProfumoById(id).subscribe({
        next: (data) => {
          console.log("📌 Profumo dettagli:", data);

          // ✅ Converti il percorso dell'immagine correggendo eventuali errori di formattazione
          if (data.immagineUrl) {
            data.immagineUrl = data.immagineUrl.replace(/\\/g, "/").replace(/^src\//, "");
          }

          this.profumo = data;
          console.log("🔹 URL immagine corretto:", this.profumo.immagineUrl);

          // ✅ Controllo di sicurezza per `note`
          if (data.note && Array.isArray(data.note)) {
            this.noteTesta = data.note.filter(nota => nota.tipoNotaId === 1);
            this.noteCuore = data.note.filter(nota => nota.tipoNotaId === 2);
            this.noteFondo = data.note.filter(nota => nota.tipoNotaId === 3);
          } else {
            console.warn("⚠️ Nessuna nota trovata per il profumo:", data);
          }
        },
        error: (err) => console.error("❌ Errore nel caricamento del profumo:", err)
      });
    }
  }
}
