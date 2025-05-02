import {Component} from '@angular/core';
import {AuthService} from '../auth.service';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {RouteService} from "../../route.service";

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';

  constructor(private authService: AuthService, private routeService: RouteService) {
  }

  login() {
    this.authService.login(this.username, this.password).subscribe(
      data => {
        this.routeService.goToGame();
      },
      error => {
        alert('Login failed');
      }
    );
  }

  goToRegister(event: Event): void {
    event.preventDefault();
    this.routeService.goToRegister();
  }
}
