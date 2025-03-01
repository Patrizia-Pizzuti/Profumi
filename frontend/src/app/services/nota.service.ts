import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, map } from 'rxjs';
import { Nota } from '../interfaces/note.interfaces';
import { Profumo } from '../interfaces/profumo.interfaces';
import { NotaTipoDTO } from '../interfaces/notaTipoDTO';
import { NotaCreateDTO } from '../interfaces/notaCreateDTO';

@Injectable({
  providedIn: 'root'
})
export class NotaService {
  private apiUrl = 'http://localhost:8080/note';
  private tipoNotaApiUrl = 'http://localhost:8080/note';

  constructor(private http: HttpClient) {}

  /** 🔹 Ottiene tutte le note */
  getNote(): Observable<Nota[]> {
    return this.http.get<Nota[]>(this.apiUrl);
  }

  /** 🔹 Ottiene tutti i tipi di nota */
  getTipiNota(): Observable<any[]> {
    return this.http.get<any[]>(this.tipoNotaApiUrl);
  }

  /** 🔹 Ottiene tutte le note con il nome del tipo */
  getNoteConTipo(): Observable<NotaTipoDTO[]> {
    return forkJoin({
      note: this.getNote(),
      tipiNota: this.getTipiNota()
    }).pipe(
      map(({ note, tipiNota }) =>
        note.map(n => ({
          id: n.id,
          nomeNota: n.nome,
          tipoNotaId: n.tipoNotaId ?? null, // ✅ Se `null`, assegna `null`
          tipoNotaNome: this.getTipoNotaNome(n.tipoNotaId ?? null, tipiNota) // ✅ Se `null`, passa `null`
        }))
      )
    );
  }

  /** 🔹 Ottiene il nome del tipo di nota */
  private getTipoNotaNome(tipoNotaId: number | null, tipiNota: any[]): string {
    if (tipoNotaId === null) return "Sconosciuto"; // ✅ Se `null`, restituisce "Sconosciuto"
    const tipo = tipiNota.find(t => t.id === tipoNotaId);
    return tipo ? tipo.nome : "Sconosciuto";
  }

  /** 🔹 Ottiene una nota per ID */
  getNotaById(id: number): Observable<Nota> {
    return this.http.get<Nota>(`${this.apiUrl}/${id}`);
  }

  /** 🔹 Crea una nuova nota (usa `NotaCreateDTO` con solo il nome) */
  createNota(nota: NotaCreateDTO): Observable<Nota> {
    return this.http.post<Nota>(this.apiUrl, nota);
  }

  /** 🔹 Aggiorna una nota esistente */
  updateNota(id: number, nota: Nota): Observable<Nota> {
    return this.http.put<Nota>(`${this.apiUrl}/${id}`, nota);
  }

  /** 🔹 Elimina una nota */
  deleteNota(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /** 🔹 Ottiene tutti i profumi associati a una nota */
  getProfumiByNota(id: number): Observable<Profumo[]> {
    return this.http.get<Profumo[]>(`${this.apiUrl}/profumi/${id}`);
  }

  /** 🔹 Ottiene i profumi filtrati per Nota e TipoNota */
  getProfumiByNotaETipo(id: number, idTipoNota: number): Observable<Profumo[]> {
    return this.http.get<Profumo[]>(`${this.apiUrl}/tipo/${id}/${idTipoNota}`);
  }
}
