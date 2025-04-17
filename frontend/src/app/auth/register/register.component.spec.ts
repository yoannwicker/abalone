import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import {AuthService} from "../auth.service";
import {RouteService} from "../../route.service";
import {of, throwError} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  let authServiceMock: { register: jest.Mock };
  let routeServiceMock: { goToLogin: jest.Mock };

  beforeEach(async () => {
    authServiceMock = { register: jest.fn() };
    routeServiceMock = { goToLogin: jest.fn() };
    await TestBed.configureTestingModule({
      imports: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: RouteService, useValue: routeServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  afterEach(() => {
    jest.restoreAllMocks(); // Restore all mocks after each test
  });

  it('should call register on the auth service and goToLogin on the route service', () => {
    // given
    authServiceMock.register.mockReturnValue(of(new HttpErrorResponse({status: 200})));

    // when
    component.username = 'username';
    component.password = 'password';
    component.register();

    // then
    expect(authServiceMock.register).toHaveBeenCalledWith({ username: 'username', password: 'password' });
    expect(routeServiceMock.goToLogin).toHaveBeenCalled();
  });

  it('should handle 500 error on registration', () => {
    // given
    const errorResponse = new HttpErrorResponse({ status: 500 });
    authServiceMock.register.mockReturnValue(throwError(errorResponse));
    jest.spyOn(window, 'alert').mockImplementation(() => {}); // Mock window.alert

    // when
    component.username = 'username';
    component.password = 'password';
    component.register();

    // then
    expect(authServiceMock.register).toHaveBeenCalledWith({ username: 'username', password: 'password' });
    expect(routeServiceMock.goToLogin).not.toHaveBeenCalled()
    expect(window.alert).toHaveBeenCalledWith('Registration failed');
  });

  it('should handle 404 error on registration', () => {
    // given
    const errorResponse = new HttpErrorResponse({ status: 404 });
    authServiceMock.register.mockReturnValue(throwError(errorResponse));
    jest.spyOn(window, 'alert').mockImplementation(() => {}); // Mock window.alert

    // when
    component.username = 'username';
    component.password = 'password';
    component.register();

    // then
    expect(authServiceMock.register).toHaveBeenCalledWith({ username: 'username', password: 'password' });
    expect(routeServiceMock.goToLogin).not.toHaveBeenCalled()
    expect(window.alert).toHaveBeenCalledWith('Registration failed');
  });
});
