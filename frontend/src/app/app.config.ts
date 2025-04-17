import {ApplicationConfig, importProvidersFrom} from '@angular/core';
import {provideRouter, RouterModule} from '@angular/router';

import {LoginComponent} from "./auth/login/login.component";
import {RegisterComponent} from "./auth/register/register.component";
import {AuthGuard} from "./auth.guard";
import {JwtModule} from "@auth0/angular-jwt";
import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi} from "@angular/common/http";
import {AuthService} from "./auth/auth.service";
import {SearchComponent} from "./search/search.component";
import {JwtInterceptor} from "./jwt.interceptor";
import {environment} from "../environments/environment";

export function tokenGetter() {
  // @ts-ignore
  const currentUser = JSON.parse(localStorage.getItem('currentUser'));
  return currentUser ? currentUser.token : null;
}

export const appConfig: ApplicationConfig = {
  providers: [
    importProvidersFrom(
        RouterModule,
        JwtModule.forRoot({
          config: {
            tokenGetter: tokenGetter,
            allowedDomains: [environment.apiDomain],
            disallowedRoutes: [
              'http://${environment.apiDomain}/api/auth/login',
              'http://${environment.apiDomain}/api/auth/register'
            ]
          }
        })
    ),
    provideRouter([
      {path: 'login', component: LoginComponent},
      {path: 'register', component: RegisterComponent},
      {path: '', redirectTo: '/search', pathMatch: 'full'},
      {path: 'search', component: SearchComponent, canActivate: [AuthGuard]}
    ]),
    provideHttpClient(withInterceptorsFromDi()),
    AuthService,
    AuthGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    }
  ]
};
