import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Screen, CreateScreenRequest } from '../models/screen.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ScreenService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/navigation/screens`;

  findAll(): Observable<Screen[]> {
    return this.http.get<Screen[]>(this.baseUrl);
  }

  findById(id: number): Observable<Screen> {
    return this.http.get<Screen>(`${this.baseUrl}/${id}`);
  }

  findByModule(module: string): Observable<Screen[]> {
    return this.http.get<Screen[]>(`${this.baseUrl}/module/${module}`);
  }

  create(request: CreateScreenRequest): Observable<Screen> {
    return this.http.post<Screen>(this.baseUrl, request);
  }

  update(id: number, request: CreateScreenRequest): Observable<Screen> {
    return this.http.put<Screen>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
