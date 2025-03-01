import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { AuthResponse } from '../interfaces/auth-response.interface';
import { User } from '../interfaces/user.interfaces';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) {}

  // 📌 Metodo per il login
  login(username: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, { username, password });
  }

  // 📌 Metodo per la registrazione
  register(user: User): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, user);
  }

  // 📌 Salva i dati dell'utente nel localStorage
  saveUserData(authData: AuthResponse): void {
    if (authData.token) {
      localStorage.setItem('token', authData.token);
      localStorage.setItem('username', authData.username);
  
      try {
        // ✅ Salva i ruoli come ARRAY
        const rolesArray = Array.isArray(authData.roles) ? authData.roles : JSON.parse(authData.roles);
        localStorage.setItem('roles', JSON.stringify(rolesArray));
        console.log("Dati utente salvati:", authData);
      } catch (error) {
        console.error("Errore nel parsing dei ruoli:", error);
      }
    } else {
      console.error("Errore: token non ricevuto!");
    }
  }

  // 📌 Metodo per controllare se l'utente è autenticato
  isLoggedIn(): boolean {   
    return !!this.getToken();
  }

  // 📌 Metodo per ottenere il token
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // 📌 Metodo per ottenere il nome utente
  getUsername(): string | null {
    return localStorage.getItem('username');
  }

  // 📌 Metodo per ottenere i ruoli dell'utente
  getUserRoles(): string[] {
    const roles = localStorage.getItem('roles');
    try {
      return roles ? JSON.parse(roles) : [];
    } catch (error) {
      console.error("Errore nel parsing dei ruoli:", error);
      return [];
    }
  }

  // 📌 Controlla se l'utente ha un certo ruolo
  hasRole(role: string): boolean {   
    const roles = this.getUserRoles();
    console.log("Ruoli attuali:", roles); // ✅ Debug
    return roles.includes(role) || roles.includes("ROLE_SUPER_ADMIN");
  }

  // 📌 Metodo per il logout
  logout(): void {
    localStorage.clear();
  }

  // 📌 Metodo per ottenere tutti gli utenti
  getAllUsers(): Observable<{ username: string; roles: string[] }[]> {
    return this.http.get<{ username: string; roles: string[] }[]>(`${this.apiUrl}/users`);
  }

  // 📌 Metodo per aggiornare i ruoli di un utente
  updateUserRoles(username: string, roles: string[]): Observable<string> {
    return this.http.put<string>(`${this.apiUrl}/update-roles/${username}`, roles);
  }

  // 📌 Metodo per eliminare un utente
  deleteUser(username: string): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/delete-user/${username}`);
  }
}
