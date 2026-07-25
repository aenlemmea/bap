import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import {
  AuthorizeResponse,
  LoginRequest,
  RegisterUserRequest,
  UserProfile
} from '../../shared/models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly USER_KEY = 'user_profile';
  private readonly API_URL = 'http://localhost:8080/api/v1';

  // TODO: Refactor
  currentUser = signal<UserProfile | null>(this.getStoredUser());

  login(credentials: LoginRequest): Observable<AuthorizeResponse> {
    return this.http.post<AuthorizeResponse>(
      `${this.API_URL}/login`,
      credentials,
      { withCredentials: true }
    ).pipe(
      tap((userProfile) => this.setSessionUser(userProfile))
    );
  }

  register(data: RegisterUserRequest): Observable<void> {
    return this.http.post<void>(
      `${this.API_URL}/register`,
      data,
      { withCredentials: true }
    );
  }

  logout(): void {
    this.http.post(`${this.API_URL}/logout`, {}, { withCredentials: true }).subscribe({
      next: () => this.clearSession(),
      error: () => this.clearSession() // Clear local state even if network call fails
    });
  }

  isAuthenticated(): boolean {
    return this.currentUser() !== null;
  }

  getUserRole(): string | null {
    return this.currentUser()?.role ?? null;
  }

  private setSessionUser(profile: UserProfile): void {
    sessionStorage.setItem(this.USER_KEY, JSON.stringify(profile));
    this.currentUser.set(profile);
  }

  private clearSession(): void {
    sessionStorage.removeItem(this.USER_KEY);
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  private getStoredUser(): UserProfile | null {
    const data = sessionStorage.getItem(this.USER_KEY);
    if (!data) return null;
    try {
      return JSON.parse(data) as UserProfile;
    } catch {
      sessionStorage.removeItem(this.USER_KEY);
      return null;
    }
  }
}
