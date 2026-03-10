import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Theme, CreateThemeRequest } from '../models/theme.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/navigation/themes`;

  findAll(): Observable<Theme[]> {
    return this.http.get<Theme[]>(this.baseUrl);
  }

  findById(id: number): Observable<Theme> {
    return this.http.get<Theme>(`${this.baseUrl}/${id}`);
  }

  create(request: CreateThemeRequest): Observable<Theme> {
    return this.http.post<Theme>(this.baseUrl, request);
  }

  update(id: number, request: CreateThemeRequest): Observable<Theme> {
    return this.http.put<Theme>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
