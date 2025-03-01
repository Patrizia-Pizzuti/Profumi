import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
  

    // TODO : Implementare quando c'è il token corretto sul backend
    if (this.authService.getUserRoles().includes('ROLE_ADMIN') || this.authService.getUserRoles().includes('ROLE_SUPER_ADMIN')) {
      return true;
     }
     this.router.navigate(['/']);
     return false;
  }
}
