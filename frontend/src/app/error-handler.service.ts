import {Injectable} from "@angular/core";
import {AuthService} from "./auth/auth.service";
import {RouteService} from "./route.service";
import {HttpErrorResponse} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  constructor(private authService: AuthService, private routeService: RouteService) {
  }

  handle(error: HttpErrorResponse): string {
    console.error(error.status + ' ' + error.message);
    if (error.status === 403) {
      this.authService.logout();
      this.routeService.goToLogin();
      return "Access denied. Please log in.";
    }
    if (error.message) {
      return error.message;
    }
    return "Something went wrong; please try again later.";
  }
}
