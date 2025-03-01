import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Profumo } from '../interfaces/profumo.interfaces';

@Injectable({
  providedIn: 'root'
})
export class ProfumoService {
  private apiUrl = 'http://localhost:8080/profumi';

  constructor(private http: HttpClient) {}

  /** ✅ Recupera tutti i profumi */
  getProfumi(): Observable<Profumo[]> {
    return this.http.get<Profumo[]>(this.apiUrl);
  }

  /** ✅ Recupera un profumo per ID */
  getProfumoById(id: number): Observable<Profumo> {
    return this.http.get<Profumo>(`${this.apiUrl}/${id}`);
  }

  /** ✅ Crea un nuovo profumo (corretto l'endpoint) */
  createProfumo(profumo: Omit<Profumo, 'id'>): Observable<Profumo> {
    return this.http.post<Profumo>(this.apiUrl, profumo); //
  }

  /** ✅ Aggiorna un profumo */
  updateProfumo(id: number, profumo: Profumo): Observable<Profumo> {
    return this.http.put<Profumo>(`${this.apiUrl}/${id}`, profumo);
  }

  /** ✅ Elimina un profumo */
  deleteProfumo(id: number): Observable<void> {
    console.log(`🔹 Chiamata API DELETE per ID: ${id}`);
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /** ✅ Recupera i profumi che contengono una determinata nota */
  getProfumiByNota(notaId: number): Observable<Profumo[]> {
    return this.http.get<Profumo[]>(`${this.apiUrl}/nota/${notaId}`); // 🔹 FIX: corretto endpoint
  }
}
