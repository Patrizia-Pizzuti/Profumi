import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone:false,
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  username = '';
  email = '';
  password = '';

  constructor(private authService: AuthService, private router: Router) {}

  register(): void {
    this.authService.register({ username: this.username, email: this.email, password: this.password }).subscribe(() => {
      this.router.navigate(['/login']);
    });
  }
}
