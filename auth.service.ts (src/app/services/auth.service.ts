import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { username, password });
  }

  storeUserData(token: string, username: string, role: string, userId: number): void {
    localStorage.setItem('jwt_token', token);
    localStorage.setItem('username', username);
    localStorage.setItem('role', role);
    localStorage.setItem('userId', userId.toString());
  }

  getToken(): string | null {
    return localStorage.getItem('jwt_token');
  }
}
