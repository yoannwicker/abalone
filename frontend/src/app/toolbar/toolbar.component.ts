import {Component} from '@angular/core';
import {MatIconButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {NgIf} from "@angular/common";
import {AuthService} from "../auth/auth.service";
import {RouteService} from "../route.service";

@Component({
  selector: 'app-toolbar',
  standalone: true,
  imports: [
    MatIconButton,
    MatIcon,
    NgIf
  ],
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.css']
})
export class ToolbarComponent {
  applicationName: string = "Pet care";
  appLogo: string = "./assets/logo.png";

  isMobileMenuOpen = false;
  isUserMenuOpen = false;
  isLoggedIn = true;

  constructor(private authService: AuthService, private routeService: RouteService) {
    this.authService.currentUserObservable.subscribe(
      user => {
        this.isLoggedIn = user != null;
      }
    );
  }

  toggleMenu() {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  toggleUserMenu() {
    this.isUserMenuOpen = !this.isUserMenuOpen;
  }

  logout() {
    this.authService.logout()
    this.routeService.goToSearch();
  }

  goToSearch() {
    this.routeService.goToSearch();
  }
}
