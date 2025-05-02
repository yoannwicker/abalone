import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {RouteService} from "../route.service";


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  public currentUser: Observable<any>;
  private apiUrl = '/api/auth';
  private currentUserSubject: BehaviorSubject<any>;

  constructor(private http: HttpClient, private routeService: RouteService) {
    // @ts-ignore
    this.currentUserSubject = new BehaviorSubject<any>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserObservable(): Observable<any> {
    return this.currentUser;
  }

  public get currentUserValue(): any {
    return this.currentUserSubject.value;
  }

  login(username: string, password: string) {
    return this.http.post<any>(`${this.apiUrl}/login`, {username, password})
    .pipe(tap(user => {
      localStorage.setItem('currentUser', JSON.stringify(user));
      this.currentUserSubject.next(user);
    }));
  }

  register(user: any) {
    return this.http.post(`${this.apiUrl}/register`, user);
  }

  logout() {
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    this.routeService.goToLogin();
  }
}
