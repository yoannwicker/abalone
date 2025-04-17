import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {RouteService} from "../../route.service";

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  username: string = '';
  password: string = '';

  constructor(private authService: AuthService, private routeService: RouteService) { }

  register() {
    this.authService.register({ username: this.username, password: this.password }).subscribe(
      () => {
        this.routeService.goToLogin();
      },
      error => {
        alert('Registration failed');
      }
    );
  }
}
