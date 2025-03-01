import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  isLoggedIn = false;

  constructor(public authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.checkLoginStatus();
  }

  checkLoginStatus(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
  }

  hasRole(role: string): boolean {
    return this.authService.hasRole(role);
  }

  logout(): void {
    this.authService.logout();
    this.isLoggedIn = false;
    this.router.navigate(['/login']);
  }
}
