import { Component, OnInit } from '@angular/core';

import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-manage-roles',
  standalone:false,
  templateUrl: './manage-roles.component.html',
  styleUrls: ['./manage-roles.component.css']
})
export class ManageRolesComponent implements OnInit {
  users: { username: string; roles: string[] }[] = [];
  filteredUsers: { username: string; roles: string[] }[] = [];
  selectedRoles: { [key: string]: string[] } = {};
  availableRoles = ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN'];
  searchText: string = '';
  selectedRoleFilter: string = '';

  constructor(
    private authService: AuthService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.authService.getAllUsers().subscribe(
      (users) => {
        this.users = users;
        this.filteredUsers = users; // Inizializza la lista filtrata
        this.users.forEach(user => {
          this.selectedRoles[user.username] = [...user.roles];
        });
      },
      (error) => {
        this.snackBar.open('Errore nel caricamento utenti: ' + error.error, 'OK', { duration: 3000 });
      }
    );
  }

  // 📌 Metodo per filtrare utenti in base alla barra di ricerca
  filterUsers(): void {
    this.filteredUsers = this.users.filter(user =>
      user.username.toLowerCase().includes(this.searchText.toLowerCase())
    );
    this.applyRoleFilter(); // Mantiene anche il filtro dei ruoli
  }

  // 📌 Metodo per filtrare utenti in base al ruolo selezionato
  applyRoleFilter(): void {
    if (this.selectedRoleFilter) {
      this.filteredUsers = this.filteredUsers.filter(user =>
        user.roles.includes(this.selectedRoleFilter)
      );
    }
  }

  // 📌 Reset dei filtri
  resetFilters(): void {
    this.searchText = '';
    this.selectedRoleFilter = '';
    this.filteredUsers = this.users;
  }

  updateRoles(username: string): void {
    const newRoles = this.selectedRoles[username] || [];
    this.authService.updateUserRoles(username, newRoles).subscribe(
      (response: any) => {
        const message = response?.message || '✅ Ruoli aggiornati con successo';
        this.snackBar.open(message, 'OK', { duration: 3000 });
        this.loadUsers();
      },
      (error) => {
        console.error('Errore aggiornamento ruoli:', error); // Debug
        const errorMessage = error.error?.message || error.message || '❌ Errore sconosciuto';
        this.snackBar.open(errorMessage, 'OK', { duration: 3000 });
      }
    );
  }
  

  // 📌 Eliminazione con conferma
  deleteUser(username: string): void {
    if (window.confirm(`Sei sicuro di voler eliminare l'utente ${username}?`)) {
      this.authService.deleteUser(username).subscribe(
        (response: any) => {
          const message = response?.message || '✅ Utente eliminato con successo';
          this.snackBar.open(message, 'OK', { duration: 3000 });
          this.loadUsers();
        },
        (error) => {
          console.error('Errore eliminazione utente:', error); // Debug
          const errorMessage = error.error?.message || error.message || '❌ Errore sconosciuto';
          this.snackBar.open(errorMessage, 'OK', { duration: 3000 });
        }
      );
    }
  }
  
}
