import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserPreference, CreateUserPreferenceRequest } from '../models/user-preference.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class UserPreferenceService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/navigation/preferences`;

  findById(id: number): Observable<UserPreference> {
    return this.http.get<UserPreference>(`${this.baseUrl}/${id}`);
  }

  findByUserId(userId: number): Observable<UserPreference> {
    return this.http.get<UserPreference>(`${this.baseUrl}/user/${userId}`);
  }

  create(request: CreateUserPreferenceRequest): Observable<UserPreference> {
    return this.http.post<UserPreference>(this.baseUrl, request);
  }

  update(id: number, request: CreateUserPreferenceRequest): Observable<UserPreference> {
    return this.http.put<UserPreference>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
