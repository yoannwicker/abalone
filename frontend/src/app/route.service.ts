import {Injectable} from "@angular/core";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class RouteService {

  constructor(private router: Router) {
  }

  public goToLogin(): void {
    this.router.navigate(['/login']).then();
  }

  public goToRegister(): void {
    this.router.navigate(['/register']).then();
  }

  public goToGame(): void {
    this.router.navigate(['/game']).then();
  }
}
